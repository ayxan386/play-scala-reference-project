package dto

import play.api.libs.json.Json

case class SimpleResponseDTO(message: String)

object SimpleResponseDTO{
  implicit val writes = Json.writes[SimpleResponseDTO]
}
