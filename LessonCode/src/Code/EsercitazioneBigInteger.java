package Code;

import java.math.BigInteger;
import java.security.*;
public class EsercitazioneBigInteger {
public static void main(String []args) {
	
	BigInteger a=new BigInteger("3");
	BigInteger b=new BigInteger("6022838398484848382829384848382");
	

	System.out.println(a.add(b).toString());
	
	
	SecureRandom r=new SecureRandom();
	
	BigInteger p=new BigInteger(128,r);
	
	System.out.println(p.toString());
	
	
	System.out.println(a.modPow(b,p).toString());
	
	
	
}
}