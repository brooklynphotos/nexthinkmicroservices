package photos.brooklyn.interviews.nexthink.microservices.model

import org.json4s.DefaultFormats
import photos.brooklyn.interviews.nexthink.microservices.DeploymentConfiguration
import photos.brooklyn.interviews.nexthink.microservices.utils.FileUtils

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

/**
 * describes the deployment description file
 *
 * @param name
 * @param entryPoint
 * @param replicas
 * @param dependencies
 */
case class MicroserviceConfiguration(name: String, entryPoint: Boolean, replicas: Int, dependencies: Set[String])

object MicroserviceConfiguration{
  implicit private val formats = DefaultFormats

  /**
   * determines if the configuration is valid, for example, that no microservice is repeated
   * TODO should also ensure there is one entry point and only one
   * @param depConfig
   * @return
   */
  def isValid(microserviceConfigs: DeploymentConfiguration): Boolean = isValid(microserviceConfigs, Set())

  @tailrec
  private def isValid(microserviceConfigs: DeploymentConfiguration, existingNames: Set[String]): Boolean = microserviceConfigs match {
    case head::rest => if(existingNames(head.name)) false else isValid(rest, existingNames + head.name)
    case Nil => true
  }

  /**
   * constructs a configuration based on the given config file
   * TODO need to figure out how to assign false to entryPoint attribute when missing
   * @param configFile
   * @return
   */
  def buildConfiguration(configFile: String): Try[DeploymentConfiguration] = {
    FileUtils.readObjectFromFile[DeploymentConfiguration](configFile) flatMap { depConfig =>
      // make sure it is valid
      if(isValid(depConfig)) Success(depConfig) else Failure(new IllegalArgumentException("Deployment configuration is illegal"))
    }
  }
}