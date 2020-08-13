package dto

import models.{Address, UserRole}
import play.api.libs.json.Json
import repository.UserDB

case class UserResponse(id: Long, name: String, age: Int, hobby: Option[String], roleName: String, address: List[Address]) {

}


object UserResponse {
  implicit val writes = Json.writes[UserResponse]

  def apply(id: Long, name: String, age: Int, hobby: Option[String], roleName: String, address: List[Address]): UserResponse
  = new UserResponse(id, name, age, hobby, roleName, address)

  def apply(user: UserDB, role: UserRole, address: List[Address]): UserResponse
  = this.apply(user.id, user.name, user.age, user.hobby, role.name, address)
}
