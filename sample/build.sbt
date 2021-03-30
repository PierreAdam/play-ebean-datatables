name := """test-project"""
organization := "com.jackson42.play.ebeandatables.test"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

// Only for testing purpose. Do not use a snapshot version on a final project.
resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"
)

libraryDependencies ++= Seq(
  guice,
  jdbc,
  "com.h2database" % "h2" % "1.4.200",

  "com.jackson42.play" % "play-ebean-datatables" % "21.04-SNAPSHOT",
  "io.ebean" % "ebean" % "12.3.6",

  "com.github.javafaker" % "javafaker" % "1.0.2",

  // To provide an implementation of JAXB-API, which is required by Ebean.
  "javax.xml.bind" % "jaxb-api" % "2.3.1",
  "javax.activation" % "activation" % "1.1.1",
  "org.glassfish.jaxb" % "jaxb-runtime" % "2.3.2"
)

dependencyOverrides ++= Seq(
  "io.ebean" % "ebean" % "12.3.6"
)

scalaVersion := "2.13.3"

libraryDependencies += guice
