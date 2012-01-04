package hr.element.etb.img

import java.io.{File, InputStream, OutputStream,
  FileInputStream, FileOutputStream,
  ByteArrayInputStream, ByteArrayOutputStream}
import java.awt.Color
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

import java.util.Arrays

object BufImg{
  def fromStream(iS: InputStream): BufImg = {
    val lI = ImageIO.read(iS)
    val nI = new BufImg(lI.getWidth, lI.getHeight)
    nI.withG2D{ _.drawImage(lI, 0, 0, null) }
    nI
  }

  def fromPath(path: String): BufImg =
    fromStream(new FileInputStream(path))

  def fromByteArray(bA: Array[Byte]): BufImg = {
    val bAIS = new ByteArrayInputStream(bA)
    val nI = fromStream(bAIS)
    bAIS.close()
    nI
  }

//  ---------------------------------------------------------------------------

  def toStream(buffImage: BufferedImage, oS: OutputStream, format: String = "png") {
    ImageIO.write(buffImage, format, oS)
  }

  def toPath(buffImage: BufferedImage, path: String, format: String = "png") {
    val fOS = new FileOutputStream(path)
    toStream(buffImage, fOS, format)
    fOS.close()
  }

  def toByteArray(buffImage: BufferedImage, format: String = "png"): Array[Byte] = {
    val bAOS = new ByteArrayOutputStream
    toStream(buffImage, bAOS, format)
    bAOS.toByteArray
  }

//  ---------------------------------------------------------------------------

  def getPx(buffImage: BufferedImage) = buffImage.getType match {
    case BufferedImage.TYPE_INT_RGB | BufferedImage.TYPE_INT_ARGB =>
      buffImage.getData().getDataElements(0, 0,
        buffImage.getWidth, buffImage.getHeight, null).asInstanceOf[Array[Int]]
    case _ =>
      buffImage.getRGB(0, 0, buffImage.getWidth, buffImage.getHeight, null, 0, buffImage.getWidth)
  }

  def setPx(buffImage: BufferedImage, px: Array[Int]) = {
    buffImage.getWritableTile(0, 0).setDataElements(0, 0,
      buffImage.getWidth, buffImage.getHeight, px)
  }
}

class BufImg(val w: Int, val h: Int)
    extends BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
    with Img {

//  ---------------------------------------------------------------------------

  def toBufImg = this
  def toMemImg = new MemImg(w, h, BufImg.getPx(this))

  override def clone() = {
    val nI = new BufImg(w, h)
    nI.bitBlt(this, 0, 0)
    nI
  }

//  ---------------------------------------------------------------------------

  def getPx() =
    BufImg.getPx(this)

  def setPx(px: Array[Int]) =
    BufImg.setPx(this, px)

//  ---------------------------------------------------------------------------

  def withG2D(f: java.awt.Graphics2D => Unit): Unit = {
    val g2D = super.createGraphics();
    g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
    g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    f(g2D)
    g2D.dispose()
  }

//  ---------------------------------------------------------------------------

  def fill(col: Int) = withG2D{ g =>
    g.setColor(new Color(col))
    g.fillRect(0, 0, w, h)
  }

//  ---------------------------------------------------------------------------

  def crop(dXY: Int) = {
    val nI = new BufImg(w + (dXY << 1), h + (dXY << 1))
    nI.bitBlt(this, dXY, dXY)
    nI
  }

  def bitBlt(iI: Img, pX: Int, pY: Int): Unit = withG2D{
    _.drawImage(iI.toBufImg, pX, pY, null)
  }

//  ---------------------------------------------------------------------------

  def thumbnail(maxW: Int, maxH: Int): BufImg = {

    def adjustSize(oW: Int, oH: Int, maxW: Int, maxH: Int) = {
      var w = oW
      var h = oH
      if ( w > maxW ) { h = h * maxW / w; w = maxW }
      if ( h > maxH ) { w = w * maxH / h; h = maxH }
      (w, h)
    }

    val (cW, cH) = adjustSize(w, h, maxW, maxH)
    val iI = getScaledInstance(cW, cH, java.awt.Image.SCALE_AREA_AVERAGING)
    val nI = new BufImg(cW, cH)
    nI.withG2D{_.drawImage(iI, 0, 0, null)}
    nI
  }

//  ---------------------------------------------------------------------------

  override def equals(other: Any) = other match {
    case bI: BufImg =>
      (w == bI.w) && (h == bI.h) && Arrays.equals(getPx(), bI.getPx())
    case mI: MemImg =>
      (w == mI.w) && (h == mI.h) && Arrays.equals(getPx(), mI.px)
    case _ => false
  }

  override def toString =
    "BufImg[" + w + "x" + h + "]"
}

