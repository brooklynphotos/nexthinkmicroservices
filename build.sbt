scalaVersion := "2.13.3"
name := "microservices"
organization := "photos.brooklyn"
version := "1.0"

val json4sNative = "org.json4s" %% "json4s-jackson" % "3.6.11"

lazy val akkaHttpVersion = "10.2.4"
lazy val akkaVersion    = "2.6.14"

// use Scaladex to find more
libraryDependencies += json4sNative
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.7"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.7" % Test

// akka http stuff
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
  "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
  "ch.qos.logback"    % "logback-classic"           % "1.2.3",
  "ch.megard"         %% "akka-http-cors"           % "1.1.1",

  "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
)
