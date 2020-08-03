package services

import com.google.inject.ImplementedBy
import dto.UserResponse
import models.User
import services.impl.UserServiceImpl

import scala.concurrent.Future

@ImplementedBy(classOf[UserServiceImpl])
trait UserService {
  def getAll: Future[List[UserResponse]]

  def getByID(id: Long): Future[Option[UserResponse]]

  def saveUser(u: User): Future[User]
}
