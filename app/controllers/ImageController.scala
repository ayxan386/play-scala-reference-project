package controllers

import akka.stream.scaladsl.Source
import akka.util.ByteString
import javax.inject.{Inject, Singleton}
import play.api.http.HttpEntity
import play.api.libs.json.Json
import play.api.mvc.{
  AbstractController,
  ControllerComponents,
  ResponseHeader,
  Result
}
import services.ImageService

import scala.concurrent.ExecutionContext

@Singleton
class ImageController @Inject()(
    cc: ControllerComponents,
    imageService: ImageService)(implicit ex: ExecutionContext)
    extends AbstractController(cc) {

  def saveImage = Action(parse.multipartFormData).async { implicit request =>
    println("request received")
    request.body
      .file("image")
      .map(f => imageService.saveImage(f))
      .map(f => f.map(s => Ok(Json.toJson(s))))
      .get
  }

  def getByName(name: String) = Action.async { implicit request =>
    imageService
      .getByFilename(name)
      .map(dm => {
        val source: Source[ByteString, _] =
          Source.single(ByteString(dm.content))
        Result(
          header = ResponseHeader(200, Map.empty),
          body = HttpEntity.Streamed(data = source,
                                     contentType = Some(dm.fileType),
                                     contentLength = Some(dm.size.toLong))
        )
      })
  }
}
