package photos.brooklyn.interviews.nexthink.microservices.deployment

import photos.brooklyn.interviews.nexthink.microservices.model.Deployment

import scala.util.Try

/**
 * Basic contract to describe functionalities for deploying from a deployment description
 */
trait MicroserviceDeploymentService {

  /**
   * launches microservices stated in the input deployment file
   * @param deploymentConfig the entry point microservice
   * @return the microservices launched or the exception that caused it to abort
   */
  def deploy(deploymentConfig: String): Try[Deployment]

  /**
   * determines if the microservice tree contains cyclic reference
   *
   * @param deploymentConfig the entry point microservice
   * @return true if there is cyclic reference
   */
  def isCyclic(deploymentConfig: String): Try[Boolean]

  /**
   * determines if the entire deployment is healthy
   *
   * @param deployment
   * @return
   */
  def isHealthy(deployment: Deployment): Boolean
}
