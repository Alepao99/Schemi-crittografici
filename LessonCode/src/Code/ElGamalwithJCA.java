
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import javax.crypto.Cipher;

import javax.crypto.spec.SecretKeySpec;

/**
 * 
 */
public class ElGamalwithJCA
{
  
    
    public static void main(
        String[]    args)
        throws Exception
    {
        byte[]           input = new byte[] { 0x00, 0x01, 0x02 };
        SecureRandom     random = new SecureRandom();
        
        KeyPairGenerator generator = KeyPairGenerator.getInstance("ELGamal"); // generator of the keys
       System.out.println(" "+ generator.getProvider());      
        generator.initialize(2048, random); // initialize with 2048 bits of security 
                // with a secure random source

        KeyPair          pair = generator.generateKeyPair(); // generate a pair of PK/SK 
        Key              pubKey = pair.getPublic(); // method getPublic returns PK
        Key              privKey = pair.getPrivate(); // getPrivate SK

        System.out.println("input            : " + Utils.toHex(input));
        
    
        Cipher	        cipher = Cipher.getInstance("ElGamal/None/PKCS1Padding"); 
        // cipher is for El Gamal with padding done internally by JCA
        
        cipher.init(Cipher.ENCRYPT_MODE, pubKey, random); // init cipher in encrypt mode with PK
      // random is used each time we encrypt to generate randomness for the encryption algorithm
        
        byte[]          cipherText = cipher.doFinal(input); 
        
        
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        
        
        
        byte[] plainText = cipher.doFinal(cipherText);
        
        System.out.println("decrypted plaintext: " + Utils.toHex(plainText));
    }
}