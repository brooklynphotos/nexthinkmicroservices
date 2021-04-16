package photos.brooklyn.interviews.nexthink.microservices.deployment

import photos.brooklyn.interviews.nexthink.microservices.deployment.Deployer.{DeployedInstance, DeployedReplicas}
import photos.brooklyn.interviews.nexthink.microservices.model.MicroserviceConfiguration
import photos.brooklyn.interviews.nexthink.microservices.{Microservice, MicroserviceReplicas}

import scala.util.Try

class NoOpDeployer extends Deployer {
  override def deployMicroservice(configuration: MicroserviceConfiguration): Try[DeployedReplicas] =
    Try((0 until configuration.replicas).map(idx =>
      DeployedInstance(configuration.name.hashCode + idx, configuration.name, configuration.entryPoint, configuration.dependencies)))

  override def buildMicroservice(instance: DeployedInstance, deploymentState: Map[String, MicroserviceReplicas]): Microservice =
    NoOpMicroservice(instance.id, instance.name, instance.isEntryPoint, instance.dependencies.flatMap(deploymentState(_)), true)
}
case class NoOpMicroservice(id: Int, name: String, isEntryPoint: Boolean, dependencies: MicroserviceReplicas, isHealthy: Boolean) extends Microservice
