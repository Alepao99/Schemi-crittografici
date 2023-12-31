import java.math.*;
import java.util.*;
import java.security.*;
import java.io.*;

public class FermatTheorem
{
    public static void main(String[] args) throws IOException
    {
    	// compute c=g^{p-1}=1
        BigInteger g, c, p,one;
        Random sc = new SecureRandom();
        p = BigInteger.probablePrime(1024, sc);
        System.out.println("p = " + p);

        g = new BigInteger("5");
        one=new BigInteger("1");
        c = g.modPow(p.subtract(one),p);
        System.out.println("c = " + c);

    }
}