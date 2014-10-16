package hr.element.etb.lift.lib

import net.liftweb.http.InMemoryResponse

import hr.element.onebyseven.common.MimeType.{ values => mimes }

object MimeType {
  val Default = "application/octet-stream"

  def fromFileName(fileName: String) =
    fileName lastIndexOf '.' match {
      case x if x > 0 =>
        val ext = fileName substring (x + 1) toLowerCase;
        mimes find (_.extension == ext) map (_.mimeType) getOrElse (Default)

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
