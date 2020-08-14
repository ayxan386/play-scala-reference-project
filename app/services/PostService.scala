package services

import com.google.inject.ImplementedBy
import dto.PostDTO
import services.impl.PostServiceImpl

@ImplementedBy(classOf[PostServiceImpl])
trait PostService {

  def save(dto: PostDTO) : PostDTO

  def getAll() : List[PostDTO]

  def getById(id : Long) : PostDTO

}
