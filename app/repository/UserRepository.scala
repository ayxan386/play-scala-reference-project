package repository

import io.getquill._
import javax.inject.{Inject, Singleton}
import models.{Address, User, UserRole}

import scala.concurrent.ExecutionContext


case class UserJoined(user: User, role: UserRole, address: Address)

@Singleton
class UserRepository @Inject()(implicit executionContext: ExecutionContext) {

  implicit val ctx = new PostgresAsyncContext[SnakeCase](SnakeCase, "db.default");

  import ctx._

  private val usersJoinRole = quote {
    for {
      u <- querySchema[User]("users")
      r <- querySchema[UserRole]("user_role").join(u.roleId == _.id)
      a <- querySchema[Address]("address").leftJoin(a => a.userId == u.id)
    } yield (u, r, a)
  }

  private val users = quote {
    querySchema[User]("users")
  }

  def getById(id: Long) = {
    val q = quote { id: Long =>
      usersJoinRole.filter(_._1.id == id)
    }
    ctx.run(q(lift(id))).map(user => user.headOption)
      .map(op => op.map { tup => UserJoined(tup._1, tup._2, tup._3.orNull) })
  }

  def getAll() = {
    val q = quote {
      usersJoinRole
    }
    ctx.run(q).map(user => user)
      .map(op => op.map { tup => UserJoined(tup._1, tup._2, tup._3.orNull) })
  }

  def addUser(u: User) = {
    val q = quote {
      users.insert(lift(u)).returningGenerated(_.id)
    }

    ctx.run(q).map(id => u.copy(id = id))
  }
}
