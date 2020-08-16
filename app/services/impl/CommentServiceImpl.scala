package services.impl

import dto.CommentDTO
import javax.inject.{Inject, Singleton}
import repository.CommentRepository
import services.CommentService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CommentServiceImpl @Inject()(commentRepository: CommentRepository)(
    ex: ExecutionContext)
    extends CommentService {
  override def addComment(dto: CommentDTO): Future[CommentDTO] = ???

  override def getAll(): Future[List[CommentDTO]] = ???

  override def getAllByPostId(postId: Long): Future[List[CommentDTO]] = ???

  override def getAllByUserId(userId: Long): Future[List[CommentDTO]] = ???
}
