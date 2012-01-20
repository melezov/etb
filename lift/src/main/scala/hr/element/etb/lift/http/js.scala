package hr.element.etb.lift.http

import net.liftweb.http.js._
import net.liftweb.util.Helpers._

package object js {
  case class Prompt(text: String, default: String = "") extends JsExp {
    def toJsCmd = "prompt(" + text.encJs + "," + default.encJs + ")"
  }

  case object ReloadPage extends JsCmd {
    def toJsCmd = "window.location.reload();"
  }
}
