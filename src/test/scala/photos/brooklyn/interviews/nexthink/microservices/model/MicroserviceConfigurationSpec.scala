package photos.brooklyn.interviews.nexthink.microservices.model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must

class MicroserviceConfigurationSpec extends AnyFlatSpec with must.Matchers {

  behavior of "buildConfiguration"

  it should "build correct configuration given correct json input" in {
    val config = MicroserviceConfiguration.buildConfiguration("input-sample.json").get
    config.length mustBe 3
  }

  behavior of "isValid"
  it should "return true when given valid input" in {
    val config = List(
      MicroserviceConfiguration("A", true, 3, Set.empty),
      MicroserviceConfiguration("B", true, 3, Set.empty),
      MicroserviceConfiguration("C", true, 3, Set.empty)
    )
    MicroserviceConfiguration.isValid(config) mustBe true
  }

  it should "return false when given duplicate names input" in {
    val config = List(
      MicroserviceConfiguration("A", true, 3, Set.empty),
      MicroserviceConfiguration("A", true, 3, Set.empty),
      MicroserviceConfiguration("C", true, 3, Set.empty)
    )
    MicroserviceConfiguration.isValid(config) mustBe false
  }

}
