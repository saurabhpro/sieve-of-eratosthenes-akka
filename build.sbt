name := "SieveOfEratosthenes"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.11",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.11" % Test,
  "com.typesafe.akka" %% "akka-cluster" % "2.5.11",
  "com.typesafe.akka" %% "akka-cluster-metrics" % "2.5.11",
  "com.typesafe.akka" %% "akka-cluster-sharding" % "2.5.11",
  "com.typesafe.akka" %% "akka-cluster-tools" % "2.5.11",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)