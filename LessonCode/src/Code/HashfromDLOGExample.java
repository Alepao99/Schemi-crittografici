package Esercitazioni;

import java.math.BigInteger;
import java.security.*;
public class HashfromDLOGExample {
public static void main(String []args) {
	
	BigInteger p,q;
	SecureRandom r=new SecureRandom();
// example of computing the hash function from dlog.
	// this is done in a single main. In a real implementation you should have a constructor that setups the description of the hash function and a function to evaluate the hash function on some input
	
while(true) {		 
	q = BigInteger.probablePrime(512, r); 
	// method probablePrime returns a prime number of length securityparameter=512
	// using sc as random source.
	// 512 bits are not enough, use at least 2048 bits in a real implementation. this is done to run the test quickly
	
	p=q.multiply(BigInteger.TWO);
	p=p.add(BigInteger.ONE);  // p=2q+1
	
if (p.isProbablePrime(50)==true) break;		// returns an integer that is prime with prob.
// 1-2^-50

}
System.out.println("we work in Zp* with p="+p.toString());

BigInteger g=new BigInteger("4"); // g generator of the subgroup of order q of Zp*
BigInteger z = new BigInteger(512 , r);
z=z.mod(q);

BigInteger h=g.modPow(z, p); // h=g^z. Observe that p,g,h represents the description of the hash function. Finding a collison in the hash function accounts to find the dlog of h in base g, that is z


// from now we suppose that someone wants to hash a certain input a,b in which a is a random integer in Zq and b is the integer 17
BigInteger a = new BigInteger(512 , r);
a=a.mod(q);
BigInteger b=new BigInteger("17"); // a and b inputs to my hash function

//We compute the hash H=g^a*h^b mod p
BigInteger H;
H=g.modPow(a, p); // H=g^a mod p
H.multiply(h.modPow(b,p)); // H=H*h^b
H.mod(p); // H=H mod p
System.out.println("hash of a=" +a+ " and "+ "b=" + b+"="+H.toString());


	
	
}
}