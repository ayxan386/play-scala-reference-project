package dto

import play.api.libs.json.Json

case class CommentDTO(body: String, author: Option[UserResponse])

object CommentDTO{
  implicit val writes = Json.writes[CommentDTO]
}
