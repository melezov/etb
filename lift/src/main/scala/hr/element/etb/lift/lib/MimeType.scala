package hr.element.etb.lift.lib

import net.liftweb.http.InMemoryResponse

object MimeType {
  val map = Map(
    "ico" -> "image/vnd.microsoft.icon",
    "png" -> "image/png",
    "txt" -> "text/plain",
    "dll" -> "application/x-msdownload",
    "zip" -> "application/x-compressed",
    "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    "pdf" -> "application/pdf",
    "jpg" -> "image/jpeg"
  )

  def fromFileName(fileName: String) =
    fileName lastIndexOf '.' match {
      case x if x > 0 =>
        map(fileName substring (x + 1))
      case _ =>
        "application/octet-stream"
    }
}

object BinaryResponse {
  def apply(body: Array[Byte], fileName: String, inline: Boolean) = {
    val disposition = if (inline) {
      "inline"
    } else {
      "attachment; filename=\"" + fileName + "\""
    }

    val headers = List(
      ("Content-Transfer-Encoding", "binary"),
      ("Content-Length", body.length.toString),
      ("Content-Type", MimeType.fromFileName(fileName)),
      ("Content-Disposition", disposition)
    )

    InMemoryResponse(body, headers, Nil, 200)
  }
}

object InlineResponse {
  def apply(body: Array[Byte], fileName: String) =
    BinaryResponse(body, fileName, true)
}

object AttachmentResponse {
  def apply(body: Array[Byte], fileName: String) =
    BinaryResponse(body, fileName, false)
}
