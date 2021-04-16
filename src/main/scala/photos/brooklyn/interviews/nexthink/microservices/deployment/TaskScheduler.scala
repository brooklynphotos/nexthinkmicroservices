package photos.brooklyn.interviews.nexthink.microservices.deployment

import photos.brooklyn.interviews.nexthink.microservices.DeploymentConfiguration
import photos.brooklyn.interviews.nexthink.microservices.model.MicroserviceConfiguration

/**
 * general functionality for scheduling tasks from a graph
 */
object TaskScheduler {

  private[deployment] def dfs(configMap: Map[String, MicroserviceConfiguration], node: MicroserviceConfiguration, accumulator: Accumulator = Accumulator(), stackVisited: Set[String] = Set.empty): Accumulator = {
    val newDependencies = node.dependencies.filterNot(accumulator.visited(_))
    val stackVisitedAfterNode = stackVisited + node.name
    val dependencyCollected = newDependencies.foldLeft(accumulator)((accum, e) => {
      if (stackVisited(e)) throw CycleDetectedException(e)
      dfs(configMap, configMap(e), accum, stackVisitedAfterNode + e)
    })
    Accumulator(node :: dependencyCollected.topo, dependencyCollected.visited + node.name)
  }

  /**
   * creates a topological sorting of the deployment configurations
   *
   * @param deploymentConfig
   * @return
   */
  def createOrderedTasks(deploymentConfig: DeploymentConfiguration) = {
    val entryMicroservice = findEntryPoint(deploymentConfig)
    val configMap = deploymentConfig.map(c => c.name -> c).toMap
    // now that we have an entry point and a graph, we can build a topological ordering
    dfs(configMap, entryMicroservice).topo
  }

  /**
   * figures out which deployment is the entry point. Exception if there isn't one and only one
   *
   * @param deploymentConfig
   * @return
   */
  def findEntryPoint(deploymentConfig: DeploymentConfiguration) = deploymentConfig.filter(x => x.entryPoint) match {
    case x :: Nil => x
    case Nil => throw new IllegalArgumentException(s"Found no entry point in the deploy file $deploymentConfig")
    case moreThanOne => throw new IllegalArgumentException(s"Found ${moreThanOne.size} entry points! Can only have one")
  }

  /**
   * helper class DFS uses to accumulate its results
   *
   * @param topo
   * @param visited
   */
  private[deployment] case class Accumulator(topo: List[MicroserviceConfiguration], visited: Set[String])

  private[deployment] object Accumulator {
    def apply(): Accumulator = Accumulator(Nil, Set.empty)
  }

  case class CycleDetectedException(visited: String) extends Exception
}
