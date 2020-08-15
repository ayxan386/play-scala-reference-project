package error.notfound

import error.CommonHttpError

case class PostNotFound()
  extends CommonHttpError(message = "Post by that id not found", status = 404) {}
