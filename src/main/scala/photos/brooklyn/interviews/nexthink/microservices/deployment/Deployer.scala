package photos.brooklyn.interviews.nexthink.microservices.deployment

import photos.brooklyn.interviews.nexthink.microservices.deployment.Deployer.{DeployedInstance, DeployedReplicas}
import photos.brooklyn.interviews.nexthink.microservices.model.MicroserviceConfiguration
import photos.brooklyn.interviews.nexthink.microservices.{Microservice, MicroserviceReplicas}

import scala.util.Try

/**
 * interface to any deployment mechanism
 */
trait Deployer {

  /**
   * converts the deployed instance to a microservice instance of its choice
   * @param instance
   * @param deploymentState
   * @return
   */
  def buildMicroservice(instance: DeployedInstance, deploymentState: Map[String, MicroserviceReplicas]): Microservice

  /**
   * needs to be implemented according to the deployment mechanism
   *
   * @param configuration
   * @return
   */
  def deployMicroservice(configuration: MicroserviceConfiguration): Try[DeployedReplicas]

}

object Deployer {
  type DeployedReplicas = Seq[DeployedInstance]
  case class DeployedInstance(id: Int, name: String, isEntryPoint: Boolean, dependencies: Set[String])
}


