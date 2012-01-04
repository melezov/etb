/*
package scala.collection
package generic

import mutable.{Builder, MapBuilder}

abstract class RetMapFactory[CC[A] <: RetMap[A] with RetMapLike[A, CC[A]]] extends GenericCompanion[CC]{
  override type Coll = CC[_]

  override def empty[A]: CC[A]

  override def apply[A](elems: A*): CC[A] = (newBuilder[A] ++= elems).result

  def newBuilder[A]: Builder[A, CC[A]] = new MapBuilder[A, CC[A]](empty)

  implicit def retMapCanBuildFrom[A] = new CanBuildFrom[Coll, A, CC[A]] {
    def apply(from: Coll) = newBuilder[A]
    def apply() = newBuilder[A]
  }
}
*/
