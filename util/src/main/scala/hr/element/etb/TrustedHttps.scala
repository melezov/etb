package hr.element.etb

import java.security._
import javax.net.ssl._

import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.conn.scheme.Scheme

import dispatch._

class TrustedHttps(truststore: String, truststorePassword: String, ks: String = null, keystorePassword: String = null) extends Http {
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
 

  private lazy val keyManagers = { 
    val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm)
    val keystore = KeyStore.getInstance("PKCS12");
    val kS = classOf[TrustedHttps].getResourceAsStream(ks)
    keystore.load(kS, keystorePassword.toCharArray())
    kmf.init(keystore, keystorePassword.toCharArray())
    kmf.getKeyManagers
  }   

  private lazy val sslSocketFactory = { 
    val km = ks match {
      case null => null
      case _ => keyManagers
    }   
    val sslContext = javax.net.ssl.SSLContext.getInstance("TLS")
    sslContext.init(km, trustManagers, new SecureRandom())
    new SSLSocketFactory(sslContext)
  }
}

