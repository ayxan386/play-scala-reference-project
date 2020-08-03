package services

import dto.UserResponse
import models.User

import scala.concurrent.Future

trait UserService {
  def getAll: Future[List[UserResponse]]

  def getByID(id: Long): Future[Option[UserResponse]]

  def saveUser(u: User): Future[User]
}
