package hr.element.etb.lift.lib

import scala.xml.NodeSeq

object SinkHole extends Function1[NodeSeq, NodeSeq] {
  def apply(x: NodeSeq) = NodeSeq.Empty
}
