package dto

import play.api.libs.json.Json

case class PostDTO(title: String, body: String, user: Option[UserResponse], comments: List[CommentDTO]) {

}

object PostDTO {
  implicit val writes = Json.writes[PostDTO]
}
