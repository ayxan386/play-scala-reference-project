package service

import dto.{PostDTO, UserResponse}
import models.{Address, Post, User, UserRole}
import org.mockito.ArgumentMatchers.{any, anyLong, anyString}
import org.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import repository.{PostRepository, UserDB, UserRepository}
import services.{CommentService, PostService}
import services.impl.PostServiceImpl

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PostServiceImplTest extends PlaySpec with MockitoSugar {

  val address = Address(id = 1L, street = "Town", city = "Street", userId = 1L)
  val mockUser = User(
    userDB =
      UserDB(id = 1L, name = "Tester", age = 1, hobby = None, roleId = 10),
    role = UserRole(id = 10, name = "Tester", level = 0),
    List(address))
  val mockUserResponse = UserResponse(1L,
                                      name = "Tester",
                                      age = 1,
                                      hobby = None,
                                      roleName = "TESTER",
                                      address = List(address))
  val mockPost =
    Post(id = 1L, title = "Test post", body = "Test Post", userId = 1L)
  val mockPostDTO = PostDTO(title = "Test post",
                            body = "Test Post",
                            user = Some(mockUserResponse),
                            comments = Nil)

  "PostServiceImpl" must {
    val postRepository = mock[PostRepository]
    val userRepository = mock[UserRepository]
    val commentService = mock[CommentService]

    when(userRepository.getById(anyLong()))
      .thenReturn(Future.successful(Some(mockUser)))
    when(commentService.getAllByPostId(anyLong()))
      .thenReturn(Future.successful(Nil))
    "be properly initialized and be of type PostService" in {
      val service = getInstance(postRepository, userRepository, commentService)
      service == null mustBe false
      service.isInstanceOf[PostServiceImpl] mustBe true
      service.isInstanceOf[PostService] mustBe true
    }

    "return non empty list of PostDTO when getAll called" in {
      when(postRepository.getAll())
        .thenReturn(Future.successful(List(mockPost)))
      val service = getInstance(postRepository, userRepository, commentService)
      val res = service.getAll()
      res.map(list => list.isEmpty mustBe false)
    }

    "return PostDTO when getById called" in {
      when(postRepository.getById(anyLong()))
        .thenReturn(Future.successful(Some(mockPost)))
      val service = getInstance(postRepository, userRepository, commentService)
      val res = service.getById(1L)
      res.map(postDTO => postDTO == null mustBe false)
    }

    "save and return PostDTO when author name provider and save called" in {
      when(postRepository.save(any()))
        .thenReturn(Future.successful(mockPost))
      when(userRepository.getByName(anyString()))
        .thenReturn(Future.successful(Some(mockUser)))
      val service = getInstance(postRepository, userRepository, commentService)
      val res = service.save(mockPostDTO, Some(mockUserResponse.name))
      res.map(postDTO => postDTO == null mustBe false)
      res.map(postDTO =>
        postDTO.user.exists(_.name == mockUserResponse.name) mustBe true)
    }
  }

  private def getInstance(postRepository: PostRepository,
                          userRepository: UserRepository,
                          commentService: CommentService) =
    new PostServiceImpl(postRepository, userRepository, commentService)

}
