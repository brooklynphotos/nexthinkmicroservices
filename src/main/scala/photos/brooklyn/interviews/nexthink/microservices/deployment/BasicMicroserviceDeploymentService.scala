package photos.brooklyn.interviews.nexthink.microservices.deployment

import photos.brooklyn.interviews.nexthink.microservices.deployment.TaskScheduler.CycleDetectedException
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
    deployer.deployMicroservice(config).map(_.map(di => deployer.buildMicroservice(di, prevMap)).toSet)

  override def deploy(deploymentConfigFile: String): Try[Deployment] = {
    for {
      deploymentConfig <- MicroserviceConfiguration.buildConfiguration(deploymentConfigFile)
      scheduledTasks <- TaskScheduler.createOrderedTasks(deploymentConfig)
    } yield {
      val startTime = System.currentTimeMillis()
      deploy(deploymentConfig) match {
        case Success(microservices) => Deployment(true, startTime, scheduledTasks.head.name, microservices, scheduledTasks.map(_.name))
        case Failure(exception: Exception) => Deployment(false, startTime, scheduledTasks.head.name, Map.empty, Nil, error = Some(exception))
      }
    }
  }

  override def isCyclic(deploymentConfig: String): Try[Boolean] = MicroserviceConfiguration.buildConfiguration(deploymentConfig).flatMap(isCyclic)

  def isCyclic(deploymentDescription: DeploymentConfiguration): Try[Boolean] =
    TaskScheduler.createOrderedTasks(deploymentDescription) match {
      case Success(_) => Success(false)
      case Failure(_: CycleDetectedException) => Success(true)
      case Failure(t: Throwable) => Failure(t)
    }

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
