package hr.element.etb.lift.boot

import net.liftweb.http.LiftRules

class Bouncer(root: String) {
  val ajaxPath = root + "-ajax"
  val cometPath = root + "-comet"
  val classPath = root + "-classpath"
  val staticPath = root + "-static"

  def init() {
    LiftRules.ajaxPath = ajaxPath
    LiftRules.cometPath = cometPath
    LiftRules.resourceServerPath = classPath
  }
}
