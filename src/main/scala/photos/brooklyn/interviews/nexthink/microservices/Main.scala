package photos.brooklyn.interviews.nexthink.microservices

import photos.brooklyn.interviews.nexthink.microservices.deployment.{BasicMicroserviceDeploymentService, MicroserviceDeploymentService, NoOpDeployer}
import photos.brooklyn.interviews.nexthink.microservices.model.Deployment

import scala.util.{Failure, Success}

/**
 * a sanity check for whether the deployment logic works
 */
object Main {

  def printResult(deployment: Deployment) = {
    println(deployment.isSuccessful)
    println("Deployed in this order:")
    deployment.order foreach {
      name => println(deployment.microservices(name).mkString(","))
    }
    print(s"Took ${deployment.deploymentCompletionTime - deployment.startTime} ms")
  }

  private def printProblem(exception: Throwable) = exception.printStackTrace()

  def main(args: Array[String]): Unit = {
    val inputJson = args(0)
    val deploymentService: MicroserviceDeploymentService = new BasicMicroserviceDeploymentService(new NoOpDeployer())
    deploymentService.deploy(inputJson) match {
      case Success(value) => printResult(value)
      case Failure(exception) => printProblem(exception)
    }
  }

}