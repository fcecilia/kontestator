name := """kontestator"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "com.typesafe.akka" %% "akka-persistence-experimental" % "2.3.9",
  "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23"
)
