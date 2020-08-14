package services

import com.google.inject.ImplementedBy
import dto.PostDTO
import services.impl.PostServiceImpl

import scala.concurrent.Future

@ImplementedBy(classOf[PostServiceImpl])
trait PostService {

  def save(dto: PostDTO, authorName: Option[String]): Future[PostDTO]

  def getAll(): Future[List[Future[PostDTO]]]

  def getById(id: Long): Future[PostDTO]

}
