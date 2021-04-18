package photos.brooklyn.interviews.nexthink.microservices

import org.scalatest.flatspec.AnyFlatSpec
import photos.brooklyn.interviews.nexthink.microservices.deployment.MicroserviceDeploymentService
import photos.brooklyn.interviews.nexthink.microservices.model.Deployment

import scala.util.{Success, Try}

/**
 * There isn't much to test as most of the functions return `Unit`. It's more of a sanity test
 */
class MainTest extends AnyFlatSpec {

  private val emptyDeployment = Deployment(true, 1L, "A", Map.empty, Nil)
  private val ds: MicroserviceDeploymentService = new MicroserviceDeploymentService {
    override def deploy(deploymentConfig: String): Try[Deployment] = Success(emptyDeployment)

    override def isCyclic(deploymentConfig: String): Try[Boolean] = Try(false)

    override def isHealthy(deployment: Deployment): Boolean = true
  }

  behavior of "Main"

  it should "deploy" in {
    Main.deploy(ds, """{"a": 1}""")
  }

  it should "printResult" in {
  Main.printResult(emptyDeployment)
  }

  it should "cyclic" in {
    Main.cyclic(ds, """{"a": 1}""")
  }

  it should "health" in {
    Main.health(ds, """{"a": 1}""")
  }

}
