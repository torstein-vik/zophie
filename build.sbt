scalaVersion := "2.12.3"

name := "zophie"

parallelExecution in Test := false

// Dependencies
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.4"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.6.0-M1"