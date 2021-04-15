scalaVersion := "2.13.3"
name := "microservices"
organization := "photos.brooklyn"
version := "1.0"

val json4sNative = "org.json4s" %% "json4s-jackson" % "3.6.11"

// use Scaladex to find more
libraryDependencies += json4sNative
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.7"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.7" % Test