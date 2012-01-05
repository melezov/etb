package hr.element.etb

import scala.annotation.tailrec

object Util {
  def waitUntil(timeout: Long, resolution: Long)(predicate: => Boolean) = {

    @tailrec
    def waitUntil(end: Long, resolution: Long)(predicate: => Boolean): Boolean = {
      val current = System.currentTimeMillis
      if (current > end) {
        false
      } else {
        if (predicate) {
          true
        } else {
          val sleep = scala.math.min(resolution, end - current)
          Thread.sleep(sleep)
          waitUntil(end, resolution)(predicate)
        }
      }
    }

    val end = System.currentTimeMillis + timeout
    waitUntil(end, resolution)(predicate)
  }
}
