/*
package scala.collection
package mutable

import generic._

object DoubleLinkedHashMap extends MutableMapFactory[DoubleLinkedHashMap] {
  implicit def canBuildFrom[A]: CanBuildFrom[Coll, A, DoubleLinkedHashMap[A]] = MapCanBuildFrom[A]
  override def empty[A]: DoubleLinkedHashMap[A] = new DoubleLinkedHashMap[A]
}

final class DoubleLinkedEntry[A]( val key: A ) extends HashEntry[A, DoubleLinkedEntry[A]]{
  var older: DoubleLinkedEntry[A] = null
  var younger: DoubleLinkedEntry[A] = null
}

@SerialVersionUID(0xC7AD20ED79CE137AL) // sha1("scala.collection.mutable.DoubleLinkedHashMap-0").take(8)
class DoubleLinkedHashMap[A] extends Map[A]
                             with GenericMapTemplate[A, DoubleLinkedHashMap]
                             with MapLike[A, DoubleLinkedHashMap[A]]
                             with HashTable[A]
                             wiht Serializable{
  override def companion: GenericCompanion[DoubleLinkedHashMap] = DoubleLinkedHashMap

  type Entry = DoubleLinkedEntry[A]

  @transient
  var oldest, youngest: Entry = null

  // part of the RetMap family (Insertion Order Retaining collections)
  override def stringPrefix = "RetMap"
  override def size = tableSize

  /** alias for addYoungest */
  override def add(elem:A): Boolean = addYoungest(elem)

  private def linkYoungest(yE: Entry): Entry = {
    // new element will always be youngest, so link accordingly
    yE.older = youngest
    yE.younger = null

    // if oldest is null then this is our first element
    if ( oldest eq null ){
      oldest = yE
    }
    // otherwise, reroute the previous youngest
    else{
      youngest.younger = yE
    }

    // Link root element
    youngest = yE
    yE
  }

  /** Add a new element to the youngest position.
   *  If the element is already present, the collection remains unchanged
   */
  def addYoungest(elem: A): Boolean = {
    val oE = findEntry(elem)
    if (oE eq null) {
      addEntry( linkYoungest( new Entry( elem ) ) )
      true
    }
    else false
  }

  /** Will Map the element as the youngest one by force.
   *  If the element already exists, its place is directly modified.
   *  The effect is the same as calling remove(elem); addYoungest(elem),
   *  but the underlying HashTable is not modified. Result of false
   *  will indicate that the element was already present.
   */
  def forceYoungest(elem: A): Boolean = {
    val yE = findEntry(elem)
    if (yE ne null) {
      removeLinks( yE )
      linkYoungest( yE )
      false
    }
    else{
      // must return true
      addYoungest(elem)
    }
  }

  private def linkOldest(oE: Entry): Entry = {
    // the new element will be injected as the oldest one, so link accordingly
    oE.older = null
    oE.younger = oldest

    // if youngest is null then this is our first element
    if ( youngest eq null ){
      youngest = oE
    }
    // otherwise, reroute the previous oldest
    else{
      oldest.older = oE
    }

    // link root element
    oldest = oE
    oE
  }

  /** Add a new element to the oldest position.
   *  If the element is already present, the collection remains unchanged
   */
  def addOldest(elem: A): Boolean = {
    val oE = findEntry(elem)
    if (oE eq null) {
      addEntry( linkOldest( new Entry( elem ) ) )
      true
    }
    else false
  }


  /** Will Map the element as the oldest one by force.
   *  If the element already exists, its place is directly modified.
   *  The effect is the same as calling remove(elem); addOldest(elem),
   *  but the underlying HashTable is not modified. Result of false
   *  will indicate that the element was already present.
   */
  def forceOldest(elem: A): Boolean = {
    val oE = findEntry(elem)
    if (oE ne null) {
      removeLinks( oE )
      linkOldest( oE )
      false
    }
    else{
      // must return true
      addYoungest(elem)
    }
  }

  private def removeLinks(dE: Entry) {
    val isYoungest = dE eq youngest
    val isOldest = dE eq oldest

    // check and modify oldest boundary
    if ( isOldest ){
      // If it was the last element, oldest and youngest will point to dE.
      if ( isYoungest ){
        youngest = null
        oldest = null
      }
      else{
        dE.younger.older = null
        oldest = dE.younger
      }
    }
    // check and modify youngest boundary
    else if ( isYoungest ){
      dE.older.younger = null
      youngest = dE.older
    }
    // the removal was from the middle of the list
    else{
      dE.younger.older = dE.older
      dE.older.younger = dE.younger
    }
  }

  override def remove(elem: A): Boolean = {
    val oE = removeEntry(elem)
    if ( oE ne null ){
      removeLinks( oE )
      true
    }
    else false
  }

  override def += (elem: A): this.type = {
    addYoungest(elem)
    this
  }

  override def -= (elem: A): this.type = {
    remove(elem)
    this
  }

  override def contains(elem: A): Boolean = {
    findEntry(elem) ne null
  }

  override def clear() {
    clearTable()

    // garbage collect the list by removing root elements
    oldest = null
    youngest = null
  }

  /** Changing the collection whilst using the iterator will cause
   *  non-deterministic behavior due to mutability of all underlying
   *  structures.
   */
  override def iterator = new Iterator[A]{
    var cur = oldest
    def next = {
      val key = cur.key
      cur = cur.younger
      key
    }
    def hasNext = cur ne null
  }

  def reverseIterator = new Iterator[A] {
    var cur = youngest
    def next = {
      val key = cur.key
      cur = cur.older
      key
    }
    def hasNext = cur ne null
  }

  override def foreach[U](f: A =>  U) {
    var cur = oldest
    while ( cur ne null ){
      f(cur.key)
      cur = cur.younger
    }
  }

  def foreachReverse[U](f: A =>  U) {
    var cur = youngest
    while ( cur ne null ){
      f(cur.key)
      cur = cur.older
    }
  }

  override def clone() = new DoubleLinkedHashMap[A]() ++= this

// ################ SERIALIZATION ################

  /** Serialization is performed by writing down the Map size, proceeded by
   *  elements from oldest to youngest.
   */
  private def writeObject(out: java.io.ObjectOutputStream) {
    out.defaultWriteObject
    out.writeInt( size )
    foreach{ out.writeObject }
  }

  /** We unserialize into a temporary DoubleLinkedHashMap, and copy from there.
   *  This could later be optimized to prevent repetitive resizing of the
   *  underlying HashTable.
   */
  private def readObject(in: java.io.ObjectInputStream) {
    in.defaultReadObject
    val size = in.readInt

    val bld = new DoubleLinkedHashMap[A]()
    for ( i <- 1 to size ) bld += in.readObject.asInstanceOf[A]

    youngest = bld.youngest
    oldest = bld.oldest

    _loadFactor = bld._loadFactor
    tableSize = bld.tableSize
    threshold = bld.threshold
    table = bld.table
  }
}
*/
