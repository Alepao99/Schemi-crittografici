import java.math.*;
import java.util.*;
import java.security.*;
import java.io.*;

public class Schnorr {

	public static int isqr(BigInteger x, BigInteger p) {

		if (x.modPow(p.subtract(BigInteger.ONE).divide(BigInteger.TWO), p).compareTo(BigInteger.ONE) == 0)
			return 1;
		return 0;
	}

	public static SchnorrSK Setup(int securityparameter) {
		BigInteger p, q, g, h;

		SecureRandom sc = new SecureRandom();
		while (true) {
			q = BigInteger.probablePrime(securityparameter, sc);
			p = q.multiply(BigInteger.TWO).add(BigInteger.ONE);
			if (p.isProbablePrime(50) == true)
				break;
		}

		g = new BigInteger("2");

		while (true) {

			if (isqr(g, p) == 1)
				break;
			g = g.add(BigInteger.ONE);
		}

		BigInteger s = new BigInteger(securityparameter, sc);
		h = g.modPow(s, p);
		SchnorrPK PK = new SchnorrPK(p, q, g, h, securityparameter);

		return new SchnorrSK(s, PK);
	}

	public static BigInteger HashToBigInteger(SchnorrPK PK, BigInteger a, String M) {
		// Hash PK+a+M to a BigInteger
		String msg = PK.g.toString() + PK.h.toString() + a.toString() + M;
		try { // hash a String using MessageDigest class
			MessageDigest h = MessageDigest.getInstance("SHA256");
			h.update(Utils.toByteArray(msg));
			BigInteger e = new BigInteger(h.digest());

			return e.mod(PK.q);
		} catch (Exception E) {
			E.printStackTrace();
		}

		BigInteger e = new BigInteger("0");
		return e;
	}

	public static SchnorrSig Sign(SchnorrSK SK, String M)

	{
		SecureRandom sc = new SecureRandom(); // generate secure random source
		BigInteger r = new BigInteger(SK.PK.securityparameter, sc); // choose random r
		BigInteger a = SK.PK.g.modPow(r, SK.PK.p); // a=g^r mod p
		BigInteger e = HashToBigInteger(SK.PK, a, M); // e=H(PK,a,M)
		BigInteger z = r.add(e.multiply(SK.s).mod(SK.PK.q)).mod(SK.PK.q); // z=r+es mod q
		return new SchnorrSig(a, e, z); // (a,e,z) is the signature of M

	}

	public static boolean Verify(SchnorrSig sigma, SchnorrPK PK, String M) {
		// sigma is the triple (a,e,z), PK is the pair (g,h)
		BigInteger e2 = HashToBigInteger(PK, sigma.a, M); // e2=H(PK,a,M)
		// crucial that we use the hash computed by ourself and not the challenge e in the signature
		// actually the value e in the signature is NOT needed
		BigInteger tmp = sigma.a.multiply(PK.h.modPow(e2, PK.p)).mod(PK.p); // tmp=ah^e2
		if (tmp.compareTo(PK.g.modPow(sigma.z, PK.p)) == 0) // compare tmp with g^z mod p
			return true; 
		return false;
	}



	public static void main(String[] args) throws IOException {



		// Setup
		SchnorrSK SK = Setup(64);

		System.out.println("secretKey = " + SK.s);
		System.out.println("p = " + SK.PK.p);
		System.out.println("q = " + SK.PK.q);

		// Sign

		String M = "Ciao";
		SchnorrSig sigma = Sign(SK, M);
		System.out.println("Message = " + M);
		System.out.println("a = " + sigma.a);
		System.out.println("e = " + sigma.e);
		System.out.println("z = " + sigma.z);

		// Verify
		System.out.println("Verification = " + Verify(sigma, SK.PK, M));

	}
}