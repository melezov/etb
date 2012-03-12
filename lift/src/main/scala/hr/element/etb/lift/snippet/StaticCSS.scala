package hr.element.etb.lift.snippet

import net.liftweb.http.S
import net.liftweb.util.Props
import net.liftweb.common.Full

import scala.xml._

trait StaticCSS {
  val root = "static"
  val section = "css"

  protected val defaultVersions: PartialFunction[String, String] =
    Map.empty

  protected def getNameWithVersion(name: String, version: Option[String]) =
    name + (
      version orElse (
        defaultVersions lift (name)
      ) map ("-"+) getOrElse ("")
    )

  def serveLink(
    name: String, version: Option[String], media: Option[String]) = {

    val someMedia = media.getOrElse("screen")

    val filename =
      getNameWithVersion(name, version)

    val path =
      if (name.startsWith("../")) {
        "/%s/%s.css" format (root, filename.substring(3))
      } else {
        "/%s/%s/%s.css" format (root, section, filename)
      }

    <link href={ path } type="text/css" rel="stylesheet" media={ someMedia }/>
  }

  def comment(n: NodeSeq) = {
    val htmlLink = render(n).toString.dropRight("</link>".length)

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

    name match {
      case Full(n) =>
        serveLink(n, version, media)

      case _ =>
        Comment("FIXME: Link name attribute was not defined!")
    }
  }
}
