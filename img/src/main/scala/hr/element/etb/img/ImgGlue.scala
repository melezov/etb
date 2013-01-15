package hr.element.etb.img

case class ImgGlue(iL: Img[_]*)(implicit val col: Int = 0x3f3f3f, implicit val spacing: Int = 1) {

  lazy val space = if (spacing != 0) (iL.length - 1) * spacing else 0

  lazy val maxH = iL.map(_.h).max
  lazy val maxW = iL.map(_.w).max
  lazy val sumH = iL.map(_.h).sum + space
  lazy val sumW = iL.map(_.w).sum + space

  def gX = {
    val gI = new MemImg(sumW, maxH)
    println(gI)
    gI.fill(col)

    var curX = 0
    for (cI <- iL) {
      gI.bitBlt(cI, curX, (gI.h - cI.h) >> 1)
      curX += cI.w + spacing
    }

    gI
  }

  def gY = {
    val gI = new MemImg(maxW, sumH)
    gI.fill(col)

    var curY = 0
    for (cI <- iL) {
      gI.bitBlt(cI, (gI.w - cI.w) >> 1, curY)
      curY += cI.h + spacing
    }

    gI
  }

  override def toString() = iL.mkString(", ", "[", "]")
}
