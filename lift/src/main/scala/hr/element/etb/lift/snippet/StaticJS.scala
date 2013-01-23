package hr.element.etb.lift.snippet

import net.liftweb.http.S
import net.liftweb.util.Props
import net.liftweb.common.Full

import scala.xml._

trait StaticJS {
  val root = "static"
  val section = "js"

  val extension = ".js"

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

  def serveScript(
      name: String
    , version: Option[String]
    , min: Option[String]
    , suffix: String) = {

    val filename =
      getNameWithVersion(name, version) + getSuffix(min)

    val path =
      if (name.startsWith("../")) {
        "/%s/%s%s" format (root, filename substring 3, suffix)
      } else {
        "/%s/%s/%s%s" format (root, section, filename, suffix)
      }

    <script type="text/javascript" src={ path }/>
  }

  val render = (_: NodeSeq) => {
    val name = S.attr("name")
    val version = S.attr("version")
    val min = S.attr("min")

    name match {
      case Full(n) =>
        serveScript(n, version, min, extension)

      case _ =>
        Comment("FIXME: Script name attribute was not defined!")
    }
  }
}
