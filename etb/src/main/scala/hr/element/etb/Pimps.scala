package hr.element.etb

import scala.util.Random
import scala.util.matching.Regex

/** The object <code>Pimps</code> provides helper methods for primitives
 *  and common Scala classes.
 *
 *  @author  Marko Elezović
 *  @version 0.1
 *  @since   0.1
 */

object Pimps {

/** Provides the <code>times</code> method for anonymous iteration.
 */
  implicit def pimpMyInt(i: Int) = new {
    def times(body: => Unit) { (1 to i).foreach(_ => body) }
  }

// ----------------------------------------------------------------------------

/** Converts boolean to "yes" / "no" String values.
 */
  implicit def pimpMyBoolean(b: Boolean) = new {
    def toYN = if (b) "yes" else "no"
  }

// ----------------------------------------------------------------------------

/**
  String helper class with one method which removes all repeating
  whitespaces, new lines and non braking spaces (&nbsp;).
  */
  val WhiteSpaces = """[\s\xA0]+""".r

  implicit def pimpMyString(s: String) = new {
    def ksp = WhiteSpaces.replaceAllIn(s," ") trim
    def kas = WhiteSpaces.replaceAllIn(s,"")
  }

// ----------------------------------------------------------------------------

/** Provides the <code>random</code> functionality for getting a random
 *  value constrained by the underlying <code>Range</code>.
 */
  implicit def pimpMyRange( r: Range ) = new {
    def random = r( Random.nextInt(r.size) )
  }

/** Enables manual method listing in REPL.
 */
  implicit def pimpMyAny( a: AnyRef ) = new {
    def gdm {
      a.getClass.getDeclaredMethods.foreach(println)
    }

    def gdmx {
      def getSuperDeclaredMethods( c: Class[_] ):List[String] =
        c.getSuperclass match{
         case null => Nil
         case x => getSuperDeclaredMethods( x ) ++
           c.getDeclaredMethods.sortBy(_.getName).map(m=>c.getSimpleName+ ": " + m).toList
        }

      getSuperDeclaredMethods( a.getClass ) foreach(println)
    }
  }

/**
  Returns all matcher objects from a regex search.
  */

  implicit def pimpMyRegex(r: Regex) = new {
    def findAllMatchesIn(s: String): List[Regex.Match] = {
      r.findFirstMatchIn(s) match {
        case Some(e: Regex.Match) =>
          e :: findAllMatchesIn(e.after.toString)
        case None =>
          Nil
      }
    }
  }
}
