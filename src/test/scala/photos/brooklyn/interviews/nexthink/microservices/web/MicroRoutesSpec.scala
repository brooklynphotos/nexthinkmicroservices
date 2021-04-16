package photos.brooklyn.interviews.nexthink.microservices.web

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.matchers.must
import org.scalatest.wordspec.AnyWordSpec
import photos.brooklyn.interviews.nexthink.microservices.DeploymentConfiguration
import photos.brooklyn.interviews.nexthink.microservices.deployment.MicroserviceDeploymentService
import photos.brooklyn.interviews.nexthink.microservices.model.Deployment

import scala.util.{Success, Try}

class MicroRoutesSpec extends AnyWordSpec with must.Matchers with ScalatestRouteTest {

  private val routes = new MicroRoutes(new MicroserviceDeploymentService {
    override def deploy(deploymentConfig: String): Try[Deployment] = Success(Deployment(true, 1L, "A", Map.empty, Seq("A"), deploymentCompletionTime = 2L))

    override def isCyclic(deploymentDescription: DeploymentConfiguration): Boolean = true

    override def isHealthy(deployment: Deployment): Boolean = true
  }).routes

  "Rest service" should {
    "return UP for a ping" in {
      Get("/ping") ~> routes ~> check {
        responseAs[String] mustBe "UP"
      }
    }

    "return json string for deployment" in {
      Post("/deploy") ~> routes ~> check {
        responseAs[String] must include("\"entryMicroservice\":\"A\"")
      }
    }

  }

}
