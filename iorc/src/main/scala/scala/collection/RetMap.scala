/*
package scala.collection
import generic._

trait RetMap[A] extends Map[A] with RetMapLike[A, RetMap[A]] {
  override def empty: RetMap[A] = RetMap.empty[A]
}

object RetMap extends RetMapFactory[RetMap] {
  override def empty[A]: immutable.RetMap[A] = immutable.RetMap.empty[A]
  implicit def canBuildFrom[A]: CanBuildFrom[Coll, A, RetMap[A]] = retMapCanBuildFrom[A]
}
*/
