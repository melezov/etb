package hr.element.etb.lift.snippet

trait GeneratedJS extends StaticJS with GeneratedTimestamp {
  override val section = "coffee-js"

  override def serveScript(name: String, version: Option[String], min: Option[String]) =
    super.serveScript(name + timestampSuffix, version, min)
}
