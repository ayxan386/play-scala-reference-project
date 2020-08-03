package models

import play.api.libs.json.Json

case class User(id: Long, name: String, age: Int, hobby: Option[String], role: UserRole)

object User {
  implicit val writes = Json.writes[User]

}