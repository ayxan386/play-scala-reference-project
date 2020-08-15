package error

import play.api.http.{HttpErrorHandler, JsonHttpErrorHandler}
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, Result}

import scala.concurrent.Future

case class ErrorResponse(message: String, status: Int)

object ErrorResponse {
  implicit val writes = Json.writes[ErrorResponse]
}

class ErrorHandler extends HttpErrorHandler {
  override def onClientError(request: RequestHeader,
                             statusCode: Int,
                             message: String): Future[Result] = {
    Future
      .successful(
        Status(statusCode)(buildJsonError(message, statusCode))
      )
  }

  override def onServerError(request: RequestHeader,
                             exception: Throwable): Future[Result] = {
    exception match {
      case ce: CommonHttpError =>
        Future.successful(
          Status(ce.status)(buildJsonError(ce.message, ce.status)))
      case _ =>
        Future.successful(
          InternalServerError(
            buildJsonError(exception.getLocalizedMessage, 500)))
    }
  }

  private def buildErrorResponse(message: String, status: Int) = {
    ErrorResponse(message = message, status = status)
  }

  private def buildJsonError(message: String, status: Int) = {
    Json.toJson(buildErrorResponse(message, status))
  }
}
