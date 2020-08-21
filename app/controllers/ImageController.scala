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

  def saveImageDirect = Action(parse.multipartFormData).async {
    implicit request =>
      println("request received")
      request.body
        .file("image")
        .map(f => imageService.saveImageDirect(f))
        .map(f => f.map(s => Ok(Json.toJson(s))))
        .get
  }

  def saveImageDB = Action(parse.multipartFormData).async { implicit request =>
    println("request received")
    request.body
      .file("image")
      .map(f => imageService.saveImage(f))
      .map(f => f.map(s => Ok(Json.toJson(s))))
      .get
  }

  def getByName(name: String) = Action { implicit request =>
    Ok.sendFile(
      imageService
        .getFileByName(name))
  }

//  def getByName(name: String) = Action { implicit request =>
//    Ok.sendFile(
//      imageService
//        .getFileByName(name))
//  }
}
