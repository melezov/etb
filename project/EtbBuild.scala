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
    name    := "Etb-Util",
    version := "0.2.9"
  )

  val bsLift = commonSettings ++ Seq(
    name    := "Etb-Lift",
    version := "0.0.20"
  )

  val bsImg = commonSettings ++ Seq(
    name    := "Etb-Img",
    version := "0.1.0"
  )
}

object Dependencies {
  //liftweb
  val liftVersion = "2.4"
  val liftWebkit = "net.liftweb" % "lift-webkit_2.9.1" % liftVersion
  
  val commonsCodec = "commons-codec" % "commons-codec" % "1.6"
  val dispatch = "net.databinder" %% "dispatch-http" % "0.8.8" % "compile"

  val mimeTypes = "hr.element.onebyseven.common" % "mimetypes" % "2012-02-12"
  
  //test
  val scalaTest = "org.scalatest" %% "scalatest" % "1.7.1" % "test"

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
    "util",
    file("util"),
    settings = bsUtil ++ Seq(
      libraryDependencies := depsUtil
    )
  )

  lazy val lift = Project(
    "lift",
    file("lift"),
    settings = bsLift ++ Seq(
      libraryDependencies := depsLift
    )
  )

  lazy val img = Project(
    "img",
    file("img"),
    settings = bsImg ++ Seq(
      libraryDependencies := depsImg
    )
  )
}
