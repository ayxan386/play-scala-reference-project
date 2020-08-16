package repository

import io.getquill._
import javax.inject.{Inject, Singleton}
import models.{Address, User, UserRole}

import scala.concurrent.{ExecutionContext, Future}

case class UserJoined(user: UserDB, role: UserRole, address: Address)

case class UserJoinedRes(user: UserDB, role: UserRole, address: Option[Address])

case class UserDB(id: Long,
                  name: String,
                  age: Int,
                  hobby: Option[String],
                  roleId: Int)

@Singleton
class UserRepository @Inject()(implicit executionContext: ExecutionContext) {

  lazy val ctx =
    new PostgresAsyncContext[SnakeCase](SnakeCase, "db.default");

  import ctx._

  private val usersJoinRole = quote {
    for {
      u <- querySchema[UserDB]("users")
      r <- querySchema[UserRole]("user_role").join(u.roleId == _.id)
      a <- querySchema[Address]("address").leftJoin(a => a.userId == u.id)
    } yield (u, r, a)
  }

  private val users = quote {
    querySchema[UserDB]("users")
  }

  def unFlatAddresses(l: Future[Iterable[UserJoinedRes]]) = {
    l.map(list => list.groupBy(u => u.user.id))
      .map(
        kv =>
          kv.values
            .map(
              it =>
                it.map(v =>
                    User(
                      v.user,
                      v.role,
                      it.filter(_.address.isDefined).map(_.address.get).toList))
                  .head)
            .toList)
  }

  def getById(id: Long) = {
    val q = quote { id: Long =>
      usersJoinRole.filter(_._1.id == id)
    }
    convertResSet(
      ctx
        .run(q(lift(id)))
        .map(user => user.headOption))
      .map(l => l.headOption)
  }

  def getByName(name: String): Future[Option[User]] = {
    val q = quote{ name: String =>
      usersJoinRole
        .filter(_._1.name == name)
    }
    convertResSet(
      ctx
        .run(q(lift(name)))
        .map(resSet => resSet.headOption))
      .map(list => list.headOption)
  }

  def convertResSet(
      resSet: Future[Iterable[(UserDB, UserRole, Option[Address])]]) = {
    unFlatAddresses(
      resSet.map(it => it.map(tup => UserJoinedRes(tup._1, tup._2, tup._3)))
    )
  }

  def getAll() = {
    val q = quote {
      usersJoinRole
    }
    convertResSet(ctx.run(q).map(user => user))
  }

  def addUser(u: UserDB) = {
    val q = quote {
      users.insert(lift(u)).returningGenerated(_.id)
    }
    ctx.run(q).map(id => u.copy(id = id))
  }

}
