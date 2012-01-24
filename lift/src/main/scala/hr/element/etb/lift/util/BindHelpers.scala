package hr.element.etb.lift.util

import net.liftweb.util.BindHelpers._
import scala.xml.{ NodeSeq, Text }

object BindHelpers extends BindHelpers

trait BindHelpers {
  object FuncFormatAttrBindParam {
    def apply(name: String, newAttr: String, data: Any*) {
      new FuncAttrBindParam(name, (n: NodeSeq) => Text(n.text format (data: _*)), newAttr)
    }
  }

  object FuncAppendAttrBindParam {
    def apply(name: String, appendix: String, newAttr: String) {
      new FuncAttrBindParam(name, (n: NodeSeq) => Text(n.text + ' ' + appendix), newAttr)
    }
  }
}
