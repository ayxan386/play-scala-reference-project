package models

import play.api.libs.json.Json

case class Address(id: Long, street: String, city: String, userId : Long)

object Address {
  implicit val writes = Json.writes[Address]
  implicit val reads = Json.reads[Address]
}
