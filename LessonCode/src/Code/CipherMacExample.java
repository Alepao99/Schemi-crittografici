
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *  MAC (HMAC) then Encrypt example - encryption AES in CTR mode
 */
public class CipherMacExample
{   
    public static void main(
        String[]    args)
        throws Exception
    {
       
    	
    	SecureRandom	random = new SecureRandom();
        IvParameterSpec ivSpec = Utils.createCtrIvForAES(random);
        Key             key = Utils.createKeyForAES(256, random);
        Cipher          cipher = Cipher.getInstance("AES/CTR/NoPadding");
       
        String          input = "Transfer 0000100";
        Mac             mac = Mac.getInstance("HMacSHA256");
        byte[]          macKeyBytes = new byte[] {  // Note: in a real implementation this has to be changed so that the key bytes are chosen at random
        		0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
        		0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,0x08,
        		0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
        		0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,0x08
        		
        		
        		};
        Key             macKey = new SecretKeySpec(macKeyBytes, "HMacSHA256");
        
        System.out.println("input : " + input);
        
        // encryption step
        
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        
        byte[] cipherText = new byte[cipher.getOutputSize(input.length() + mac.getMacLength())];

        int ctLength = cipher.update(Utils.toByteArray(input), 0, input.length(), cipherText, 0);
        
        mac.init(macKey);
        mac.update(Utils.toByteArray(input));
        
        ctLength += cipher.doFinal(mac.doFinal(), 0, mac.getMacLength(), cipherText, ctLength);
        
        // decryption step
        
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        
        byte[] plainText = cipher.doFinal(cipherText, 0, ctLength);
        int    messageLength = plainText.length - mac.getMacLength();
        
        mac.init(macKey);
        mac.update(plainText, 0, messageLength);
        
        byte[] messageHash = new byte[mac.getMacLength()];
        System.arraycopy(plainText, messageLength, messageHash, 0, messageHash.length);
        
        System.out.println("plain : " + Utils.toString(plainText, messageLength) + " verified: " + MessageDigest.isEqual(mac.doFinal(), messageHash));
        
    }
}
