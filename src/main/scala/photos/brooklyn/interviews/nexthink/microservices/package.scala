package photos.brooklyn.interviews.nexthink

import photos.brooklyn.interviews.nexthink.microservices.model.MicroserviceConfiguration

package object microservices {
  type DeploymentConfiguration = List[MicroserviceConfiguration]
  type MicroserviceReplicates = List[Microservice]
}
