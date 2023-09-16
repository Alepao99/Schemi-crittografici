import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.GCMParameterSpec;


public class AES_GCM {
public static void main(String[] args) throws Exception {
	 
		        byte[]        input = new byte[] { 
		                0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 
		                (byte)0x88, (byte)0x99, (byte) 0xaa, (byte)0xbb,
		                (byte)0xcc, (byte)0xdd, (byte)0xee, (byte)0xff };
		        
		        byte[]        keyBytes = new byte[] { 
		                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
		                0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e,0x0a
		                }; // recall that we set the key to constant bytes for simplicity and to have a deterministic behavior. This is NOT secure. 
		        // Use SecureRandom to fill this array with random bytes in a real implementation
		        
		        byte[]        IV = new byte[] { 
		                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
		                0x08, 0x09, 0x0a, 0x0b
		                }; // IV needs to be 12-bytes long - also fill this array with random bytes
		        
		        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		        

		        Cipher        cipher = Cipher.getInstance("AES/GCM/NoPadding");
		        

		        System.out.println("input text : " + Utils.toHex(input));
		        
		        SecureRandom random=new SecureRandom();
		        GCMParameterSpec spec = new GCMParameterSpec(128,IV);
		        // in AES GCM the ciphertexts 	  have an additional tag parameter length - in this case set to 128

		        cipher.init(Cipher.ENCRYPT_MODE, key,spec);
		        
		        
		        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];


 int ctLength = cipher.update(input, 0, input.length, cipherText, 0);


		   
		        ctLength += cipher.doFinal(cipherText, ctLength);
		        
		        
		        System.out.println("cipher text: " + Utils.toHex(cipherText) + " bytes: " + ctLength);
		        
		        
		        byte[] plainText = new byte[ctLength];
		        
		      // cipherText[0]=0;
		        spec = new GCMParameterSpec(128, cipher.getIV()); 

		        cipher.init(Cipher.DECRYPT_MODE, key,spec);

		        int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
		        
		        ptLength += cipher.doFinal(plainText, ptLength);
		        
		        System.out.println("plain text : " + Utils.toHex(plainText) + " bytes: " + ptLength);
} }