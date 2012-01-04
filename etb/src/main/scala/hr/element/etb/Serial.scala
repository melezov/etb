package hr.element.etb

import java.security.MessageDigest

object Serial{
  def hash(x: String) = {
    val sha = MessageDigest.getInstance("SHA-1").digest(x.getBytes("UTF-8"))
    val uid = BigInt(sha take 8).toLong formatted "0x%08XL"
    "@serializable @SerialVersionUID(" + uid + ") // " + x
  }
}


