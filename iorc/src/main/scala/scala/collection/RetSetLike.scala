/*
package scala.collection

import scala.collection.generic.Retaining

trait RetSetLike[A, +This <: RetSet[A] with RetSetLike[A, This]] extends Retaining[A] with SetLike[A, This] {
self =>
  override def stringPrefix = "RetSet"
  override def keySet = repr

  override def youngestKey: A = head
  override def oldestKey: A = last
}
*/
