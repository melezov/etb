import sbt._
import Keys._

object Repositories {
  val ElementNexus     = "Element Nexus"     at "http://repo.element.hr/nexus/content/groups/public"
  val ElementReleases  = "Element Releases"  at "http://repo.element.hr/nexus/content/repositories/releases/"
  val ElementSnapshots = "Element Snapshots" at "http://repo.element.hr/nexus/content/repositories/snapshots/"
}

object BuildSettings {
  import Repositories._

  //Dependency graph plugin
  import net.virtualvoid.sbt.graph.Plugin._

  val scala2_8 = Seq(
    "-unchecked"
  , "-deprecation"
  , "-optimise"
  , "-encoding", "UTF-8"
  , "-explaintypes"
  , "-Xcheckinit"
  , "-Xfatal-warnings"
  , "-Yclosure-elim"
  , "-Ydead-code"
  , "-Yinline"
  )

  val scala2_9 = Seq(
    "-Xmax-classfile-name", "72"
  )

  val scala2_9_1 = Seq(
    "-Yrepl-sync"
  , "-Xlint"
  , "-Xverify"
  , "-Ywarn-all"
  )

  val scala2_10 = Seq(
    "-feature"
  , "-language:postfixOps"
  , "-language:implicitConversions"
  , "-language:existentials"
  )

  lazy val commonSettings = Defaults.defaultSettings ++
                            graphSettings ++ Seq(
    organization := "hr.element.etb"

  , javaHome := sys.env.get("JDK16_HOME").map(file(_))
  , javacOptions := Seq(
      "-deprecation"
    , "-encoding", "UTF-8"
    , "-Xlint:unchecked"
    , "-source", "1.6"
    , "-target", "1.6"
    )

  , scalaVersion <<= crossScalaVersions(_.last)

  , scalacOptions <<= scalaVersion map ( sV => scala2_8 ++ (sV match {
        case x if (x startsWith "2.10.")                => scala2_9 ++ scala2_9_1 ++ scala2_10
        case x if (x startsWith "2.9.") && x >= "2.9.1" => scala2_9 ++ scala2_9_1
        case x if (x startsWith "2.9.")                 => scala2_9
        case _ => Nil
      } )
    )

  , unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(_ :: Nil)
  , unmanagedSourceDirectories in Test    <<= (scalaSource in Test   )(_ :: Nil)

  , resolvers := Seq(ElementNexus, ElementReleases, ElementSnapshots)
  , externalResolvers <<= resolvers map { r =>
      Resolver.withDefaultResolvers(r, mavenCentral = false)
    }

  , publishTo <<= version { version => Some(
      if (version endsWith "SNAPSHOT") ElementSnapshots else ElementReleases
    )}
  , publishArtifact in (Compile, packageDoc) := false

  , credentials += Credentials(Path.userHome / ".config" / "etb" / "nexus.config")
  )

  lazy val bsUtil = commonSettings ++ Seq(
    name    := "Etb-Util"
  , version := "0.2.19"
  , initialCommands := "import hr.element.etb.Pimps._"
  )

  lazy val bsLift = commonSettings ++ Seq(
    name    := "Etb-Lift"
  , version := "0.1.4"
  )

  lazy val bsImg = commonSettings ++ Seq(
    name    := "Etb-Img"
  , version := "0.2.1"
  )
}

object Dependencies {
  lazy val commonsCodec = "commons-codec" % "commons-codec" % "1.7"
  lazy val dispatch = "net.databinder" %% "dispatch-http" % "0.8.9"

  lazy val mimeTypes = "hr.element.onebyseven.common" % "mimetypes" % "2012-02-12"

  lazy val liftWebkit = "net.liftweb" %% "lift-webkit" % "2.5-RC5"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "2.0.M5b"

  lazy val depsUtil = Seq(
    commonsCodec
  , dispatch % "provided"
  , scalaTest % "test"
  )

  lazy val depsLift = Seq(
    liftWebkit % "provided"
  , mimeTypes
//  , scalaTest % "test"
  )

  lazy val depsImg = Seq(
//    scalaTest % "test"
  )
}

object EtbBuild extends Build {
  import Dependencies._
  import BuildSettings._

  lazy val util = Project(
    "util"
  , file("util")
  , settings = bsUtil ++ Seq(
      libraryDependencies ++= depsUtil
    , crossScalaVersions := Seq(
        "2.9.0", "2.9.0-1"
      , "2.9.1", "2.9.1-1"
      , "2.9.2"
      , "2.9.3"
      , "2.10.1"
      )
    )
  )

  lazy val lift = Project(
    "lift"
  , file("lift")
  , settings = bsLift ++ Seq(
      libraryDependencies ++= depsLift
    , crossScalaVersions := Seq(
        "2.9.1", "2.9.1-1"
      , "2.9.2"
      //, "2.9.3"
      , "2.10.1"
      )
    , libraryDependencies ++= depsLift
    )
  )

  lazy val img = Project(
    "img"
  , file("img")
  , settings = bsImg ++ Seq(
      libraryDependencies ++= depsImg
    , crossScalaVersions := Seq(
        "2.8.0", "2.8.1", "2.8.2"
      , "2.9.0", "2.9.0-1", "2.9.1", "2.9.1-1"
      , "2.9.2"
      , "2.9.3"
      , "2.10.1"
      )
    )
  )
}
