package controllers

import dto.PostDTO
import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{
  AbstractController,
  Action,
  AnyContent,
  ControllerComponents
}
import services.PostService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PostController @Inject()(
    cc: ControllerComponents,
    postService: PostService)(implicit ex: ExecutionContext)
    extends AbstractController(cc) {

  case class PostInputJson(title: String, body: String)

  object PostInputJson {
    implicit val reads = Json.reads[PostInputJson]
  }

  def add(): Action[AnyContent] = Action.async { implicit request =>
    val author = request.headers.get("Author")
    request.body.asJson match {
      case Some(jsonBody) =>
        val dto = jsonBody.as[PostInputJson]
        postService
          .save(PostDTO(title = dto.title, body = dto.body, user = None, Nil),
                author)
          .map(p => Ok(Json.toJson(p)))
      case None => Future(BadRequest(Json.toJson("Missing request body")))
    }
  }

  def getById(id: Long): Action[AnyContent] = Action.async { implicit request =>
    postService
      .getById(id)
      .map(post => Ok(Json.toJson(post)))
  }

  def getAll(): Action[AnyContent] = Action.async { implicit request =>
    postService
      .getAll()
      .map(listOfFutures => Future.sequence(listOfFutures))
      .map(f => f.map(list => Ok(Json.toJson(list))))
      .flatMap(f => f)
  }

}
