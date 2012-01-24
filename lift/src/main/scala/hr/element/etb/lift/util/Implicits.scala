package hr.element.etb.lift.util

import scala.xml._

object Implicits {
  type FNN = NodeSeq => NodeSeq

  class RichNodeCol[A](nodeCol: Iterable[A]) {
    def flatXML(f: A => NodeSeq): NodeSeq = {
      nodeCol.toSeq.flatMap(f)
    }
  }

  implicit def impaleRichNodeCol[A](nodeCol: Iterable[A]) =
    new RichNodeCol(nodeCol)
}
