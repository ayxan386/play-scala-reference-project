package repository

import io.getquill.{PostgresAsyncContext, SnakeCase}
import javax.inject.{Inject, Singleton}
import models.Address

import scala.concurrent.ExecutionContext

@Singleton
class AddressRepository @Inject()(implicit ex: ExecutionContext) {
  implicit val ctx = new PostgresAsyncContext[SnakeCase](SnakeCase, "db.default");

  import ctx._

  private val addresses = quote {
    querySchema[Address]("address")
  }

  def insertAddress(address: Address) = {
    ctx.run {
      addresses
        .insert(lift(address))
        .returning(_.id)
    }
      .map(id => address.copy(id = id))
  }
}
