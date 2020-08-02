package repository

import io.getquill._
import javax.inject.Singleton
import models.User

import scala.concurrent.ExecutionContext

@Singleton
class UserRepository(implicit executionContext: ExecutionContext) {

  implicit val ctx = new PostgresAsyncContext[SnakeCase](SnakeCase, "db.default");

  import ctx._

  private val users = quote {
    querySchema[User]("users")
  }

  def getById(id: Long) = {
    val q = quote {
      users.filter(_.id == id)
    }
    ctx.run(q).map(user => user.headOption)
  }

  def getAll() = {
    val q = quote {
      users
    }
    ctx.run(q).map(user => user)
  }
}
