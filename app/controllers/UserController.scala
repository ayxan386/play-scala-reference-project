package controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import repository.UserRepository

import scala.concurrent.ExecutionContext

@Singleton
class UserController @Inject() (cc: ControllerComponents, userRepository: UserRepository)(implicit ex : ExecutionContext)
extends AbstractController(cc){

  def getById(id: Long) = Action.async {
    request =>
      userRepository.getById(id)
        .map(Ok(Json.toJson(_)))
  }
}
