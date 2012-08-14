package hr.element.etb.lift.snippet

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import net.liftweb.util.Props

private object GeneratedTimestamp {
  private val TimestampFormat = DateTimeFormat.forPattern("yyyyMMddHHmmss")

  def formatTime = TimestampFormat.print(new DateTime())
  val bootTime = formatTime
}

trait GeneratedTimestamp {
  import GeneratedTimestamp._

  protected def timestampSuffix =
    "-" + (if (Props.productionMode) bootTime else formatTime)
}
