package repository

import io.getquill.{PostgresAsyncContext, SnakeCase}
import javax.inject.{Inject, Singleton}
import models.Address

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AddressRepository @Inject()(implicit ex: ExecutionContext) {
  lazy val ctx = new PostgresAsyncContext[SnakeCase](SnakeCase, "db.default");

  import ctx._

  private val addresses = quote {
    querySchema[Address]("address")
  }

  def insertAddress(address: Address): Future[Address] = {
    ctx.run {
      addresses
        .insert(lift(address))
        .returningGenerated(_.id)
    }
      .map(id => address.copy(id = id))
  }
}
