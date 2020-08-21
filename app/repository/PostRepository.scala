package repository

import io.getquill._
import javax.inject.{Inject, Singleton}
import models.Post

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PostRepository @Inject()(implicit ex: ExecutionContext) {


  lazy val ctx: PostgresAsyncContext[SnakeCase] =
    new PostgresAsyncContext[SnakeCase](SnakeCase, "db.default");

  import ctx._

  private val postsSolo = quote {
    querySchema[Post]("posts")
  }

  def save(post: Post): Future[Post] = {
    val q = quote {
      postsSolo.insert(lift(post))
        .returningGenerated(_.id)
    }

    ctx.run(q).map(id => post.copy(id = id))
  }

  def getAll(): Future[List[Post]] = {
    val q = quote {
      postsSolo
    }
    ctx.run(q).map(post => post)
  }

  def getById(id: Long): Future[Option[Post]] = {
    val q = quote { id : Long =>
      postsSolo.filter(_.id == id)
    }

    ctx.run(q(lift(id))).map(resSet => resSet.headOption)
  }
}
