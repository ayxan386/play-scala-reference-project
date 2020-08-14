package services.impl

case class UserNotFound(message: String = "User not found", status: Int = 404) extends Exception{

}
