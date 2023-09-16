package Code;
	//import java.Utils.*;
//import java.math.BigInteger;
import java.security.SecureRandom;
	import java.util.Arrays;



	public class EsercizioOTP {
	
	
		public static void main(String[] args) 
 {
			
			byte []message="ciao".getBytes();
			byte key[]=new byte[message.length];
		// Secure randomness is obtained via the SecureRandom class.
			SecureRandom r=new SecureRandom();
			
		/* uncomment this code if you want to get secure randomness from a "strong source".
		 *  See https://tersesystems.com/blog/2015/12/17/the-right-way-to-use-securerandom/
		 
			try {
		r=SecureRandom.getInstanceStrong();
			
			} catch(Exception e) {
			e.printStackTrace();	
			}
	*/		
			
			// the method netxtBytes fills the array given as input with random bytes
			// you can invoke this method an arbitrary number of times to get random bytes.
			// There are also methods like nextInt(bound) that return random integers from 0 to bound-1.
		
			 r.nextBytes(key);
			System.out.println("key: "+ Utils.toHex(key));
			
			byte encoded[]=new byte[message.length];
			System.out.println("message: "+ Utils.toHex(message));

			for (int i=0;i<message.length;i++)
			{
			encoded[i]= (byte) (message[i] ^ key[i]);
			}
			System.out.println("ciphertext: "+Utils.toHex(encoded));

			byte decoded[]=new byte[message.length];

			for (int i=0;i<message.length;i++)
			{
			decoded[i]= (byte) (encoded[i] ^ key[i]);
			}
			
			System.out.println("decoded: "+Utils.toHex(decoded)+" "+Arrays.equals(message,decoded));

			
		}
		
		}
