package hr.element.etb.img

trait Img[T] extends Cloneable { self: T =>
  val w: Int
  val h: Int
  val s = w * h

  def toMemImg: MemImg
  def toBufImg: BufImg

  def fill(color: Int): this.type
  def clear() = fill(0)

  def crop(dXY: Int): T
  def trim(bgCol: Int = 0, border: Int = 0): T

  def bitBlt(iI: Img[_], pX: Int, pY: Int): this.type

  override lazy val hashCode = w << 16 + h

  def canEqual(other: Any) = other match {
    case iI: Img[_] => true
    case _ => false
  }
}
