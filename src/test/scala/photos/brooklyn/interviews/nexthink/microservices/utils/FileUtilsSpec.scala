package photos.brooklyn.interviews.nexthink.microservices.utils

import org.json4s.DefaultFormats
import org.scalatest.Inside.inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must
import photos.brooklyn.interviews.nexthink.microservices.utils.FileUtils._

class FileUtilsSpec extends AnyFlatSpec with must.Matchers {
  import Helpers._
  implicit private val formats = DefaultFormats

  behavior of "readObjectFromFile"

  it should "Read into case class correctly when given correct json" in {
    val res = readObjectFromFile[Sample]("sample.json").get
    inside(res) {
      case Sample(x) => x must be(1)
    }
  }

  private object Helpers{
    case class Sample(x: Int)
  }
}
