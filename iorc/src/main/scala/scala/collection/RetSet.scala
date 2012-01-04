/*
package scala.collection

import scala.collection.generic.{CanBuildFrom, RetSetFactory}

trait RetSet[A] extends Set[A] with RetSetLike[A, RetSet[A]] {
  override def empty: RetSet[A] = RetSet.empty[A]
}

object RetSet extends RetSetFactory[RetSet] {
  override def empty[A]: immutable.RetSet[A] = immutable.RetSet.empty[A]
  implicit def canBuildFrom[A]: CanBuildFrom[Coll, A, RetSet[A]] = retSetCanBuildFrom[A]
}
*/
