package controllers

import dto.PostDTO
import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.PostService

import scala.concurrent.ExecutionContext

@Singleton
class PostController @Inject()(cc: ControllerComponents, postService: PostService)(
  implicit ex: ExecutionContext)
  extends AbstractController(cc) {

  case class PostInputJson(title: String, body: String)

  object PostInputJson {
    implicit val reads = Json.reads[PostInputJson]
  }

  def add(): Action[AnyContent] = Action.async {
    implicit request =>
      val author = request.headers.get("Author")
      Json.fromJson[PostInputJson](request.body.asJson)
        .map(p => postService.save(PostDTO(p.title, p.body, None), author))
  }

  def getById(id: Long): Action[AnyContent] = ???

  def getAll(): Action[AnyContent] = ???

}
