package controllers

import dto.CommentDTO
import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.CommentService

import scala.concurrent.ExecutionContext

@Singleton
class CommentController @Inject()(cc: ControllerComponents, commentService: CommentService)(
    implicit ex: ExecutionContext)
    extends AbstractController(cc) {

  case class CommentInput(body: String, postId: Long)
  object CommentInput{
    implicit val reads = Json.reads[CommentInput]
  }


  def addComment() = Action.async {
    implicit request =>
      val author = request.headers.get("Author").getOrElse(throw AuthorNotProvided())
      request.body.asJson match {
        case Some(jsonBody) =>
          val parsed = jsonBody.as[CommentInput]
          val dto = CommentDTO(body = parsed.body, None)
          commentService.addComment(dto, parsed.postId, author)
          .map(dto => Ok(Json.toJson(dto)))
      }
  }

}
