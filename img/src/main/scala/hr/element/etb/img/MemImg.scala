package hr.element.etb.img

import java.util.Arrays

import scala.math.max
import scala.math.min

class MemImg(val w: Int, val h: Int, val px: Array[Int]) extends Img[MemImg] {

  def this(w: Int, h: Int) =
    this(w, h, new Array[Int](w * h))

  //  ---------------------------------------------------------------------------

  def apply(x: Int, y: Int) =
    px(x + y * w)

  def update(x: Int, y: Int, c: Int) =
    px(x + y * w) = c

  //  ---------------------------------------------------------------------------

  def toBufImg = {
    var nI = new BufImg(w, h)
    nI.setPx(px)
    nI
  }

  def toMemImg = this

  override def clone() = new MemImg(w, h, px.clone())

  //  ---------------------------------------------------------------------------

  def fill(col: Int) = {
    Arrays.fill(px, col)
    this
  }

  def toGrayscale() = {
    for (index <- 0 until s) {
      val v = px(index)

      val r = (v >> 16) & 0xff
      val g = (v >> 8) & 0xff
      val b = (v) & 0xff

      px(index) = ((r * 30 + g * 59 + 11 * b) / 100) * 0x010101;
    }

    this
  }

  //  ---------------------------------------------------------------------------

  def bitBlt(iI: Img[_], pX: Int, pY: Int) = {
    val sX2 = pX
    val sY2 = pY
    val eX2 = pX + iI.w
    val eY2 = pY + iI.h

    val sX1 = max(0, sX2)
    val eX1 = min(w, eX2)

    if (sX1 < eX1) {
      val sY1 = max(0, sY2)
      val eY1 = min(h, eY2)

      if (sY1 < eY1) {
        val offset = (sX1 - sX2) - sY2 * iI.w
        val limit = eX1 - sX1

        val mI = iI.toMemImg

        for (y <- sY1 until eY1)
          System.arraycopy(mI.px, offset + y * mI.w, px, sX1 + y * w, limit);
      }
    }

    this
  }

  def crop(xy: Int) = {
    val mX = max(w + (xy << 1), 0)
    val mY = max(h + (xy << 1), 0)

    val mI = new MemImg(mX, mY)
    mI.bitBlt(this, xy, xy)
    mI
  }

  def trim(bgCol: Int = 0, border: Int = 0) = {
    var minX = w
    var minY = h
    var maxX = 0
    var maxY = 0

    for (y <- 0 until h; x <- 0 until w if (px(x + y * w) != bgCol)) {
      minX = min(minX, x)
      minY = min(minY, y)
      maxX = max(maxX, x)
      maxY = max(maxY, y)
    }

    val nW = max((maxX - minX + 1) + (border << 1), 1)
    val nH = max((maxY - minY + 1) + (border << 1), 1)

    val nI = new MemImg(nW, nH)
    nI.bitBlt(this, -minX + border, -minY + border)
    nI
  }

  //  ---------------------------------------------------------------------------

  override def equals(other: Any) = other match {
    case mI: MemImg =>
      (w == mI.w) && (h == mI.h) && Arrays.equals(px, mI.px)
    case bI: BufImg =>
      (w == bI.w) && (h == bI.h) && Arrays.equals(px, bI.getPx())
    case _ => false
  }

  override def toString =
    "MemImg[" + w + "x" + h + "]"
}
