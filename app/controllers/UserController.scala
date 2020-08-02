package controllers

import javax.inject.{Inject, Singleton}
import models.User
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import repository.UserRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(cc: ControllerComponents, userRepository: UserRepository)(implicit ex: ExecutionContext)
  extends AbstractController(cc) {

  def getById(id: Long) = Action.async {
    request =>
      userRepository.getById(id)
        .map(user => Ok(Json.toJson(user)))
  }

  def getById(id: String): Action[AnyContent] = getById(id.toLong)

  def getAll = Action.async {
    request =>
      userRepository.getAll
        .map(users => Ok(Json.toJson(users)))
  }


  def addUser = Action.async {
    request =>
      request.body.asJson match {
        case Some(jsonBody) =>
          val user = User(id = -1, name = (jsonBody \ "name").as[String], age = (jsonBody \ "age").as[Int], hobby = (jsonBody \ "hobby").asOpt[String])
          userRepository.addUser(user)
            .map(user => Ok(Json.toJson(user)))
        case None => Future.successful(BadRequest(Json.toJson("request body missing")))
      }
  }
}
