/*
package scala.collection
package immutable

import generic._
import mutable.Builder

trait RetMap[A] extends Map[A]
                   with scala.collection.RetMap[A]
                   with RetMapLike[A, RetMap[A]] {
  override def empty: RetMap[A] = RetMap.empty[A]
  override def companion: GenericCompanion[RetMap] = RetMap
}

object RetMap extends ImmutableRetMapFactory[RetMap] {
  implicit def canBuildFrom[A]: CanBuildFrom[Coll, A, RetMap[A]] = retMapCanBuildFrom[A]
  override def empty[A]: RetMap[A] = EmptyRetMap.asInstanceOf[RetMap[A]]

  @serializable
  private object EmptyRetMap extends RetMap[Any] {
    override def size: Int = 0
    def contains(elem: Any): Boolean = false
    def + (elem: Any): RetMap[Any] = new RetMap1(elem)
    def - (elem: Any): RetMap[Any] = this
    def iterator: Iterator[Any] = Iterator.empty
    override def foreach[U](f: Any =>  U): Unit = {}
  }

  @serializable @SerialVersionUID(0x4C696D6F53657401L)
  class RetMap1[A](elem1: A) extends RetMap[A] {
    override def size: Int = 1
    def contains(elem: A): Boolean =
      elem == elem1
    def + (elem: A): RetMap[A] =
      if (contains(elem)) this
      else new RetMap2(elem1, elem)
    def - (elem: A): RetMap[A] =
      if (elem == elem1) RetMap.empty
      else this
    def iterator: Iterator[A] =
      Iterator(elem1)
    override def foreach[U](f: A =>  U): Unit = {
      f(elem1)
    }
  }

  @serializable @SerialVersionUID(0x4C696D6F53657402L)
  class RetMap2[A](elem1: A, elem2: A) extends RetMap[A] {
    override def size: Int = 2
    def contains(elem: A): Boolean =
      elem == elem1 || elem == elem2
    def + (elem: A): RetMap[A] =
      if (contains(elem)) this
      else new RetMap3(elem1, elem2, elem)
    def - (elem: A): RetMap[A] =
      if (elem == elem1) new RetMap1(elem2)
      else if (elem == elem2) new RetMap1(elem1)
      else this
    def iterator: Iterator[A] =
      Iterator(elem1, elem2)
    override def foreach[U](f: A =>  U): Unit = {
      f(elem1); f(elem2)
    }
  }

  @serializable @SerialVersionUID(0x4C696D6F53657403L)
  class RetMap3[A](elem1: A, elem2: A, elem3: A) extends RetMap[A] {
    override def size: Int = 3
    def contains(elem: A): Boolean =
      elem == elem1 || elem == elem2 || elem == elem3
    def + (elem: A): RetMap[A] =
      if (contains(elem)) this
      else new RetMap4(elem1, elem2, elem3, elem)
    def - (elem: A): RetMap[A] =
      if (elem == elem1) new RetMap2(elem2, elem3)
      else if (elem == elem2) new RetMap2(elem1, elem3)
      else if (elem == elem3) new RetMap2(elem1, elem2)
      else this
    def iterator: Iterator[A] =
      Iterator(elem1, elem2, elem3)
    override def foreach[U](f: A =>  U): Unit = {
      f(elem1); f(elem2); f(elem3)
    }
  }

  @serializable @SerialVersionUID(0x4C696D6F53657404L)
  class RetMap4[A](elem1: A, elem2: A, elem3: A, elem4: A) extends RetMap[A] {
    override def size: Int = 4
    def contains(elem: A): Boolean =
      elem == elem1 || elem == elem2 || elem == elem3 || elem == elem4
    def + (elem: A): RetMap[A] =
      if (contains(elem)) this
      // FIXME: Can't connect to VectorHashMap, due to it not being a RetMap
      else this // VectorHashMap[A]( Vector(elem1, elem2, elem3, elem4, elem) )
    def - (elem: A): RetMap[A] =
      if (elem == elem1) new RetMap3(elem2, elem3, elem4)
      else if (elem == elem2) new RetMap3(elem1, elem3, elem4)
      else if (elem == elem3) new RetMap3(elem1, elem2, elem4)
      else if (elem == elem4) new RetMap3(elem1, elem2, elem3)
      else this
    def iterator: Iterator[A] =
      Iterator(elem1, elem2, elem3, elem4)
    override def foreach[U](f: A =>  U): Unit = {
      f(elem1); f(elem2); f(elem3); f(elem4)
    }
  }
}
*/
