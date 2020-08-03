package services.impl

import dto.UserResponse
import javax.inject.{Inject, Singleton}
import models.User
import repository.UserRepository
import services.UserService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserServiceImpl @Inject()(userRepository: UserRepository)(
  implicit ex: ExecutionContext)
  extends UserService {

  override def getAll: Future[List[UserResponse]] =
    userRepository
      .getAll()
      .map(list => list.map(joined => UserResponse(joined.user, joined.role)))

  override def getByID(id: Long): Future[Option[UserResponse]] =
    userRepository.getById(id).map(_.map(uj => UserResponse(uj.user, uj.role)))

  override def saveUser(u: User): Future[User] = userRepository.addUser(u)
}
