/*
package scala.collection.immutable

import scala.collection.generic._
import scala.collection.mutable.Builder

trait RetSet[A] extends Set[A]
                with scala.collection.RetSet[A]
                with GenericSetTemplate[A, RetSet]
                with scala.collection.RetSetLike[A, RetSet[A]]{
  override def empty: RetSet[A] = RetSet.empty[A]
  override def companion: scala.collection.generic.GenericCompanion[RetSet] = RetSet
}

object RetSet extends ImmutableRetSetFactory[RetSet] {
  implicit def canBuildFrom[A]: CanBuildFrom[Coll, A, RetSet[A]] = retSetCanBuildFrom[A]
  override def empty[A]: RetSet[A] = EmptyRetSet.asInstanceOf[RetSet[A]]

  @serializable
  private object EmptyRetSet extends RetSet[Any] {
    override def size: Int = 0
    def contains(elem: Any): Boolean = false
    def + (elem: Any): RetSet[Any] = new RetSet1(elem)
    def - (elem: Any): RetSet[Any] = this
    def iterator: Iterator[Any] = Iterator.empty
    override def foreach[U](f: Any =>  U): Unit = {}
  }

  @serializable @SerialVersionUID(0x4C696D6F53657401L)
  class RetSet1[A](elem1: A) extends RetSet[A] {
    override def size: Int = 1
    def contains(elem: A): Boolean =
      elem == elem1
    def + (elem: A): RetSet[A] =
      if (contains(elem)) this
      else new RetSet2(elem1, elem)
    def - (elem: A): RetSet[A] =
      if (elem == elem1) RetSet.empty
      else this
    def iterator: Iterator[A] =
      Iterator(elem1)
    override def foreach[U](f: A =>  U): Unit = {
      f(elem1)
    }
  }

  @serializable @SerialVersionUID(0x4C696D6F53657402L)
  class RetSet2[A](elem1: A, elem2: A) extends RetSet[A] {
    override def size: Int = 2
    def contains(elem: A): Boolean =
      elem == elem1 || elem == elem2
    def + (elem: A): RetSet[A] =
      if (contains(elem)) this
      else new RetSet3(elem1, elem2, elem)
    def - (elem: A): RetSet[A] =
      if (elem == elem1) new RetSet1(elem2)
      else if (elem == elem2) new RetSet1(elem1)
      else this
    def iterator: Iterator[A] =
      Iterator(elem1, elem2)
    override def foreach[U](f: A =>  U): Unit = {
      f(elem1); f(elem2)
    }
  }

  @serializable @SerialVersionUID(0x4C696D6F53657403L)
  class RetSet3[A](elem1: A, elem2: A, elem3: A) extends RetSet[A] {
    override def size: Int = 3
    def contains(elem: A): Boolean =
      elem == elem1 || elem == elem2 || elem == elem3
    def + (elem: A): RetSet[A] =
      if (contains(elem)) this
      else new RetSet4(elem1, elem2, elem3, elem)
    def - (elem: A): RetSet[A] =
      if (elem == elem1) new RetSet2(elem2, elem3)
      else if (elem == elem2) new RetSet2(elem1, elem3)
      else if (elem == elem3) new RetSet2(elem1, elem2)
      else this
    def iterator: Iterator[A] =
      Iterator(elem1, elem2, elem3)
    override def foreach[U](f: A =>  U): Unit = {
      f(elem1); f(elem2); f(elem3)
    }
  }

  @serializable @SerialVersionUID(0x4C696D6F53657404L)
  class RetSet4[A](elem1: A, elem2: A, elem3: A, elem4: A) extends RetSet[A] {
    override def size: Int = 4
    def contains(elem: A): Boolean =
      elem == elem1 || elem == elem2 || elem == elem3 || elem == elem4
    def + (elem: A): RetSet[A] =
      if (contains(elem)) this
      else this // IndexedSeqSet(elem1, elem2, elem3, elem4, elem)
    def - (elem: A): RetSet[A] =
      if (elem == elem1) new RetSet3(elem2, elem3, elem4)
      else if (elem == elem2) new RetSet3(elem1, elem3, elem4)
      else if (elem == elem3) new RetSet3(elem1, elem2, elem4)
      else if (elem == elem4) new RetSet3(elem1, elem2, elem3)
      else this
    def iterator: Iterator[A] =
      Iterator(elem1, elem2, elem3, elem4)
    override def foreach[U](f: A =>  U): Unit = {
      f(elem1); f(elem2); f(elem3); f(elem4)
    }
  }
}
*/
