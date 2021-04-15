package photos.brooklyn.interviews.nexthink.microservices.utils

import com.typesafe.scalalogging.Logger

trait AppLogger {

  protected lazy val logger = Logger(getClass)

}
