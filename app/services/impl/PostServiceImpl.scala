package services.impl

import dto.{PostDTO, UserResponse}
import javax.inject.{Inject, Singleton}
import models.{Post, User}
import repository.{PostRepository, UserRepository}
import services.PostService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PostServiceImpl @Inject()(
    postRepository: PostRepository,
    userRepository: UserRepository)(implicit ex: ExecutionContext)
    extends PostService {

  override def save(dto: PostDTO,
                    authorName: Option[String]): Future[PostDTO] = {
    val author = authorName
      .map(name => userRepository.getByName(name))
      .map(f => f.map(op => op.getOrElse(throw UserNotFound())))
      .get
    author
      .map(
        user =>
          Post(id = -1L,
               title = dto.title,
               body = dto.body,
               userId = user.userDB.id))
      .map(post => postRepository.save(post))
      .flatMap(f => f)
      .map(model =>
        PostDTO(title = model.title, body = model.body, user = None))
      .map(dto =>
        author.map(user =>
          dto.copy(user = Some(mapUserModelToResponse(Some(user))))))
      .flatMap(f => f)
  }

  private def mapUserModelToResponse(u: Option[User]) = {
    u.map(user => UserResponse(user.userDB, user.role, user.addresses))
      .getOrElse(throw UserNotFound())
  }

  override def getAll(): Future[List[Future[PostDTO]]] = {
    postRepository
      .getAll()
      .map(
        list =>
          list.map(
            model =>
              userRepository
                .getById(model.userId)
                .map(user => mapUserModelToResponse(user))
                .map(userResponse =>
                  PostDTO(title = model.title,
                          body = model.body,
                          user = Some(userResponse)))))
  }

  override def getById(id: Long): Future[PostDTO] = ???

}
