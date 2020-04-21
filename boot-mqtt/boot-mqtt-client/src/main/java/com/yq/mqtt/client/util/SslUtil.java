package com.yq.mqtt.client.util;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Ssl 工具类
 */
public class SslUtil {

    /**
     * 创建 SocketFactory
     * @param caCrtFile ca证书文件
     * @param crtFile   客户端证书文件
     * @param keyFile   客户端私钥文件
     * @param password  客户端密码
     * @return SSLSocketFactory
     */
    public static SSLSocketFactory createSocketFactory(final String caCrtFile, final String crtFile, final String keyFile,
                                                       final String password) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException, PKCSException, OperatorCreationException {
        Security.addProvider(new BouncyCastleProvider());

        // load CA certificate
        PEMParser reader = new PEMParser(new FileReader(caCrtFile));
        X509CertificateHolder caCert = (X509CertificateHolder) reader.readObject();
        reader.close();

        // load client certificate
        reader = new PEMParser(new FileReader(crtFile));
        X509CertificateHolder cert = (X509CertificateHolder) reader.readObject();
        reader.close();

        // load client private key
        PEMParser pemParser = new PEMParser(new FileReader(keyFile));
        Object object = pemParser.readObject();

        InputDecryptorProvider decProv = new JceOpenSSLPKCS8DecryptorProviderBuilder().build(password.toCharArray());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

        PrivateKey privateKey;
        if (object instanceof PKCS8EncryptedPrivateKeyInfo) {
            privateKey = converter.getPrivateKey(((PKCS8EncryptedPrivateKeyInfo) object).decryptPrivateKeyInfo(decProv));
        } else {
            privateKey = converter.getPrivateKey((PrivateKeyInfo) object);
        }

        // CA certificate is used to authenticate server
        JcaX509CertificateConverter convert = new JcaX509CertificateConverter();
        KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
        caKs.load(null, null);
        caKs.setCertificateEntry("ca-certificate", convert.getCertificate(caCert));
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(caKs);

        // client key and certificates are sent to server so it can authenticate us
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        ks.setCertificateEntry("certificate", convert.getCertificate(cert));
        ks.setKeyEntry("private-key", privateKey, password.toCharArray(), new java.security.cert.Certificate[]{convert.getCertificate(cert)});
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, password.toCharArray());

        // finally, create SSL socket factory
        SSLContext context = SSLContext.getInstance("TLSv1");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }

}
