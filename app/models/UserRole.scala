package models

import play.api.libs.json.Json

case class UserRole(id: Int, name: String, level: Byte) {

}

object UserRole {
  implicit val writes = Json.writes[UserRole]
}
