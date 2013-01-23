package hr.element.etb.lift.snippet

trait GeneratedCSS extends StaticCSS with GeneratedTimestamp {
  override val section = "less-css"

  override def serveLink(name: String, version: Option[String], media: Option[String], min: Option[String], suffix: String) =
    super.serveLink(name, version, media, min, suffix + timestampSuffix)
}
