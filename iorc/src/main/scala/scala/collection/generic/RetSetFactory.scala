/*
package scala.collection.generic

import scala.collection.{RetSet, RetSetLike}
import scala.collection.mutable.{Builder, SetBuilder}

abstract class RetSetFactory[CC[A] <: RetSet[A] with RetSetLike[A, CC[A]]] extends GenericCompanion[CC]{
  override type Coll = CC[_]

  override def empty[A]: CC[A]

  override def apply[A](elems: A*): CC[A] = (newBuilder[A] ++= elems).result

  def newBuilder[A]: Builder[A, CC[A]] = new SetBuilder[A, CC[A]](empty)

  implicit def newCanBuildFrom[A]: CanBuildFrom[Coll, A, CC[A]] = retSetCanBuildFrom

  implicit def retSetCanBuildFrom[A] = new CanBuildFrom[Coll, A, CC[A]] {
    def apply(from: Coll) = newBuilder[A]
    def apply() = newBuilder[A]
  }
}
*/
