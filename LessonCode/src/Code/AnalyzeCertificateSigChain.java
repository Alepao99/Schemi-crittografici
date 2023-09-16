
import java.io.*;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class AnalyzeCertificateSigChain {

    public static void main(String[] args)
            throws Exception {
        // the purpose of this program is to analyze Wikipedia Certificate (wikipedia.cer) and verify the certificate with
        // respect to  its parent certificate (cawikipedia.cer)
        // assume wikipedia.cer contains the certificate of wikipedia's website and cawikipedia.cer the intermediate CA that signed the wikipedia's certificate

        String currentPath = new java.io.File(".").getCanonicalPath();
        System.out.println("Current dir:" + currentPath);

        InputStream in = new FileInputStream("wikipedia.cer");

        // create the certificate factory 
        CertificateFactory fact = CertificateFactory.getInstance("X.509");

        // read the certificate
        X509Certificate x509Cert = (X509Certificate) fact.generateCertificate(in);
        // cawikipedia is the certificate of the authority that signed wikipedia.cer, that is the certificate
        // of wikipedia
        in = new FileInputStream("wikipediaca.cer");
        X509Certificate x509CACert = (X509Certificate) fact.generateCertificate(in); // certificate of the parent

        //     Certificate ::= SEQUENCE { tbsCertificate TBSCertificate, signatureAlgorithm AlgorithmIdentifier, signatureValue BIT STRING }
        System.out.println("sign. algorithm used to sign the certificate:" + x509Cert.getSigAlgName());
        System.out.println("sign. algorithm used for the PK:" + x509Cert.getPublicKey().getAlgorithm());
        System.out.println("sign. algorithm used to sign the parent certificate:" + x509CACert.getSigAlgName());
        System.out.println("sign. algorithm used for the parent PK:" + x509CACert.getPublicKey().getAlgorithm());

        try {
            // x509Cert is the certificate of wikipedia (contained in the file wikipedia.cer - this is the certificate
            // that wikipedia.org sends to browsers)
            // We want to verify that this certificate has been signed by a trusted authority, in particular
            // from the authority whose certificate is cawikipedia.cer
            // wikipedia.cer={ Certificate_of_Wikipedia, sigma=Sign(Certificate_of_Wikipedia,SK_CAwikipedia)
            // PK corresponding to SK_CAwikipedia is in the file cawikipedia.cer

            // x59cert contains a signature sigma and a string T
            x509Cert.verify(x509CACert.getPublicKey()); // implicitly verify sigma and T with PK=x509CACert.getPublicKey((
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("if not exception signature is verified");

        // here we try to verify the certificates's signature with the class Signature (rather than using the .verify method of x509Certificate.
        // Observe that the part of the certificate that is actually signed is given by the method .getTBCertificate() 
        Signature signature = Signature.getInstance(x509Cert.getSigAlgName()); // Signature
        // the wikipedia certificate is for SHA256withRSA
        signature.initVerify(x509CACert.getPublicKey());
        signature.update(x509Cert.getTBSCertificate());
        System.out.println("is signature verified? " + signature.verify(x509Cert.getSignature()));

    }
}
