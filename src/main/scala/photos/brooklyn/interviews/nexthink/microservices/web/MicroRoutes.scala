package photos.brooklyn.interviews.nexthink.microservices.web

import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCode
import akka.http.scaladsl.model.StatusCodes.{BadRequest, InternalServerError, OK}
import akka.http.scaladsl.model.headers.`Content-Type`
import akka.http.scaladsl.server.Directives._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write
import photos.brooklyn.interviews.nexthink.microservices.deployment.MicroserviceDeploymentService
import photos.brooklyn.interviews.nexthink.microservices.deployment.TaskScheduler.CycleDetectedException

import scala.util.{Failure, Success, Try}

class MicroRoutes(deploymentService: MicroserviceDeploymentService) {

  import photos.brooklyn.interviews.nexthink.microservices.web.MicroRoutes.respond

  def routes = cors() {
    path("ping") {
      get {
        complete("UP")
      }
    } ~
      path("deploy") {
        post {
          parameter("dryRun".optional) { dryRun =>
            entity(as[String]) { config =>
              respond(if (dryRun.isEmpty) deploymentService.deploy(config) else deploymentService.isCyclic(config).map(x => Map("cyclic" -> x)))
            }
          }
        }
      }
  }
}

object MicroRoutes {
  implicit private def json4sFormat = DefaultFormats

  final case class Response[A](data: A, status: Int)

  def respond[A](data: Try[A]) = data match {
    case Success(deployment) => composeResponse(deployment, OK)
    case Failure(_: CycleDetectedException) => composeResponse("cycle detected", BadRequest.intValue)
    case Failure(exception) => composeResponse(s"Exception: ${exception.getMessage}", InternalServerError.intValue)
  }

  def composeResponse[T](data: T, status: StatusCode) = complete(status, List(`Content-Type`(`application/json`)), write(Response(data, status.intValue)))


}
