package photos.brooklyn.interviews.nexthink.microservices

trait Microservice {
  def id: Int

  def name: String

  def isEntryPoint: Boolean

  def dependencies: List[Microservice]

  def isHealthy: Boolean
}