package hr.element.etb

import java.security._
import javax.net.ssl._

import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.conn.scheme.Scheme

import dispatch._

class TrustedHttps(truststore: String, truststorePassword: String, ks: Option[String] = None, keystorePassword: Option[String] = None, Timeout: Option[Int] = None) extends Http {
  client.getConnectionManager.getSchemeRegistry.register(
    new Scheme("https", 443, sslSocketFactory)
  )

  Timeout match {
    case None => // Do nothing
    case Some(x) => {
      val params = client.getParams
      import org.apache.http.params.CoreConnectionPNames._
      params.setParameter(CONNECTION_TIMEOUT, x)
      params.setParameter(SO_TIMEOUT, x)
    }
  }
   
  private lazy val trustManagers = { 
    val factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
    val keystore = KeyStore.getInstance(KeyStore.getDefaultType)
    val kS = classOf[TrustedHttps].getResourceAsStream(truststore)
    keystore.load(kS, truststorePassword.toCharArray())
    factory.init(keystore)
    factory.getTrustManagers
  }
 
  private def keyManagers(k: String, p: String) = { 
    val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm)
    val keystore = KeyStore.getInstance("PKCS12");
    val kS = classOf[TrustedHttps].getResourceAsStream(k)
    keystore.load(kS, p.toCharArray())
    kmf.init(keystore, p.toCharArray())
    kmf.getKeyManagers
  }   

  private lazy val sslSocketFactory = { 
    val km = (ks,keystorePassword) match {
      case (None, None) => null
      case (Some(k),Some(p)) => keyManagers(k,p)
    }   
    val sslContext = javax.net.ssl.SSLContext.getInstance("TLS")
    sslContext.init(km, trustManagers, new SecureRandom())
    new SSLSocketFactory(sslContext)
  }
}

