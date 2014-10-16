package hr.element.etb

import scalax.file.Path
import java.io.File

object Workspace {
  def apply(localPath: String): Workspace =
    apply(new File(localPath))

  def apply(file: File): Workspace =
    apply(Path(file))

  def apply(path: Path): Workspace =
    new Workspace(path)
}

class Workspace(val localPath: Path) {
  val path = localPath.toRealPath()
  val file = path.fileOption.get

  def /(child: String) = new Workspace(path / child)

  def install(): this.type = {
    path.createDirectory(true, false)
    this
  }

  def install(child: String): Workspace =
    /(child).install()

  def temporary[T](f: Workspace => T) = {
    val ws = install(XKCD.now.toString)
    try {
      f(ws)
    } finally {
      ws.uninstall()
    }
  }

  def uninstall() =
    path.deleteRecursively(true, true)._2 == 0

  override val toString = file.getPath
}
