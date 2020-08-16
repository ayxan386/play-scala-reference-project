package repository

import io.getquill.{PostgresAsyncContext, SnakeCase}
import javax.inject.{Inject, Singleton}
import models.Comment

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CommentRepository @Inject()(implicit ex: ExecutionContext) {
  lazy val ctx = new PostgresAsyncContext[SnakeCase](SnakeCase, "db.default")

  import ctx._

  private val simpleComment = quote {
    querySchema[Comment]("comments")
  }

  def insertComment(comment: Comment): Future[Comment] = {
      ctx.run(simpleComment.insert(lift(comment)).returningGenerated(_.id))
        .map(id => comment.copy(id = id))
  }


  def getAllByPostId(postId: Long): Future[List[Comment]] = {
    val q = quote{ postId: Long =>
      simpleComment.filter(_.postId == postId)
    }
    ctx.run(q(lift(postId)))
  }

}
