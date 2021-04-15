package photos.brooklyn.interviews.nexthink.microservices.deployment
import photos.brooklyn.interviews.nexthink.microservices.model.{Deployment, MicroserviceConfiguration}
import photos.brooklyn.interviews.nexthink.microservices.{DeploymentConfiguration, MicroserviceReplicates}

class BasicMicroserviceDeploymentService extends MicroserviceDeploymentService {

  def deployMicroservice(configuration: MicroserviceConfiguration): MicroserviceReplicates = ???

  override def deploy(deploymentConfigFile: String): Deployment = {
    // if there is an error it will get thrown here
    val deploymentConfig = MicroserviceConfiguration.buildConfiguration(deploymentConfigFile).get
    val startTime = System.currentTimeMillis()
    val entryMicroservice = deploymentConfig.filter(x=>x.entryPoint) match {
      case x :: Nil => x.name
      case Nil => throw new IllegalArgumentException(s"Found no entry point in the deploy file $deploymentConfig")
      case moreThanOne => throw new IllegalArgumentException(s"Found ${moreThanOne.size} entry points! Can only have one")
    }
    val microservices = deploy(deploymentConfig)

    val endTime = System.currentTimeMillis()
    Deployment(true, startTime, endTime, entryMicroservice, microservices)
  }

  private def deploy(deploymentConfig: DeploymentConfiguration): Map[String, MicroserviceReplicates] = ???

  /**
   * determines if the microservice tree contains cyclic reference
   *
   * @param deploymentDescription the entry point microservice
   * @return true if there is cyclic reference
   */
  override def isCyclic(deploymentDescription: DeploymentConfiguration): Boolean = ???

  /**
   * determines if the entire deployment is healthy
   *
   * @param deployment
   * @return
   */
  override def isHealthy(deployment: Deployment): Boolean = ???
}
