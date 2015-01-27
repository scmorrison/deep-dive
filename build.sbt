name := """Deep Dive"""

version := "0.0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  // WebJars pull in client-side web libraries
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.webjars" % "bootstrap" % "3.3.1",
  "org.webjars" % "angularjs" % "1.3.10",
  "org.webjars" % "requirejs" % "2.1.15",
  "org.webjars" % "metisMenu" % "1.1.2",
  "org.webjars" % "morrisjs" % "0.5.1",
  "org.webjars" % "raphaeljs" % "2.1.2-1",
  "org.scalatestplus" %% "play" % "1.2.0" % "test"
)
