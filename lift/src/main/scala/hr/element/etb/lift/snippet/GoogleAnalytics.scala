package hr.element.etb.lift.snippet

import scala.xml.NodeSeq

import net.liftweb._
import http._
import js.JsCmds._
import util._
import Helpers._
import BindPlus._

trait GoogleAnalytics {
  val Disabled = <!-- Google Analytics is disabled -->
  val Undefined = <!-- Google Analytics page tracking code is not defined -->

  val pageTrackingCode: Option[String] = None

  def render(scripts: NodeSeq) = Props.productionMode match {
    case true => injectTrackingCode(scripts) openOr Undefined
    case _ => Disabled
  }

  /**
   * If lift is being run in production mode, try to read the
   * ga.ptc (Google Analytics Page Tracking Code) property.
   * Then create a javascript variable with the said code,
   * and name it with the value which was hard-coded in the
   * google-analytics.html snippet
   */

  def injectTrackingCode(scripts: NodeSeq) =
    for (name <- S.attr("name"); value <- pageTrackingCode orElse Props.get("ga.ptc"))
      yield scripts.bind("ga", "ptc" -> Script(JsCrVar(name, value)))
}

/**
 * Invoke via this snippet:
 *
 * <lift:GoogleAnalytics name="gaptc">
 * <ga:ptc/>
 * <script type="text/javascript">
 * var _gaq = _gaq || [];
 * _gaq.push(['_setAccount', gaptc]);
 * _gaq.push(['_trackPageview']);
 *
 * (function() {
 * var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
 * ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
 * var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
 * })();
 * </script>
 * </lift:GoogleAnalytics>
 */
