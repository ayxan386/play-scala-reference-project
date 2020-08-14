package error

case class ErrorSaving(message: String = "Error while saving", status: Int = 500) extends Exception{

}
