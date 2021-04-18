package photos.brooklyn.interviews.nexthink.microservices.web

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.matchers.must
import org.scalatest.wordspec.AnyWordSpec
import photos.brooklyn.interviews.nexthink.microservices.deployment.MicroserviceDeploymentService
import photos.brooklyn.interviews.nexthink.microservices.deployment.TaskScheduler.CycleDetectedException
import photos.brooklyn.interviews.nexthink.microservices.model.Deployment

import scala.util.{Failure, Success, Try}

class MicroRoutesSpec extends AnyWordSpec with must.Matchers with ScalatestRouteTest {

  private val routes = new MicroRoutes(new MicroserviceDeploymentService {
    override def deploy(deploymentConfig: String): Try[Deployment] = Success(Deployment(true, 1L, "A", Map.empty, Seq("A"), deploymentCompletionTime = 2L))

    override def isCyclic(deploymentConfig: String): Try[Boolean] = Try(false)

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

    "return json string for dryrun" in {
      Post("/deploy?dryRun") ~> routes ~> check {
        responseAs[String] must include("\"cyclic\":false")
      }
    }

    "return error message for real run that has true cyclic graph as input" in {
      val cyclicRoutes = new MicroRoutes(new MicroserviceDeploymentService {
        override def deploy(deploymentConfig: String): Try[Deployment] = Failure(CycleDetectedException("A"))

        override def isCyclic(deploymentConfig: String): Try[Boolean] = Try(true)

        override def isHealthy(deployment: Deployment): Boolean = true
      }).routes
      Post("/deploy") ~> cyclicRoutes ~> check {
        status mustBe StatusCodes.BadRequest
        responseAs[String] must include("cycle detected")
      }
    }

    "return error message for real run that has a graph that raises other errors" in {
      val errorRoutes = new MicroRoutes(new MicroserviceDeploymentService {
        override def deploy(deploymentConfig: String): Try[Deployment] = Failure(new OutOfMemoryError())

        override def isCyclic(deploymentConfig: String): Try[Boolean] = Failure(new OutOfMemoryError())

        override def isHealthy(deployment: Deployment): Boolean = false
      }).routes
      Post("/deploy") ~> errorRoutes ~> check {
        status mustBe StatusCodes.InternalServerError
        responseAs[String] must include(":500")
      }
    }

  }

}
