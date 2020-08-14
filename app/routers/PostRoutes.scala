package routers

import controllers.PostController
import javax.inject.{Inject, Singleton}
import play.api.routing.Router._
import play.api.routing._
import play.api.routing.sird._

@Singleton
class PostRoutes @Inject()(postController: PostController) extends SimpleRouter {
  override def routes: Routes = {
    case GET(p"/") => postController.getAll()
    case GET(p"/$id") => postController.getById(id.toLong)
    case POST(p"/") => postController.add()
  }
}
