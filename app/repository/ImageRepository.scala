package repository

import javax.inject.{Inject, Singleton}
import models.ImageDM
import io.getquill._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ImageRepository @Inject()(implicit ex: ExecutionContext) {

  lazy val ctx = new PostgresAsyncContext[SnakeCase](SnakeCase, "db.default")

  import ctx._

  private val simpleImage = quote {
    querySchema[ImageDM]("images")
  }

  def save(m: ImageDM): Future[ImageDM] = {
    val q = quote {
      simpleImage
        .insert(lift(m))
        .returningGenerated(_.id)
    }
    ctx.run(q).map(id => m.copy(id = id))
  }

  def getByName(name: String): Future[Option[ImageDM]] = {
    val q = quote { filename: String =>
      simpleImage.filter(_.filename == filename)
    }
    ctx.run(q(lift(name))).map(_.headOption)
  }

}
