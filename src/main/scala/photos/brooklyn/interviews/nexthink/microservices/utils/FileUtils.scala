package photos.brooklyn.interviews.nexthink.microservices.utils

import java.io.{File, FileNotFoundException}

import org.json4s._
import org.json4s.jackson.Serialization.read

import scala.io.Source
import scala.reflect.Manifest
import scala.util.{Failure, Try, Using}

/**
 * General file utils
 */
object FileUtils extends AppLogger {

  /**
   * saves content of file into a string
   *
   * @param file
   * @return
   */
  def fileToString(file: String): Try[String] = fileToString(new File(file)) recoverWith {
    case fe: FileNotFoundException =>
      logger.info(s"Failed to read from file $file, now trying resource", fe)
      Using(Source.fromResource(file)) { src =>
        src.mkString
      }
    case t => Failure(t)
  }

  /**
   * saves contents of a file handler to a string
   *
   * @param file
   * @return
   */
  def fileToString(file: File): Try[String] = Using(Source.fromFile(file, "UTF-8")) { src =>
    src.mkString
  }

  /**
   * reads contents of a json file into the case class of type A
   *
   * @param jsonFile
   * @param formats
   * @param mf
   * @tparam A type that this json corresponds to
   * @return
   */
  def readObjectFromFile[A](jsonFile: String)(implicit formats: Formats, mf: Manifest[A]): Try[A] =
    fileToString(jsonFile).recover(_=>jsonFile).map(read[A]) recoverWith {
      case t => Failure(new IllegalArgumentException(s"JsonFile input not good: $jsonFile", t))
    }

}
