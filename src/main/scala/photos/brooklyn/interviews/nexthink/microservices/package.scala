package photos.brooklyn.interviews.nexthink

import photos.brooklyn.interviews.nexthink.microservices.model.MicroserviceConfiguration

package object microservices {
  type DeploymentConfiguration = Seq[MicroserviceConfiguration]
  type MicroserviceReplicas = Set[Microservice]
}
