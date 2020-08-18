package service

import org.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import repository.{CommentRepository, UserRepository}
import services.{CommentService, UserService}
import services.impl.CommentServiceImpl

import scala.concurrent.ExecutionContext.Implicits.global

class CommentServiceImplTest extends PlaySpec with MockitoSugar {

  "CommentServiceImpl" must {
    val commentRepository = mock[CommentRepository]
    val userRepository = mock[UserRepository]
    val userService = mock[UserService]
    "correctly initialize and be instance of CommentService" in {
      val service = getInstance(commentRepository, userRepository, userService)
      service != null mustBe true
      service.isInstanceOf[CommentServiceImpl] mustBe true
      service.isInstanceOf[CommentService] mustBe true
    }

  }

  private def getInstance(commentRepository: CommentRepository,
                          userRepository: UserRepository,
                          userService: UserService) =
    new CommentServiceImpl(commentRepository, userRepository, userService)

}
