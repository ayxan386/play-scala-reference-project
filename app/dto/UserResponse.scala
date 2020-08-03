package dto

import models.{User, UserRole}
import play.api.libs.json.Json

case class UserResponse(id: Long, name: String, age: Int, hobby: Option[String], roleName: String) {

}


object UserResponse {
  implicit val writes = Json.writes[UserResponse]

  def apply(id: Long, name: String, age: Int, hobby: Option[String], roleName: String): UserResponse = new UserResponse(id, name, age, hobby, roleName)

  def apply(user: User, role: UserRole): UserResponse = this.apply(user.id, user.name, user.age, user.hobby, role.name)
}
