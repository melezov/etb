package hr.element.etb

import java.security._
import javax.net.ssl._

import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.conn.scheme.Scheme

import dispatch._

class TrustedHttps(truststore: String, truststorePassword: String) extends Http {
  client.getConnectionManager.getSchemeRegistry.register(
    new Scheme("https", 443, sslSocketFactory)
  )

  private lazy val trustManagers = {
    val factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
    val keystore = KeyStore.getInstance(KeyStore.getDefaultType)
    val kS = classOf[TrustedHttps].getResourceAsStream(truststore)
    keystore.load(kS, truststorePassword.toCharArray())
    factory.init(keystore)
    factory.getTrustManagers
  }

  private lazy val sslSocketFactory = {
    val sslContext = javax.net.ssl.SSLContext.getInstance("TLS")
    sslContext.init(null, trustManagers, new SecureRandom())
    new SSLSocketFactory(sslContext)
  }
}
