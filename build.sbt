name := "utilities"
organization := "com.tutran"
version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

crossPaths := false

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.4",
  "org.apache.commons" % "commons-csv" % "1.2",
  "com.google.code.gson" % "gson" % "2.4",
  "org.specs2" %% "specs2-core" % "3.6.5" % "test",
  "org.specs2" %% "specs2-mock" % "3.6.5" % "test",
  "org.mockito" % "mockito-all" % "1.9.5" % "test"

)

scalacOptions in Test ++= Seq("-Yrangepos")
