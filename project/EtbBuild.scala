import sbt._
import Keys._

object Repositories {
  val ElementNexus     = "Element Nexus"     at "http://repo.element.hr/nexus/content/groups/public"
  val ElementReleases  = "Element Releases"  at "http://repo.element.hr/nexus/content/repositories/releases/"
  val ElementSnapshots = "Element Snapshots" at "http://repo.element.hr/nexus/content/repositories/snapshots/"
}

object BuildSettings {
  import Repositories._

  //Eclipse plugin
  import com.typesafe.sbteclipse.plugin.EclipsePlugin._

  //Dependency graph plugin
  import net.virtualvoid.sbt.graph.Plugin._

  val scala2_8 = Seq(
    "-unchecked"
  , "-deprecation"
  , "-optimise"
  , "-encoding", "UTF-8"
//  , "-explaintypes"
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
                            eclipseSettings ++
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

  , crossScalaVersions := Seq(
      "2.9.0", "2.9.0-1", "2.9.1", "2.9.1-1", "2.9.2", "2.9.3"
    , "2.10.3-RC3"
    )
  , scalaVersion := crossScalaVersions.value.find(_ startsWith "2.10").get

  , scalacOptions := scala2_8 ++ (scalaVersion.value match {
        case x if (x startsWith "2.10.")                => scala2_9 ++ scala2_9_1 ++ scala2_10
        case x if (x startsWith "2.9.") && x >= "2.9.1" => scala2_9 ++ scala2_9_1
        case x if (x startsWith "2.9.")                 => scala2_9
        case _ => Nil
      } )

  , unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil
  , unmanagedSourceDirectories in Test    := (scalaSource in Test).value :: Nil

  , resolvers := Seq(ElementNexus, ElementReleases, ElementSnapshots)
  , externalResolvers := Resolver.withDefaultResolvers(resolvers.value, mavenCentral = false)

  , publishTo := Some(
      if (version.value endsWith "SNAPSHOT") ElementSnapshots else ElementReleases
    )
  , publishArtifact in (Compile, packageDoc) := false
  , credentials += Credentials(Path.userHome / ".config" / "etb" / "nexus.config")

  , EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE16)
  )

  lazy val bsUtil = commonSettings ++ Seq(
    name    := "Etb-Util"
  , version := "0.2.20"
  , initialCommands := "import hr.element.etb._"
  )

  lazy val bsLift = commonSettings ++ Seq(
    name    := "Etb-Lift"
  , version := "0.1.5"
  )

  lazy val bsImg = commonSettings ++ Seq(
    name    := "Etb-Img"
  , version := "0.2.1"
  )
}

object Dependencies {
  lazy val commonsCodec = "commons-codec" % "commons-codec" % "1.8"
  lazy val dispatch = "net.databinder" %% "dispatch-http" % "0.8.9"

  lazy val scalaTime = "com.github.nscala-time" % "nscala-time" % "0.6.0" cross CrossVersion.binaryMapped {
    case x if x startsWith "2.9.0" => "2.9.1"
    case x if x startsWith "2.9.1" => "2.9.1"
    case x => x
  }

  lazy val scalaIo = Def.setting {
    scalaVersion.value match {
      case x if x startsWith "2.9.0" => "com.github.scala-incubator.io" % "scala-io-file_2.9.1" % "0.4.1-seq"
      case x if x startsWith "2.9.1" => "com.github.scala-incubator.io" % "scala-io-file_2.9.1" % "0.4.1-seq"
      case x if x startsWith "2.9.3" => "com.github.scala-incubator.io" % "scala-io-file_2.9.2" % "0.4.1-seq"
      case x if x startsWith "2.9."  => "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.1-seq"
      case x                         => "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.2"
    }
  }

  lazy val mimeTypes = "hr.element.onebyseven.common" % "mimetypes" % "2012-02-12"
  lazy val scalaUUID = "io.jvm" %% "scala-uuid" % "0.1.1"

  lazy val liftWebkit = "net.liftweb" % "lift-webkit" % "2.5.1" cross CrossVersion.binaryMapped {
    case x if x startsWith "2.9.0" => "2.9.1"
    case x if x startsWith "2.9.3" => "2.9.2"
    case x => x
  }

  lazy val scalaTest = Def.setting {
    "org.scalatest" %% "scalatest" % (scalaVersion.value match {
      case x if x startsWith "2.8." => "1.8"
      case x if x startsWith "2.9." => "2.0.M5b"
      case x if x startsWith "2.10." => "2.0.M8"
      case x if x startsWith "2.11." => "2.0.M7"
    } )
  }
}

object EtbBuild extends Build {
  import Dependencies._
  import BuildSettings._

  lazy val util = Project(
    "util"
  , file("util")
  , settings = bsUtil ++ Seq(
      libraryDependencies ++= Seq(
        commonsCodec
      , scalaUUID
      , scalaTime
      , scalaIo.value
      , dispatch % "provided"
      , scalaTest.value % "test"
      )
    )
  )

  lazy val lift = Project(
    "lift"
  , file("lift")
  , settings = bsLift ++ Seq(
      libraryDependencies ++= Seq(
        liftWebkit % "provided"
      , mimeTypes
      , scalaTest.value % "test"
      )
    )
  )

  lazy val img = Project(
    "img"
  , file("img")
  , settings = bsImg ++ Seq(
      libraryDependencies ++= Seq(
        scalaTest.value % "test"
      )
    )
  )
}
