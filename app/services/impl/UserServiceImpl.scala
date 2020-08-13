package services.impl

import dto.UserResponse
import javax.inject.{Inject, Singleton}
import models.User
import repository.{AddressRepository, UserRepository}
import services.UserService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserServiceImpl @Inject()(userRepository: UserRepository,
                                addressRepository: AddressRepository)(
                                 implicit ex: ExecutionContext)
  extends UserService {

  override def getAll: Future[List[UserResponse]] =
    userRepository
      .getAll()
      .map(list => list.map(joined => UserResponse(joined.userDB, joined.role, joined.addresses)))

  override def getByID(id: Long): Future[Option[UserResponse]] =
    userRepository.getById(id).map(_.map(uj => UserResponse(uj.userDB, uj.role, uj.addresses)))

  override def saveUser(u: User): Future[UserResponse] = {
    userRepository.addUser(u.userDB).map(saved => {
      u.addresses.map(a => a.copy(userId = saved.id)).map(addressRepository.insertAddress)
      UserResponse(saved, u.role, u.addresses)
    })
  }
}
