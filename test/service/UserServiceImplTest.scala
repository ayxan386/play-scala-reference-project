package service

import dto.UserResponse
import models.{Address, User, UserRole}
import org.scalatestplus.play._
import services.impl.UserServiceImpl
import org.mockito.Mockito._
import org.mockito.MockitoSugar
import org.specs2.mock.Mockito.mock
import repository.{AddressRepository, UserDB, UserRepository}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.{ExecutionContext, Future}

class UserServiceImplTest extends PlaySpec with MockitoSugar {
  val address = Address(id = 1L, street = "Town", city = "Street", userId = 1L)
  val mockUserResponse = UserResponse(1L,
                                      name = "Tester",
                                      age = 1,
                                      hobby = None,
                                      roleName = "TESTER",
                                      address = List(address))
  val mockUser = User(
    userDB =
      UserDB(id = 1L, name = "Tester", age = 1, hobby = None, roleId = 10),
    role = UserRole(id = 10, name = "Tester", level = 0),
    List(address))

  "UserService" must {
    "initialize when required parameters passed" in {
      val userRepository = mock[UserRepository]
      val addressRepository = mock[AddressRepository]
      val userService = getInstance(userRepository, addressRepository)
      userService.getClass mustBe classOf[UserServiceImpl]
    }

    "return non empty list when getAll called" in {
      val userRepository = mock[UserRepository]
      val addressRepository = mock[AddressRepository]
      when(userRepository.getAll()).thenReturn(Future.successful(List(mockUser)))
      val userService = getInstance(userRepository, addressRepository)
      val res = userService.getAll
      res.map(list => list.isEmpty mustBe false)
    }
  }

  private def getInstance(userRepository: UserRepository,
                          addressRepository: AddressRepository) =
    new UserServiceImpl(userRepository, addressRepository)
}
