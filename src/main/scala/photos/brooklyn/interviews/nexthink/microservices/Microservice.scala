package photos.brooklyn.interviews.nexthink.microservices

trait Microservice {
  def id: Int

  def name: String

  def isEntryPoint: Boolean

  def dependencies: Set[Microservice]

  def isHealthy: Boolean
}