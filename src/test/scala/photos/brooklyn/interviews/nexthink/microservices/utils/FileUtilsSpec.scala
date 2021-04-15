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

  it should "read into case class correctly when given correct json" in {
    val res = readObjectFromFile[Sample]("sample.json").get
    inside(res) {
      case Sample(x) => x must be(1)
    }
  }

  it should "fail when given correct json content" in {
    an[IllegalArgumentException] must be thrownBy {
      readObjectFromFile[Sample]("sample_bad.json").get
    }
  }

  it should "fail when given incorrect path" in {
    an[IllegalArgumentException] must be thrownBy {
      readObjectFromFile[Sample]("samplex.json").get
    }
  }

  private object Helpers {
    case class Sample(x: Int)
  }
}
