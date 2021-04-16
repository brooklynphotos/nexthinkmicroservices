package photos.brooklyn.interviews.nexthink.microservices.web

import akka.http.scaladsl.server.Directives._
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write
import photos.brooklyn.interviews.nexthink.microservices.deployment.MicroserviceDeploymentService

class MicroRoutes(deploymentService: MicroserviceDeploymentService) {
  implicit def json4sFormat = DefaultFormats

  def routes =
      path("ping") {
        get {
          complete("UP")
        }
      } ~
      path("deploy") {
        post {
          entity(as[String]) { config =>
            complete {
              write(deploymentService.deploy(config))
            }
          }
        }
      }
}
