package services.impl

import dto.CommentDTO
import error.notfound.UserNotFound
import javax.inject.{Inject, Singleton}
import models.Comment
import repository.{CommentRepository, UserRepository}
import services.{CommentService, UserService}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CommentServiceImpl @Inject()(
    commentRepository: CommentRepository,
    userRepository: UserRepository,
    userService: UserService)(implicit ex: ExecutionContext)
    extends CommentService {
  override def addComment(dto: CommentDTO,
                          postId: Long,
                          username: String): Future[CommentDTO] =
    userRepository
      .getByName(username)
      .map(op => op.map(_.userDB.id).getOrElse(throw UserNotFound()))
      .map(
        userId =>
          Comment(
            id = -1L,
            body = dto.body,
            postId = postId,
            userId = userId
        ))
      .map(commentRepository.insertComment)
      .flatMap(f => f)
      .map(fromModel)
      .flatMap(f => f)

  override def getAll(): Future[List[CommentDTO]] =
    commentRepository
      .getAll()
      .map(list => list.map(fromModel))
      .map(lf => Future.sequence(lf))
      .flatMap(f => f)

  override def getAllByPostId(postId: Long): Future[List[CommentDTO]] =
    commentRepository
      .getAllByPostId(postId)
      .map(list => list.map(fromModel))
      .map(lf => Future.sequence(lf))
      .flatMap(f => f)

  override def getAllByUserId(userId: Long): Future[List[CommentDTO]] = ???

  private def fromModel(model: Comment) =
    userService
      .getByID(model.userId)
      .map(ur => CommentDTO(body = model.body, author = ur))
}
