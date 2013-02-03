package hr.element.etb

import java.security._
import javax.net.ssl._

import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.conn.scheme.Scheme

import dispatch.classic._

object HttpsConfig extends HttpsConfig(None, None, None, None) {
  case class Store(path: String, password: String)
}

import HttpsConfig._

case class HttpsConfig protected(
    truststore: Option[Store]
  , keystore: Option[Store]
  , connectionTimeout: Option[Int]
  , readTimeout: Option[Int]) {

  def setTruststore(path: String, password: String) =
    copy(truststore = Some(Store(path, password)))

  def removeTruststore() =
    copy(truststore = None)

  def setKeystore(path: String, password: String) =
    copy(keystore = Some(Store(path, password)))

  def removeKeystore() =
    copy(keystore = None)

  def setConnectionTimeout(t: Int) =
    copy(connectionTimeout = Some(t))

  def removeConnectionTimeout() =
    copy(connectionTimeout = None)

  def setReadTimeout(t: Int) =
    copy(readTimeout = Some(t))

  def removeReadTimeout() =
    copy(readTimeout = None)

// -----------------------------------------------------------------------------

  private lazy val trustManagers = {
    for (t <- truststore) yield {
      val factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
      val tI = KeyStore.getInstance(KeyStore.getDefaultType)
      val tS = classOf[TrustedHttps].getResourceAsStream(t.path)
      val tP = t.password.toCharArray()
      tI.load(tS, tP)
      factory.init(tI)
      factory.getTrustManagers
    }
  }

  private lazy val keyManagers = {
    for (k <- keystore) yield {
      val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm)
      val kI = KeyStore.getInstance("PKCS12");
      val kS = classOf[TrustedHttps].getResourceAsStream(k.path)
      val kP = k.password.toCharArray()
      kI.load(kS, kP)
      kmf.init(kI, kP)
      kmf.getKeyManagers
    }
  }

  private lazy val sslSocketFactory = {
    val sslContext = javax.net.ssl.SSLContext.getInstance("TLS")

    sslContext.init(
      keyManagers.getOrElse(null)
    , trustManagers.getOrElse(null)
    , new SecureRandom()
    )

    new SSLSocketFactory(sslContext)
  }

// -----------------------------------------------------------------------------

  def apply() = new TrustedHttps()

  class TrustedHttps private[HttpsConfig]() extends Http {
    client.getConnectionManager.getSchemeRegistry.register(
      new Scheme("https", 443, sslSocketFactory)
    )

    import org.apache.http.params.CoreConnectionPNames._
    connectionTimeout.foreach(client.getParams.setParameter(CONNECTION_TIMEOUT, _))
    readTimeout.foreach(client.getParams.setParameter(SO_TIMEOUT, _))
  }
}
