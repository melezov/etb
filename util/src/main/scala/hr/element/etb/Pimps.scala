package hr.element.etb

import scala.util.Random
import scala.util.matching.Regex
import scala.xml.{ XML, Elem, NodeSeq, PrettyPrinter }
import scala.xml.parsing.ConstructingParser
import scala.io.Source
import java.io.{ StringWriter, PrintWriter }

/**
  * The object <code>Pimps</code> provides helper methods for primitives
  *  and common Scala classes.
  *
  *  @author  Marko Elezović
  *  @version 0.1
  *  @since   0.1
  */

object Pimps extends Pimps {

  /**
    * Provides the <code>times</code> method for anonymous iteration.
    */
  class PimpedInt(i: Int) {
    def times(body: => Unit) { (1 to i).foreach(_ => body) }
  }

  // ----------------------------------------------------------------------------

  /**
    * Converts boolean to "yes" / "no" String values.
    */
  class PimpedBoolean(b: Boolean) {
    def toYN = if (b) "yes" else "no"
  }

  // ----------------------------------------------------------------------------

  /**
    * String helper class with one method which removes all repeating
    * whitespaces, new lines and non braking spaces (&nbsp;).
    */
  private val WhiteSpaces = """[\s\xA0]+""".r

  class PimpedString(s: String) {
    def ksp = WhiteSpaces.replaceAllIn(s, " ") trim
    def kas = WhiteSpaces.replaceAllIn(s, "")

    def toElem = ConstructingParser
      .fromSource(Source.fromString(s), true)
      .document.docElem.asInstanceOf[Elem]
  }

  // ----------------------------------------------------------------------------

  /**
    * Provides the <code>random</code> functionality for getting a random
    *  value constrained by the underlying <code>Range</code>.
    */
  class PimpedRange(r: Range) {
    def random = r(Random.nextInt(r.size))
  }

  // ----------------------------------------------------------------------------

  /**
    * For easy classpath retrieval of resources via class reference.
    */

  class PimpedClass(c: Class[_]) {
    def getPackagePath(filename: String) = {
      '/' + c.getPackage.getName.replace('.', '/') + '/' + filename
    }
  }

  // ----------------------------------------------------------------------------

  /**
    * Returns all matcher objects from a regex search.
    */
  class PimpedRegex(r: Regex) {
    def findAllMatchesIn(s: String): List[Regex.Match] = {
      r.findFirstMatchIn(s) match {
        case Some(e: Regex.Match) =>
          e :: findAllMatchesIn(e.after.toString)
        case None =>
          Nil
      }
    }
  }

  private object PrettyPrinter80 extends PrettyPrinter(80, 2)

  /**
    * Provides a short-hand for pretty printing XML
    */
  class PimpedElem(e: Elem) {
    def prettyPrint = ConstructingParser
      .fromSource(Source.fromString(PrettyPrinter80.format(e)), true)
      .document.docElem.asInstanceOf[Elem]
  }

  /**
    * Provides a short-hand for pretty printing XML
    */
  class PimpedNodeSeq(ns: NodeSeq) {
    def prettyPrintString = PrettyPrinter80.formatNodes(ns)
  }

  /**
    * Gets the stack trace in String form
    */
  class PimpedThrowable(t: Throwable) {
    def stackTraceString: String = {
      val sW = new StringWriter
      t.printStackTrace(new PrintWriter(sW))
      sW.toString
    }
  }
}

trait Pimps {
  import Pimps._

  implicit def pimpMyInt(i: Int) =
    new PimpedInt(i)

  implicit def pimpMyBoolean(b: Boolean) =
    new PimpedBoolean(b)

  implicit def pimpMyString(s: String) =
    new PimpedString(s)

  implicit def pimpMyRange(r: Range) =
    new PimpedRange(r)

  implicit def pimpMyClass(c: Class[_]) =
    new PimpedClass(c)

  implicit def pimpMyRegex(r: Regex) =
    new PimpedRegex(r)

  implicit def pimpMyElem(e: Elem) =
    new PimpedElem(e)

  implicit def pimpMyNodeSeq(ns: NodeSeq) =
    new PimpedNodeSeq(ns)

  implicit def pimpMyThrowable(t: Throwable) =
    new PimpedThrowable(t)
}
