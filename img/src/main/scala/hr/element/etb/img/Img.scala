package hr.element.etb.img

trait Img extends Cloneable
{
  val w: Int
  val h: Int
  val s = w * h

  def toMemImg: MemImg
  def toBufImg: BufImg

  def fill(color: Int): Unit
  def clear() = fill(0)

  def crop(dXY: Int): Img
  def bitBlt(iI: Img, pX: Int, pY: Int): Unit

  override lazy val hashCode = w << 16 + h

  def canEqual(other: Any) = other match {
    case iI: Img => true
    case _ => false
  }
}
