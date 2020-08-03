package controllers

import javax.inject.{Inject, Singleton}
import models.User
import play.api.libs.json.Json
import play.api.mvc.{
  AbstractController,
  Action,
  AnyContent,
  ControllerComponents
}
import repository.UserRepository
import services.UserService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(
    cc: ControllerComponents,
    userService: UserService)(implicit ex: ExecutionContext)
    extends AbstractController(cc) {

  def getById(id: Long) = Action.async { request =>
    userService
      .getByID(id)
      .map(user => Ok(Json.toJson(user)))
  }

  def getById(id: String): Action[AnyContent] = getById(id.toLong)

  def getAll = Action.async { request =>
    userService.getAll
      .map(users => Ok(Json.toJson(users)))
  }

  def addUser = Action.async { request =>
    request.body.asJson match {
      case Some(jsonBody) =>
        val user = User(id = -1,
                        name = (jsonBody \ "name").as[String],
                        age = (jsonBody \ "age").as[Int],
                        hobby = (jsonBody \ "hobby").asOpt[String],
                        roleId = (jsonBody \ "roleId").as[Int])
        userService
          .saveUser(user)
          .map(user => Ok(Json.toJson(user)))
      case None =>
        Future.successful(BadRequest(Json.toJson("request body missing")))
    }
  }
}
