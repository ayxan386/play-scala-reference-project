package repository

import io.getquill._
import javax.inject.{Inject, Singleton}
import models.{User, UserRole}

import scala.concurrent.ExecutionContext


case class UserJoined(user: User, role: UserRole)

@Singleton
class UserRepository @Inject()(implicit executionContext: ExecutionContext) {

  implicit val ctx = new PostgresAsyncContext[SnakeCase](SnakeCase, "db.default");

  import ctx._

  private val usersJoinRole = quote {
    for {
      u <- querySchema[User]("users")
      r <- querySchema[UserRole]("user_role").join(u.roleId == _.id)
    } yield (u, r)
  }

  private val users = quote {
    querySchema[User]("users")
  }

  def getById(id: Long) = {
    val q = quote { id: Long =>
      usersJoinRole.filter(_._1.id == id)
    }
    ctx.run(q(lift(id))).map(user => user.headOption)
      .map(op => op.map { tup => UserJoined(tup._1, tup._2) })
  }

  def getAll() = {
    val q = quote {
      usersJoinRole
    }
    ctx.run(q).map(user => user)
      .map(op => op.map { tup => UserJoined(tup._1, tup._2) })
  }

  def addUser(u: User) = {
    val q = quote {
      users.insert(lift(u)).returningGenerated(_.id)
    }

    ctx.run(q).map(id => u.copy(id = id))
  }
}
