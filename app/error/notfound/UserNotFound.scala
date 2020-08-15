package error.notfound

import error.CommonHttpError

case class UserNotFound()
  extends CommonHttpError(message = "User not found", status = 404) {}
