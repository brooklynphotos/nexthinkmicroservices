package photos.brooklyn.interviews.nexthink.microservices.deployment

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must
import photos.brooklyn.interviews.nexthink.microservices.Microservice
import photos.brooklyn.interviews.nexthink.microservices.model.{Deployment, MicroserviceConfiguration}

class BasicMicroserviceDeploymentServiceSpec extends AnyFlatSpec with must.Matchers {

  private val service = new BasicMicroserviceDeploymentService(new NoOpDeployer())

  behavior of "isCyclic"

  it should "return true if there is a cycle" in {
    val deploymentDescription = List(
      MicroserviceConfiguration("A", true, 2, Set("B")),
      MicroserviceConfiguration("B", false, 2, Set("A"))
    )
    service.isCyclic(deploymentDescription) mustBe true
  }

  it should "return false given there is no cycle" in {
    val deploymentDescription = List(
      MicroserviceConfiguration("A", true, 2, Set("B")),
      MicroserviceConfiguration("B", false, 2, Set("C"))
    )
    service.isCyclic(deploymentDescription) mustBe true
  }

  it should "return false given there a more complex graph with no cycle" in {
    val deploymentDescription = List(
      MicroserviceConfiguration("A", true, 2, Set("B", "D")),
      MicroserviceConfiguration("B", false, 2, Set("C")),
      MicroserviceConfiguration("C", false, 2, Set("D"))
    )
    service.isCyclic(deploymentDescription) mustBe true
  }

  behavior of "isHealthy"
  it should "return true if all microservices are healthy" in {
    val BList: Set[Microservice] = Set(NoOpMicroservice(1, "B", false, Set.empty, true))
    val AList: Set[Microservice] = Set(NoOpMicroservice(1, "A", true, BList, true))
    val deployment = Deployment(true, 1L, "A", Map(
      "A" -> AList,
      "B" -> BList
    ), deploymentCompletionTime = 2L)
    service.isHealthy(deployment) mustBe true
  }

  it should "return false if one microservice is unhealthy" in {
    val BList: Set[Microservice] = Set(NoOpMicroservice(1, "B", true, Set.empty, true))
    val AList: Set[Microservice] = Set(NoOpMicroservice(1, "A", true, BList, false))
    val deployment = Deployment(true, 1L, "A", Map(
      "A" -> AList,
      "B" -> BList
    ), deploymentCompletionTime = 2L)
    service.isHealthy(deployment) mustBe false
  }

  behavior of "deploy"
  it should "return a Deploy instance when no errors were found" in {

  }

  it should "throw an error if the configuration is not correct" in {

  }

}
