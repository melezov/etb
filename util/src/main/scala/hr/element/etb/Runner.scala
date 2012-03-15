package hr.element.etb
package object io {

  import sys.process._
  import sys.process.BasicIO.transferFully

  import java.io.{ File, ByteArrayInputStream, ByteArrayOutputStream }
  import java.lang.{ Thread, Runnable, InterruptedException }

  implicit val WaitPeriod = 60000
  implicit val WorkingDir = new File(".")

  object Runner {

    def apply(cmd: Seq[String], input: Array[Byte] = Array())(implicit workingDir: File, waitPeriod: Int) = {
      val pb = Process(cmd, workingDir)

      val oS = new ByteArrayOutputStream
      val eS = new ByteArrayOutputStream
      println("timeout je: " + waitPeriod)
      val pio = new ProcessIO(
        in = { iSP =>
          val iS = new ByteArrayInputStream(input)
          transferFully(iS, iSP)
          iSP.close()
        }, out = transferFully(_, oS), err = transferFully(_, eS)
      )

      val process = pb run pio
      val unmonitor = after(waitPeriod)(process.destroy)
      val retcode = process.exitValue
      unmonitor()

      (retcode, oS.toByteArray, eS.toByteArray)
    }

    def after(time: Long)(cont: => Any): () => Boolean = {
      val t = soloThread {
        try { Thread sleep time; cont }
        catch { case _: InterruptedException => }
      }
      () => t.isAlive && { t.interrupt(); true }
    }

    def soloThread(wat: => Any) = {
      val t = new Thread(new Runnable {
        def run { wat }
      })
      t.start()
      t
    }
  }

}