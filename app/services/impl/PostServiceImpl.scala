package services.impl

import dto.PostDTO
import javax.inject.{Inject, Singleton}
import services.PostService

@Singleton
class PostServiceImpl @Inject()() extends PostService {

  override def save(dto: PostDTO): PostDTO = ???

  override def getAll(): List[PostDTO] = ???

  override def getById(id: Long): PostDTO = ???

}
