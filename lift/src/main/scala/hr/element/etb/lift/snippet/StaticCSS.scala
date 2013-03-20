package hr.element.etb.lift.snippet

import net.liftweb.http.S
import net.liftweb.util.Props
import net.liftweb.common.Full

import scala.xml._

trait StaticCSS {
  val root = "static"
  val section = "css"

  val extension = ".css"

  protected val defaultVersions: PartialFunction[String, String] =
    Map.empty

  protected def getNameWithVersion(name: String, version: Option[String]) =
    name + (
      version orElse (
        defaultVersions lift (name)
      ) map ("-"+) getOrElse ("")
    )

  protected val defaultMinified: PartialFunction[String, String] = {
    case "all" =>
      ".min"

    case "prod" if Props.productionMode =>
      ".min"
  }

  protected def getSuffix(min: Option[String]) = (
    defaultMinified lift
    min.getOrElse("prod")
    getOrElse ("")
  )

  def serveLink(
      name: String
    , version: Option[String]
    , media: Option[String]
    , min: Option[String]
    , suffix: String) = {

    val someMedia = media.getOrElse("screen")

    val filename =
      getNameWithVersion(name, version) + getSuffix(min)

    val path =
      if (name.startsWith("../")) {
        "/%s/%s%s" format (root, filename substring 3, suffix)
      } else {
        "/%s/%s/%s%s" format (root, section, filename, suffix)
      }

    <link href={ path } type="text/css" rel="stylesheet" media={ someMedia }/>
  }

  def comment(n: NodeSeq) = {
    val htmlLink = render(n).toString.replaceFirst("</link>$", "").replaceFirst(" */>$", ">")

    Comment(
      S.attr("if") match {
        case Full(clause) =>
          """[if %s]>
  %s
  <![endif]""" format (clause, htmlLink)

        case _ =>
          htmlLink
      }
    )
  }

  val render = (_: NodeSeq) => {
    val name = S.attr("name")
    val version = S.attr("version")
    val media = S.attr("media")
    val min = S.attr("min")

    name match {
      case Full(n) =>
        serveLink(n, version, media, min, extension)

      case _ =>
        Comment("FIXME: Link name attribute was not defined!")
    }
  }
}
