package hr.element.etb.img.geom

case class Point(x: Int, y: Int)
case class Dim(w: Int, h: Int)

object Rect {
  def apply(p: Point, d: Dim): Rect = {
    Rect(p.x, p.y, d.w, d.h)
  }

  def apply(p1: Point, p2: Point): Rect = {
    val (sx, ex) = if (p1.x <= p2.x) {
      (p1.x, p2.x)
    } else {
      (p2.x, p1.x)
    }

    val (sy, ey) = if (p1.y <= p2.y) {
      (p1.y, p2.y)
    } else {
      (p2.y, p1.y)
    }

    apply(
      sx
    , sy
    , ex - sx + 1
    , ey - sy + 1
    )
  }
}

case class Rect(x: Int, y: Int, w: Int, h: Int) {
  val minX = x
  val maxX = x + w - 1
  val minY = y
  val maxY = y + h - 1

  val centerX = (minX + maxX) / 2.0
  val centerY = (minY + maxY) / 2.0
}
