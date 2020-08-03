package models

import play.api.libs.json.Json

case class UserRole(id: Int, name: String, level: Int) {

}

object UserRole {
  implicit val writes = Json.writes[UserRole]
}
