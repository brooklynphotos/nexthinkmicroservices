package photos.brooklyn.interviews.nexthink.microservices.model

import org.json4s.DefaultFormats
import photos.brooklyn.interviews.nexthink.microservices.DeploymentConfiguration
import photos.brooklyn.interviews.nexthink.microservices.utils.FileUtils

import scala.util.Try

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
   * constructs a configuration based on the given config file
   * @param configFile
   * @return
   */
  def buildConfiguration(configFile: String): Try[DeploymentConfiguration] = FileUtils.readObjectFromFile[DeploymentConfiguration](configFile)
}