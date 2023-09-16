import java.io.*;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;



public class AnalyzeSelfCertificateAndSign
{
    public static void main(String[] args)
        throws Exception
    {
       // the purpose of this program is:
       // 1) analyze a self-certificate (sslserver.cer) and verify the certificate with
    	// respect to  itself 
   // 2) use the private key of this self-signed certificate to sign the certificate and verify the signature
    	String currentPath = new java.io.File(".").getCanonicalPath();
     System.out.println("Current dir:" + currentPath);

        
        InputStream in = new FileInputStream("sslserver.cer");
        
        // create the certificate factory 
        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        
        // read the certificate
        X509Certificate    x509Cert = (X509Certificate)fact.generateCertificate(in);
       
        in = new FileInputStream("sslserver.cer"); // we do it again supposing that the x509CACert contains the certificate of the parent
        X509Certificate    x509CACert = (X509Certificate)fact.generateCertificate(in); 

        //      Certificate ::= SEQUENCE { tbsCertificate TBSCertificate, signatureAlgorithm AlgorithmIdentifier, signatureValue BIT STRING }

        
        System.out.println("sign. algorithm used to sign the certificate:"+x509Cert.getSigAlgName());
        System.out.println("sign. algorithm used for the PK:"+x509Cert.getPublicKey().getAlgorithm());
        System.out.println("sign. algorithm used to sign the parent certificate:"+x509CACert.getSigAlgName());
        System.out.println("sign. algorithm used for the parent PK:"+x509CACert.getPublicKey().getAlgorithm());

        try { 
        	
        	// x59cert contains a signature sigma and a string T
        x509Cert.verify(x509CACert.getPublicKey()); // implicitly verify sigm$a and T with PK=x509CACert.getPublicKey()
        } catch(Exception e) {
        e.printStackTrace();
        }
            System.out.println("if not exception signature is verified");
        
        
        // here we try to verify the certificates's signature with the class Signature (rather than using the .verify method of x509Certificate.
            // Observe that the part of the certificate that is actually signed is given by the method .getTBCertificate() 
        Signature signature=Signature.getInstance(x509Cert.getSigAlgName()); 
        signature.initVerify(x509CACert.getPublicKey());
        signature.update(x509Cert.getTBSCertificate());
        System.out.println("is signature verified? "+ signature.verify(x509CACert.getSignature()));


	// here we load from the keystore keystore.jks the private key identified by alias ssltest (this is supposed to be the private key corresponding to the self-signed certificate) - here we suppose that the keystore is protected by password changeit
 KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            FileInputStream file;
            PrivateKey privateKey = null;
            try {
                file = new FileInputStream("keystore.jks");
                keyStore.load(file, "changeit".toCharArray());
                 privateKey = (PrivateKey) keyStore.getKey("ssltest", "changeit".toCharArray());
                System.out.println("privateKey: "+privateKey);
            } catch (Exception e) { 


    }
    // now we use the private key loaded before to sign the Certificate and we verify the signature
        Signature signature2=Signature.getInstance(x509Cert.getSigAlgName()); 
        signature2.initSign(privateKey);
        signature2.update(x509Cert.getTBSCertificate());
	byte []sig=signature2.sign();
        Signature signature3=Signature.getInstance(x509Cert.getSigAlgName()); 
        signature3.initVerify(x509CACert.getPublicKey());
        signature3.update(x509Cert.getTBSCertificate());
        System.out.println("is signature verified? "+ signature3.verify(sig));
}

}
