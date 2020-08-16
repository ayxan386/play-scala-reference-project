package services

import com.google.inject.ImplementedBy
import dto.CommentDTO
import services.impl.CommentServiceImpl

import scala.concurrent.Future

@ImplementedBy(classOf[CommentServiceImpl])
trait CommentService {
  def addComment(dto: CommentDTO): Future[CommentDTO]

  def getAll(): Future[List[CommentDTO]]

  def getAllByPostId(postId: Long): Future[List[CommentDTO]]

  def getAllByUserId(userId: Long): Future[List[CommentDTO]]

}
