import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
public class TamperedWithDigestExample
{   
	// show that just adding
    public static void main(
        String[]    args)
        throws Exception
    {
        SecureRandom	random = new SecureRandom();
        IvParameterSpec ivSpec = Utils.createCtrIvForAES(random);
        Key             key = Utils.createKeyForAES(128, random);
        Cipher          cipher = Cipher.getInstance("AES/CTR/NoPadding");
        String          input = "Transfer 0000100";
        MessageDigest   hash = MessageDigest.getInstance("SHA256");
        
        System.out.println("input : " + input);
        
        // encryption step
        
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        
        byte[] cipherText = new byte[cipher.getOutputSize(input.length() + hash.getDigestLength())];

        int ctLength = cipher.update(Utils.toByteArray(input), 0, input.length(), cipherText, 0);
        
        hash.update(Utils.toByteArray(input));
        byte []h=hash.digest();
        
        ctLength += cipher.doFinal(h, 0, hash.getDigestLength(), cipherText, ctLength);
        
        // tampering step
        
        cipherText[9] ^= '0' ^ '9';

    
        hash = MessageDigest.getInstance("SHA256");
        hash.update(Utils.toByteArray("Transfer 9000100"));
       byte[] h2=hash.digest();
        for (int i=16;i<16+hash.getDigestLength();i++) 
        	cipherText[i] ^= h[i-16] ^ h2[i-16];
  
        
        
        // decryption step
        
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        
        byte[] plainText = cipher.doFinal(cipherText, 0, ctLength);
        int    messageLength = plainText.length - hash.getDigestLength();
        
        hash.update(plainText, 0, messageLength);
        
        byte[] messageHash = new byte[hash.getDigestLength()];
        System.arraycopy(plainText, messageLength, messageHash, 0, messageHash.length);
        
        System.out.println("plain : " + Utils.toString(plainText, messageLength) + "\nverified: " + MessageDigest.isEqual(hash.digest(), messageHash));
    }
}
