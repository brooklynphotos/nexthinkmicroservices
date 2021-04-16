package photos.brooklyn.interviews.nexthink.microservices.deployment

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must
import photos.brooklyn.interviews.nexthink.microservices.Microservice
import photos.brooklyn.interviews.nexthink.microservices.deployment.Deployer.DeployedInstance
import photos.brooklyn.interviews.nexthink.microservices.model.MicroserviceConfiguration

class NoOpDeployerSpec extends AnyFlatSpec with must.Matchers {
  private val service = new NoOpDeployer()

  behavior of "buildMicroservice"

  it should "return NoOpMicroservice correctly" in {
    val servicesMapping: Map[String, Set[Microservice]] = Map(
      "C" -> Set(NoOpMicroservice(2, "C", false, Set.empty, true)),
      "B" -> Set(NoOpMicroservice(3, "B", false, Set.empty, true))
    )
    val newMS = service.buildMicroservice(DeployedInstance(1, "A", true, Set("C")), servicesMapping)
    newMS.id mustBe 1
    newMS.dependencies.size mustBe 1
  }

  behavior of "deployMicroservice"


  it should "return correct number of replicas" in {
    val config: MicroserviceConfiguration = MicroserviceConfiguration("A", true, 3, Set.empty)
    val instances = service.deployMicroservice(config).get
    instances.size mustBe 3
  }

}
