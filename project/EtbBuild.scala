import sbt._
import Keys._

object Resolvers {
  val eleRepo = "Element Repo" at "http://maven.element.hr/nexus/content/groups/public"

  val res = Seq(
    eleRepo
  )
}

object BuildSettings {
  import Resolvers._

  val commonSettings = Defaults.defaultSettings ++ Seq(
    organization := "hr.element.etb",
    crossScalaVersions := Seq("2.9.1", "2.9.0-1", "2.9.0"),
    scalaVersion <<= (crossScalaVersions) { versions => versions.head },
    scalacOptions := Seq("-unchecked", "-deprecation", "-Yrepl-sync", "-encoding", "UTF-8", "-optimise"),
    javacOptions := Seq("-deprecation", "-encoding", "UTF-8", "-source", "1.5", "-target", "1.5"),
    compileOrder := CompileOrder.JavaThenScala,
    publishArtifact in (Compile, packageDoc) := false,
    resolvers := res,
    externalResolvers <<= resolvers map { rs =>
      Resolver.withDefaultResolvers(rs, mavenCentral = false, scalaTools = false)
    },
    publishTo := Some("Element Nexus" at "http://maven.element.hr/nexus/content/repositories/releases/"),
    credentials += Credentials(Path.userHome / ".publish" / "element.credentials")
  )

  val bsIORC = commonSettings ++ Seq(
    name    := "IORC",
    version := "0.0.21"
  )

  val bsImg = commonSettings ++ Seq(
    name    := "Img",
    version := "0.0.4"
  )

  val bsEtb = commonSettings ++ Seq(
    name    := "Etb",
    version := "0.1.22"
  )
}

object Dependencies {
  val iorc = "hr.element.etb" %% "iorc" % "0.0.21"
  val img = "hr.element.etb" %% "img" % "0.0.4"

  val jodaTime = "joda-time" % "joda-time" % "1.6.2"
  val scalaTime = "org.scala-tools.time" %% "time" % "0.5"
  val commonsIo = "commons-io" % "commons-io" % "2.1" % "test"

  val scalaTest = "org.scalatest" %% "scalatest" % "1.6.1" % "test"

  val depsIORC = Seq(
    scalaTest
  )

  val depsImg = Seq(
    scalaTest
  )

  val depsEtb = Seq(
    iorc,
    img,
    scalaTime,
    scalaTest
  )
}

object EtbxBuild extends Build {
  import Dependencies._
  import BuildSettings._

  lazy val iorc = Project(
    "iorc",
    file("iorc"),
    settings = bsIORC ++ Seq(
      libraryDependencies := depsIORC
    )
  )

  lazy val img = Project(
    "img",
    file("img"),
    settings = bsImg ++ Seq(
      libraryDependencies := depsImg
    )
  )

  lazy val etb = Project(
    "etb",
    file("etb"),
    settings = bsEtb ++ Seq(
      libraryDependencies := depsEtb
    )
  )
}
