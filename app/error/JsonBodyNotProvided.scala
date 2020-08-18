package error

case class JsonBodyNotProvided()
    extends CommonHttpError(message =
                              "Request body is empty or could not be parsed",
                            status = 400)
