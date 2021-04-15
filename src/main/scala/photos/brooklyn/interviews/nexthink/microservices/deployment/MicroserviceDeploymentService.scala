package photos.brooklyn.interviews.nexthink.microservices.deployment

import photos.brooklyn.interviews.nexthink.microservices.DeploymentConfiguration
import photos.brooklyn.interviews.nexthink.microservices.model.{Deployment, MicroserviceConfiguration}

/**
 * Basic contract to describe functionalities for deploying from a deployment description
 */
trait MicroserviceDeploymentService {

  /**
   * launches microservices stated in the input deployment file
   *
   * @return the microservices launched
   */
  def deploy(deploymentConfig: String): Deployment

  /**
   * determines if the microservice tree contains cyclic reference
   *
   * @param deploymentDescription the entry point microservice
   * @return true if there is cyclic reference
   */
  def isCyclic(deploymentDescription: DeploymentConfiguration): Boolean

  /**
   * determines if the entire deployment is healthy
   *
   * @param deployment
   * @return
   */
  def isHealthy(deployment: Deployment): Boolean
}
