package controllers

import error.CommonHttpError

case class AuthorNotProvided() extends CommonHttpError(message = "Required auth headers are not provided", status = 401){

}
