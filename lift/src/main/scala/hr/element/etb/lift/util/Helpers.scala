package hr.element.etb.lift.util

import net.liftweb.util.Helpers._
import scala.xml.{ NodeSeq, Text }

import java.util.UUID

object Helpers extends Helpers

trait Helpers {
  object FuncFormatAttrBindParam {
    def apply(name: String, newAttr: String, data: Any*) = {
      new FuncAttrBindParam(name, (n: NodeSeq) => Text(n.text format (data: _*)), newAttr)
    }
  }

  object AsUUID {
    def apply(uuid: String): UUID =
      UUID.fromString(uuid)

    def unapply(uuid: String): Option[UUID] =
      try Some(apply(uuid)) catch { case _ => None }
  }
}
