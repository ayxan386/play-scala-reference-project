package services.impl

import error.CommonHttpError

case class FileNotFoundError()
    extends CommonHttpError(message =
                              "DB does not contain file with specified name",
                            status = 404) {}
