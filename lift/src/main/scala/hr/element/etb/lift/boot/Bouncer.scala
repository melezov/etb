package hr.element.etb.lift.boot

import net.liftweb.http.LiftRules

class Bouncer(root: String) {
  /* Lift is currently set not to accept ajaxPath, cometPath and friends
     with level of depth > 1,  so we are replacing slashes with minuses. */
  def unslashify(path: String) =
    path.replace('/', '-')

  val ajaxPath = unslashify(root + "/ajax")
  val cometPath = unslashify(root + "/comet")
  val classPath = unslashify(root + "/classpath")

  val staticPath = root + "/static"

  def init() {
    LiftRules.ajaxPath = ajaxPath
    LiftRules.cometPath = cometPath
    LiftRules.resourceServerPath = classPath
  }
}
