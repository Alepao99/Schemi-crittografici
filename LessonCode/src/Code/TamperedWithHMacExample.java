import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Tampered message with HMac, encryption AES in CTR mode
 */
public class TamperedWithHMacExample
{   
    public static void main(
        String[]    args)
        throws Exception
    {
        SecureRandom	random = new SecureRandom();
        IvParameterSpec ivSpec = Utils.createCtrIvForAES(random);
        Key             key = Utils.createKeyForAES(128, random);
        Cipher          cipher = Cipher.getInstance("AES/CTR/NoPadding");
        String          input = "Transfer 0000100";
        Mac             hMac = Mac.getInstance("HMacSHA256");
     //   Key             hMacKey = new SecretKeySpec(key.getEncoded(), "HMacSHA256");
   
        KeyGenerator    generator = KeyGenerator.getInstance("HMacSHA256");
        generator.init(128);
  
        Key hMacKey = generator.generateKey();
        
        System.out.println("input : " + input);
        
        // encryption step
        
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        
        byte[] cipherText = new byte[cipher.getOutputSize(input.length() + hMac.getMacLength())];

        int ctLength = cipher.update(Utils.toByteArray(input), 0, input.length(), cipherText, 0);
        
        hMac.init(hMacKey);
       
        hMac.update(Utils.toByteArray(input));
        byte []h=new byte[hMac.getMacLength()];
        h=hMac.doFinal();
        ctLength += cipher.doFinal(h, 0, hMac.getMacLength(), cipherText, ctLength);
        System.out.println("hash : " +  Utils.toHex(h));

        
        // tampering step
        
        cipherText[9] ^= '0' ^ '9';
        
      
      
        // decryption step
        
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        
        byte[] plainText = cipher.doFinal(cipherText, 0, ctLength);
        int    messageLength = plainText.length - hMac.getMacLength();
        
        hMac.init(hMacKey);
        hMac.update(plainText, 0, messageLength);
        
        byte[] messageHash = new byte[hMac.getMacLength()];
        System.arraycopy(plainText, messageLength, messageHash, 0, messageHash.length);
        System.out.println("plain : " + Utils.toString(plainText, messageLength) + "\nrecomputed hash:"+ Utils.toHex(hMac.doFinal()));
    }
}
