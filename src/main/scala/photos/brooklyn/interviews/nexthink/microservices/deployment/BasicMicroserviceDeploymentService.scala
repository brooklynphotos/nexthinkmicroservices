package photos.brooklyn.interviews.nexthink.microservices.deployment

import photos.brooklyn.interviews.nexthink.microservices.model.{Deployment, MicroserviceConfiguration}
import photos.brooklyn.interviews.nexthink.microservices.{DeploymentConfiguration, MicroserviceReplicas}

import scala.util.{Failure, Success, Try}

class BasicMicroserviceDeploymentService(deployer: Deployer) extends MicroserviceDeploymentService {

  /**
   * deploys in accordance to the deployment sequence. This is kept private so no outside caller can provide a topologically unordered sequence
   *
   * @param deploymentSequence the stack where the last element is the first to deploy as it depends on nothing
   * @return
   */
  private def deploy(deploymentSequence: DeploymentConfiguration): Try[Map[String, MicroserviceReplicas]] =
    deploymentSequence.foldRight(Try(Map.empty[String, MicroserviceReplicas]))((config, prevVal) =>
      prevVal
        .flatMap(prevMap => deploy(config, prevMap)
          .map(newMap => prevMap + (config.name -> newMap)
          )
        )
    )

  private def deploy(config: MicroserviceConfiguration, prevMap: Map[String, MicroserviceReplicas]): Try[MicroserviceReplicas] =
    deployer.deployMicroservice(config).map(_.map(di=>deployer.buildMicroservice(di, prevMap)).toSet)

  override def deploy(deploymentConfigFile: String): Try[Deployment] = {
    for {
      deploymentConfig <- MicroserviceConfiguration.buildConfiguration(deploymentConfigFile)
      scheduledTasks <- TaskScheduler.createOrderedTasks(deploymentConfig)
    } yield {
      val startTime = System.currentTimeMillis()
      deploy(deploymentConfig) match {
        case Success(microservices) => Deployment(true, startTime, scheduledTasks.head.name, microservices)
        case Failure(exception: Exception) => Deployment(false, startTime, scheduledTasks.head.name, Map.empty, error = Some(exception))
      }
    }
  }

  /**
   * determines if the microservice tree contains cyclic reference
   *
   * @param deploymentDescription the entry point microservice
   * @return true if there is cyclic reference
   */
  override def isCyclic(deploymentDescription: DeploymentConfiguration): Boolean = TaskScheduler.createOrderedTasks(deploymentDescription).isFailure

  /**
   * determines if the entire deployment is healthy
   *
   * @param deployment
   * @return
   */
  override def isHealthy(deployment: Deployment): Boolean = deployment.microservices.forall {
    case (_, replicas) => replicas.forall(m => m.isHealthy)
  }
}
