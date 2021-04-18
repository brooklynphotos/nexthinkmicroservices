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
      name => {
        println(name)
        println(s"""\t${deployment.microservices(name).map(_.id).mkString("\n\t")}""")
      }
    }
    print(s"Took ${deployment.deploymentCompletionTime - deployment.startTime} ms")
  }

  private def printProblem(exception: Throwable) = exception.printStackTrace()

  def main(args: Array[String]): Unit = {
    val command = args(0)
    val inputJson = args(1)
    val deploymentService: MicroserviceDeploymentService = new BasicMicroserviceDeploymentService(new NoOpDeployer())
    commands(command)(deploymentService, inputJson)
  }

  def deploy(deploymentService: MicroserviceDeploymentService, inputJson: String) =
    deploymentService.deploy(inputJson) match {
      case Success(value) => printResult(value)
      case Failure(exception) => printProblem(exception)
    }

  def cyclic(deploymentService: MicroserviceDeploymentService, inputJson: String) =
    deploymentService.isCyclic(inputJson) match {
      case Success(value) => println(value)
      case Failure(exception) => printProblem(exception)
    }

  def health(deploymentService: MicroserviceDeploymentService, inputJson: String) = {
    deploymentService.deploy(inputJson) match {
      case Success(dep) => println(deploymentService.isHealthy(dep))
      case Failure(exception) => printProblem(exception)
    }
  }

  private val commands = Map(
    "deploy" -> deploy _,
    "cyclic" -> cyclic _,
    "health" -> health _,
  )
}