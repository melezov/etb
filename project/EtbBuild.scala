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
    organization := "hr.element.etb"
  , crossScalaVersions := Seq("2.9.2", "2.9.1-1", "2.9.1", "2.9.0-1", "2.9.0")
  , scalaVersion <<= (crossScalaVersions) { versions => versions.head }
  , scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "UTF-8", "-optimise")
  , unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)( _ :: Nil)
  , unmanagedSourceDirectories in Test    <<= (scalaSource in Test   )( _ :: Nil)
  , publishArtifact in (Compile, packageDoc) := false
  , resolvers := res
  , externalResolvers <<= resolvers map { rs =>
      Resolver.withDefaultResolvers(rs, mavenCentral = false, scalaTools = false)
    }
  , publishTo := Some("Element Releases" at "http://maven.element.hr/nexus/content/repositories/releases/")
  , credentials += Credentials(Path.userHome / ".publish" / "element.credentials")
  )

  val bsUtil = commonSettings ++ Seq(
    name    := "Etb-Util"
  , version := "0.2.16-P0"
  , initialCommands := "import hr.element.etb.Pimps._"
  )

  val bsLift = commonSettings ++ Seq(
    name    := "Etb-Lift"
  , version := "0.0.21-P0"
  )

  val bsImg = commonSettings ++ Seq(
    name    := "Etb-Img"
  , version := "0.1.0"
  )
}

object Dependencies {
  //liftweb
  val liftWebkit = "net.liftweb" %% "lift-webkit" % "2.5-M1" % "provided"

  val commonsCodec = "commons-codec" % "commons-codec" % "1.6"
  val dispatch = "net.databinder" %% "dispatch-http" % "0.8.8" % "provided"

  val mimeTypes = "hr.element.onebyseven.common" % "mimetypes" % "2012-02-12"

  //test
  val scalaTest = "org.scalatest" %% "scalatest" % "2.0.M4" % "test"

  val depsUtil = Seq(
    commonsCodec
  , dispatch
    //test
  , scalaTest
  )

  val depsLift = Seq(
    liftWebkit
  , mimeTypes
    //test
  , scalaTest
  )

  val depsImg = Seq(
    //test
    scalaTest
  )
}

object EtbBuild extends Build {
  import Dependencies._
  import BuildSettings._

  lazy val util = Project(
    "util"
  , file("util")
  , settings = bsUtil ++ Seq(
      libraryDependencies := depsUtil
    )
  )

  lazy val lift = Project(
    "lift"
  , file("lift")
  , settings = bsLift ++ Seq(
      crossScalaVersions := Seq("2.9.2", "2.9.1-1", "2.9.1")
    , libraryDependencies := depsLift
    )
  )

  lazy val img = Project(
    "img"
  , file("img")
  , settings = bsImg ++ Seq(
      libraryDependencies := depsImg
    )
  )
}
