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
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "UTF-8", "-optimise"),
    javacOptions := Seq("-deprecation", "-encoding", "UTF-8", "-source", "1.5", "-target", "1.5"),
    compileOrder := CompileOrder.JavaThenScala,
    publishArtifact in (Compile, packageDoc) := false,
    resolvers := res,
    externalResolvers <<= resolvers map { rs =>
      Resolver.withDefaultResolvers(rs, mavenCentral = false, scalaTools = false)
    },
    publishTo := Some("Element Releases" at "http://maven.element.hr/nexus/content/repositories/releases/"),
    credentials += Credentials(Path.userHome / ".publish" / "element.credentials")
  ) ++ Format.settings

  val bsUtil = commonSettings ++ Seq(
    name    := "Etb-Util",
    version := "0.2.3"
  )

  val bsIORC = commonSettings ++ Seq(
    name    := "Etb-IORC",
    version := "0.1.0"
  )

  val bsImg = commonSettings ++ Seq(
    name    := "Etb-Img",
    version := "0.1.0"
  )
}

object Dependencies {
  val scalaTest = "org.scalatest" %% "scalatest" % "1.6.1" % "test"

  val depsUtil = Seq(
    //test
    scalaTest
  )

  val depsIORC = Seq(
    //test
    scalaTest
  )

  val depsImg = Seq(
    //test
    scalaTest
  )
}

object EtbBuild extends Build {
  import Dependencies._
  import BuildSettings._

  lazy val etb = Project(
    "etb",
    file(".")
  ) aggregate(
    util,
    iorc,
    img
  )

  lazy val util = Project(
    "util",
    file("util"),
    settings = bsUtil ++ Seq(
      libraryDependencies := depsUtil
    )
  )

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
}

object Format {
  // Scalariform plugin
  import com.typesafe.sbtscalariform._
  import ScalariformPlugin._
  import scalariform.formatter.preferences._

  ScalariformKeys.preferences := FormattingPreferences()
    .setPreference(AlignParameters, false)
    .setPreference(AlignSingleLineCaseStatements, false)
    .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 40)
    .setPreference(CompactControlReadability, true)
    .setPreference(CompactStringConcatenation, false)
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(FormatXml, false)
    .setPreference(IndentLocalDefs, false)
    .setPreference(IndentPackageBlocks, false)
    .setPreference(IndentSpaces, 2)
    .setPreference(IndentWithTabs, false)
    .setPreference(MultilineScaladocCommentsStartOnFirstLine, true)
    .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)
    .setPreference(PreserveDanglingCloseParenthesis, false)
    .setPreference(PreserveSpaceBeforeArguments, false)
    .setPreference(RewriteArrowSymbols, false)
    .setPreference(SpaceBeforeColon, false)
    .setPreference(SpaceInsideBrackets, false)
    .setPreference(SpaceInsideParentheses, false)
    .setPreference(SpacesWithinPatternBinders, true)

  lazy val settings =
    ScalariformPlugin.scalariformSettings
}
