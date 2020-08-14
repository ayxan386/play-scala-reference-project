package repository

import javax.inject.{Inject, Singleton}
import models.Post

import scala.concurrent.ExecutionContext

@Singleton
class PostRepository @Inject()(ex: ExecutionContext) {
  def save(post: Post): Post = ???

}
