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

  lazy val commonSettings =
    Defaults.defaultSettings ++
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
      "2.10.4"
    , "2.11.2"
    )
  , scalaVersion := crossScalaVersions.value.head

  , scalacOptions := Seq(
      "-deprecation"
    , "-encoding", "UTF-8"
    , "-feature"
    , "-language:existentials"
    , "-language:implicitConversions"
    , "-language:postfixOps"
    , "-language:reflectiveCalls"
    , "-optimise"
    , "-unchecked"
    , "-Xcheckinit"
    , "-Xlint"
    , "-Xmax-classfile-name", "72"
    , "-Xno-forwarders"
    , "-Xverify"
    , "-Yclosure-elim"
    , "-Ydead-code"
    , "-Yinline-warnings"
    , "-Yinline"
    , "-Yrepl-sync"
    , "-Ywarn-adapted-args"
    , "-Ywarn-dead-code"
    , "-Ywarn-inaccessible"
    , "-Ywarn-nullary-override"
    , "-Ywarn-nullary-unit"
    , "-Ywarn-numeric-widen"
    )

  , unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil
  , unmanagedSourceDirectories in Test    := (scalaSource in Test).value :: Nil

  , resolvers := Seq(ElementNexus, ElementReleases, ElementSnapshots)
  , externalResolvers := Resolver.withDefaultResolvers(resolvers.value, mavenCentral = false)

  , publishTo := Some(
      if (version.value endsWith "-SNAPSHOT") ElementSnapshots else ElementReleases
    )
  , publishArtifact in (Compile, packageDoc) := false
  , credentials += Credentials(Path.userHome / ".config" / "etb" / "nexus.config")

  , EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE16)
  )
}

object Dependencies {
  lazy val commonsCodec = "commons-codec" % "commons-codec" % "1.9"
  lazy val dispatch = "net.databinder.dispatch" %% "dispatch-core" % "0.11.2"

  lazy val scalaTime = "com.github.nscala-time" %% "nscala-time" % "1.4.0"

  lazy val scalaIo = Def.setting {
    "com.github.scala-incubator.io" %% "scala-io-file" % (
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, scalaMajor)) if scalaMajor == 11 => "0.4.3-1"
        case _ => "0.4.3"
      }
    )
  }

  lazy val scalaXml = Def.setting {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, scalaMajor)) if scalaMajor >= 11 =>
        Seq("org.scala-lang.modules" %% "scala-xml" % "1.0.2")
      case _ =>
        Nil
    }
  }

  lazy val mimeTypes = "hr.element.onebyseven.common" % "mimetypes" % "2012-02-12"
  lazy val liftWebkit = "net.liftweb" %% "lift-webkit" % "2.5.1"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "2.2.2" % "test"
}

object EtbBuild extends Build {
  import Dependencies._
  import BuildSettings._

  lazy val util = Project(
    "util"
  , file("util")
  , settings = commonSettings ++ Seq(
      name    := "Etb-Util"
    , version := "0.2.22"
    , initialCommands := "import hr.element.etb._"
    , libraryDependencies ++= Seq(
        scalaTime
      , scalaIo.value
      ) ++ scalaXml.value
    )
  )

  lazy val lift = Project(
    "lift"
  , file("lift")
  , settings = commonSettings ++ Seq(
      name    := "Etb-Lift"
    , version := "0.1.7"
    , libraryDependencies ++= Seq(
        liftWebkit % "provided"
      , mimeTypes
      )
    )
  )

  lazy val img = Project(
    "img"
  , file("img")
  , settings = commonSettings ++ Seq(
      name    := "Etb-Img"
    , version := "0.2.3"
    )
  )
}
