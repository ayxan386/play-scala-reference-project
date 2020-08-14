package services.impl

import dto.PostDTO
import javax.inject.{Inject, Singleton}
import models.Post
import repository.{PostRepository, UserRepository}
import services.PostService

import scala.concurrent.ExecutionContext

@Singleton
class PostServiceImpl @Inject()(
    postRepository: PostRepository,
    userRepository: UserRepository)(implicit ex: ExecutionContext)
    extends PostService {

  override def save(dto: PostDTO, authorName: Option[String]): PostDTO = {
    authorName
      .map(name => userRepository.getByName(name))
      .get
      .map(op =>
        op.map(user =>
          Post(id = -1L, title = dto.title, body = dto.body, userId = user.userDB.id)))
      .map(postOp => postOp.map(post => postRepository.save(post)))
  }

  override def getAll(): List[PostDTO] = ???

  override def getById(id: Long): PostDTO = ???

}
