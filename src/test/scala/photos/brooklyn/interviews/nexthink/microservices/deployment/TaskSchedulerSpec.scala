package photos.brooklyn.interviews.nexthink.microservices.deployment

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must
import photos.brooklyn.interviews.nexthink.microservices.deployment.TaskScheduler._
import photos.brooklyn.interviews.nexthink.microservices.model.MicroserviceConfiguration

import scala.util.Failure

class TaskSchedulerSpec extends AnyFlatSpec with must.Matchers {

  behavior of "findEntryPoint"

  it should "find the entry point given a valid config" in {
    val entry = MicroserviceConfiguration("A", true, 2, Set("B"))
    val depConfig = List(
      entry,
      MicroserviceConfiguration("B", false, 2, Set("C")),
      MicroserviceConfiguration("C", false, 2, Set.empty)
    )
    findEntryPoint(depConfig) mustBe entry
  }

  it should "throw exception given no entry point" in {
    val depConfig = List(
      MicroserviceConfiguration("A", false, 2, Set("B")),
      MicroserviceConfiguration("B", false, 2, Set("C")),
      MicroserviceConfiguration("C", false, 2, Set.empty)
    )
    an[IllegalArgumentException] must be thrownBy {
      findEntryPoint(depConfig)
    }
  }

  behavior of "createOrderedTasks"
  it should "return ordered tasks when given DAG" in {
    val entry = MicroserviceConfiguration("A", true, 2, Set("B", "C"))
    val B = MicroserviceConfiguration("B", false, 2, Set("D"))
    val C = MicroserviceConfiguration("C", false, 2, Set("D"))
    val D = MicroserviceConfiguration("D", false, 2, Set.empty)
    val taskList = createOrderedTasks(List(B, entry, C, D)).get
    taskList.head mustBe entry
    taskList.last mustBe D
    taskList.size mustBe 4
  }

  it should "throw exception when given a loop" in {
    val entry = MicroserviceConfiguration("A", true, 2, Set("B"))
    val B = MicroserviceConfiguration("B", false, 2, Set("C"))
    val C = MicroserviceConfiguration("C", false, 2, Set("D"))
    val D = MicroserviceConfiguration("D", false, 2, Set("A"))
    createOrderedTasks(List(B, entry, C, D)) match {
      case Failure(caught: CycleDetectedException) => caught.visited mustBe "A"
      case _ => fail("Did not get the exception expected")
    }
  }

  behavior of "dfs with DAG"
  it should "return ordered tasks when given diamond DAG" in {
    val entry = MicroserviceConfiguration("A", true, 2, Set("B", "C"))
    val B = MicroserviceConfiguration("B", false, 2, Set("D"))
    val C = MicroserviceConfiguration("C", false, 2, Set("D"))
    val D = MicroserviceConfiguration("D", false, 2, Set.empty)
    val configMap = Map(
      "A" -> entry,
      "B" -> B,
      "C" -> C,
      "D" -> D
    )
    val taskList = dfs(configMap, entry)
    taskList.topo.head mustBe entry
    taskList.topo.last mustBe D
    taskList.topo.size mustBe configMap.size
  }

  it should "return ordered tasks when given tree DAG" in {
    val entry = MicroserviceConfiguration("A", true, 2, Set("B", "C", "D"))
    val B = MicroserviceConfiguration("B", false, 2, Set.empty)
    val C = MicroserviceConfiguration("C", false, 2, Set.empty)
    val D = MicroserviceConfiguration("D", false, 2, Set.empty)
    val configMap = Map(
      "A" -> entry,
      "B" -> B,
      "C" -> C,
      "D" -> D
    )
    val taskList = dfs(configMap, entry)
    taskList.topo.head mustBe entry
    taskList.topo.size mustBe configMap.size
  }

  it should "return ordered tasks when given linked list DAG" in {
    val entry = MicroserviceConfiguration("A", true, 2, Set("B"))
    val B = MicroserviceConfiguration("B", false, 2, Set("C"))
    val C = MicroserviceConfiguration("C", false, 2, Set("D"))
    val D = MicroserviceConfiguration("D", false, 2, Set.empty)
    val configMap = Map(
      "A" -> entry,
      "B" -> B,
      "C" -> C,
      "D" -> D
    )
    val taskList = dfs(configMap, entry)
    taskList.topo.head mustBe entry
    taskList.topo.last mustBe D
    taskList.topo.size mustBe configMap.size
  }

  it should "return ordered tasks when given single element" in {
    val A = MicroserviceConfiguration("D", true, 2, Set.empty)
    val configMap = Map(
      "A" -> A
    )
    val taskList = dfs(configMap, A)
    taskList.topo.head mustBe A
    taskList.topo.size mustBe configMap.size
  }

  behavior of "dfs with Cyclic graph"
  it should "throw exception when given a loop" in {
    val entry = MicroserviceConfiguration("A", true, 2, Set("B"))
    val B = MicroserviceConfiguration("B", false, 2, Set("C"))
    val C = MicroserviceConfiguration("C", false, 2, Set("D"))
    val D = MicroserviceConfiguration("D", false, 2, Set("A"))
    val configMap = Map(
      "A" -> entry,
      "B" -> B,
      "C" -> C,
      "D" -> D
    )
    val caught = intercept[CycleDetectedException] {
      dfs(configMap, entry)
    }
    caught.visited mustBe "A"
  }

  it should "throw exception when given a hairpin" in {
    val entry = MicroserviceConfiguration("A", true, 2, Set("B"))
    val B = MicroserviceConfiguration("B", false, 2, Set("C"))
    val C = MicroserviceConfiguration("C", false, 2, Set("D"))
    val D = MicroserviceConfiguration("D", false, 2, Set("B"))
    val configMap = Map(
      "A" -> entry,
      "B" -> B,
      "C" -> C,
      "D" -> D
    )
    val caught = intercept[CycleDetectedException] {
      dfs(configMap, entry)
    }
    caught.visited mustBe "B"
  }

  it should "throw exception when given a double hairpin" in {
    val entry = MicroserviceConfiguration("A", true, 2, Set("B"))
    val B = MicroserviceConfiguration("B", false, 2, Set("C"))
    val C = MicroserviceConfiguration("C", false, 2, Set("D", "E"))
    val D = MicroserviceConfiguration("D", false, 2, Set("B"))
    val E = MicroserviceConfiguration("E", false, 2, Set("D"))
    val configMap = Map(
      "A" -> entry,
      "B" -> B,
      "C" -> C,
      "E" -> E,
      "D" -> D
    )
    val caught = intercept[CycleDetectedException] {
      dfs(configMap, entry)
    }
    caught.visited mustBe "B"
  }

}
