package Code;


import java.math.*;
import java.util.*;
import java.security.*;
import java.io.*;

// example of Threshold El Gamal 
// Vincenzo Iovino
public class ThresholdElGamalProf extends ElGamal {

    public static class ElGamalCT {

        BigInteger C, C2;

        public ElGamalCT(BigInteger C, BigInteger C2) {
            this.C = C;
            this.C2 = C2;

        }

        public ElGamalCT(ElGamalCT CT) {
            this.C = CT.C;
            this.C2 = CT.C2;

        }

    }

    public static class ElGamalSK { // Sepublic class ElGamalCT{

        BigInteger s;
        // s is random BigInteger from 1 to q where q is the order of g (g is in the PK)

        ElGamalPK PK; // PK of El Gamal

        public ElGamalSK(BigInteger s, ElGamalPK PK) {
            this.s = s;
            this.PK = PK;

        }

    }

    public static class ElGamalPK {

        BigInteger g, h, p, q; // description of the group and public-key h=g^s
        int securityparameter; // security parameter

        public ElGamalPK(BigInteger p, BigInteger q, BigInteger g, BigInteger h, int securityparameter) {
            this.p = p;
            this.q = q;
            this.g = g;
            this.h = h;
            this.securityparameter = securityparameter;

        }
    }

    public class ElGamal {

        public static ElGamalSK Setup(int securityparameter) {
            BigInteger p, q, g, h;

            SecureRandom sc = new SecureRandom(); // create a secure random source

            while (true) {
                q = BigInteger.probablePrime(securityparameter, sc);
                // method probablePrime returns a prime number of length securityparameter
                // using sc as random source

                p = q.multiply(BigInteger.TWO);
                p = p.add(BigInteger.ONE);  // p=2q+1

                if (p.isProbablePrime(50) == true) {
                    break;		// returns an integer that is prime with prob.
                }// 1-2^-50

            }
// henceforth we have that p and q are both prime numbers and p=2q+1
// Subgroups of Zp* have order 2,q,2q

            g = new BigInteger("4"); // 4 is quadratic residue so it generates a group of order q
// g is a generator of the subgroup the QR modulo p
// in particular g generates q elements where q is prime

            BigInteger s = new BigInteger(securityparameter, sc); // s is the secret-key
            h = g.modPow(s, p); // h=g^s mod p

            ElGamalPK PK = new ElGamalPK(p, q, g, h, securityparameter);

            return new ElGamalSK(s, PK);
        }

        public static ElGamalCT Encrypt(ElGamalPK PK, BigInteger M) {
            SecureRandom sc = new SecureRandom(); // create a secure random source

            BigInteger r = new BigInteger(PK.securityparameter, sc); // choose random r of lenght security parameter
            // C=[h^r*M mod p, g^r mod p].

            BigInteger C = M.multiply(PK.h.modPow(r, PK.p)); // C=M*(h^r mod p)
            C = C.mod(PK.p); // C=C mod p
            BigInteger C2 = PK.g.modPow(r, PK.p);  // C2=g^r mod p
            return new ElGamalCT(C, C2);   // return CT=(C,C2)

        }

        public static ElGamalCT EncryptInTheExponent(ElGamalPK PK, BigInteger m) {
            // identical to Encrypt except that input is an exponent m and encrypts M=g^m mod p

            SecureRandom sc = new SecureRandom();
            BigInteger M = PK.g.modPow(m, PK.p); // M=g^m mod p
            BigInteger r = new BigInteger(PK.securityparameter, sc);
            BigInteger C = M.multiply(PK.h.modPow(r, PK.p)).mod(PK.p);
            BigInteger C2 = PK.g.modPow(r, PK.p);
            return new ElGamalCT(C, C2);

        }

        public static BigInteger Decrypt(ElGamalCT CT, ElGamalSK SK) {
            // C=[C,C2]=[h^r*M mod p, g^r mod p].
            // h=g^s mod p

            BigInteger tmp = CT.C2.modPow(SK.s, SK.PK.p);  // tmp=C2^s mod p
            tmp = tmp.modInverse(SK.PK.p);
            // if tmp and p are BigInteger tmp.modInverse(p) is the integer x s.t. 
            // tmp*x=1 mod p
            // thus tmp=C2^{-s}=g^{-rs} mod p =h^{-r}

            BigInteger M = tmp.multiply(CT.C).mod(SK.PK.p); // M=tmp*C mod p
            return M;

        }

        public static BigInteger DecryptInTheExponent(ElGamalCT CT, ElGamalSK SK) {
            BigInteger tmp = CT.C2.modPow(SK.s, SK.PK.p).modInverse(SK.PK.p);
            BigInteger res = tmp.multiply(CT.C).mod(SK.PK.p);
            // after this step res=g^d for some d in 1,...,q

            BigInteger M = new BigInteger("-6");
            while (true) {
                if (SK.PK.g.modPow(M, SK.PK.p).compareTo(res) == 0) {
                    return M;
                }
// if g^M=res stop and return M
// otherwise M++
                M = M.add(BigInteger.ONE);
            }

        }

        public static ElGamalCT Homomorphism(ElGamalPK PK, ElGamalCT CT1, ElGamalCT CT2) {
            ElGamalCT CT = new ElGamalCT(CT1); // CT=CT1
            CT.C = CT.C.multiply(CT2.C).mod(PK.p);  // CT.C=CT.C*CT2.C mod p
            CT.C2 = CT.C2.multiply(CT2.C2).mod(PK.p); // CT.C2=CT.C2*CT2.C2 mod p
            return CT; // If CT1 encrypts m1 and CT2 encrypts m2 then CT encrypts m1+m2

        }
    }

    public static ElGamalSK Setup(ElGamalSK SK) { // computes a partial public key 
        // given the public parameters taken as input in SK

        SecureRandom sc = new SecureRandom();
        BigInteger s = new BigInteger(SK.PK.securityparameter, sc); // i-th authority has s_i
        BigInteger h = SK.PK.g.modPow(s, SK.PK.p); // and h_i=g^{s_i}

        ElGamalPK PK = new ElGamalPK(SK.PK.p, SK.PK.q, SK.PK.g, h, SK.PK.securityparameter); //
// return the partial public key of i-th authority
        return new ElGamalSK(s, PK);
    }

    public static ElGamalSK SetupParameters(int securityparameter) {
        // since the authorities should work over the same group Zp* we 
        // need to define a SetupParameters method that  computes the public parameters p,q,g shared
        //  by all authorities.
        // For  compatibility with the our structures we compute but ignore h,s.
        BigInteger p, q, g, h, s;

        SecureRandom sc = new SecureRandom();

        while (true) {
            q = BigInteger.probablePrime(securityparameter, sc);

            p = q.multiply(BigInteger.TWO);
            p = p.add(BigInteger.ONE);

            if (p.isProbablePrime(50) == true) {
                break;
            }

        }

        g = new BigInteger("4");
        /*
while(true) {
	
	if (isqr(g,p)==1)break; 
	g=g.add(BigInteger.ONE);
}
         */
        s = BigInteger.ZERO;
        h = BigInteger.ZERO;

        ElGamalPK PK = new ElGamalPK(p, q, g, h, securityparameter);

        return new ElGamalSK(s, PK);
    }

    public static ElGamalPK AggregatePartialPublicKeys(ElGamalPK PK[]) {

        BigInteger tmp = BigInteger.ONE;
        // the array PK contains the partial public keys of the m-authorities
        // in particular PK[i].h=h_i=g^{s_i}

        for (int i = 0; i < PK.length; i++) {
            tmp = tmp.multiply(PK[i].h).mod(PK[0].p);
        }
        // here tmp=\Prod_{i=1}^m h_i
        // therefore tmp is the General public key h
        return new ElGamalPK(PK[0].p, PK[0].q, PK[0].g, tmp, PK[0].securityparameter);

    }

    public static ElGamalCT PartialDecrypt(ElGamalCT CT, ElGamalSK SK) {
        // CT is the ciphertext to decrypt or a ciphertext resulting from a partial decryption
        // Suppose SK is the key of the i-th authority. Then SK.s is s_i
        BigInteger tmp = CT.C2.modPow(SK.s, SK.PK.p); // tmp=C2^s_i 
        tmp = tmp.modInverse(SK.PK.p);   // tmp=C2^{-s_i}
        BigInteger newC = tmp.multiply(CT.C).mod(SK.PK.p); // newC=C*tmp=(h^r*M)*C2^{-s_i}=h^r*M*g^{-rs_i}

        return new ElGamalCT(newC, CT.C2);
    }

    public static void main(String[] args) throws IOException {

        SecureRandom sc = new SecureRandom();

        ElGamalSK Params = SetupParameters(64); // in real implementation set the security parameter to at least 2048 bits
        //there is some non-trusted entity that generates the parameters

        // we now suppose there are 3 authorities
        ElGamalSK[] SK = new ElGamalSK[3];
        BigInteger m1 = new BigInteger("1");
        //String msg1 = Ts + ";" + m1;
        //u2 vota
        BigInteger m2 = new BigInteger("-1");
        BigInteger m3 = new BigInteger("-1");
        LinkedList<BigInteger> listb = new LinkedList<>();
        listb.add(m1);
        listb.add(m2);
        listb.add(m3);

        for (int i = 0; i < 3; i++) {
            SK[i] = Setup(Params); // we assume we have m-authorities and they use the parameters generated
            // before to compute 3 partial secret key.
            // Sk[i].s=s_i
            // SK[i].PK.h=h_i=g^s_i

            System.out.println("Setup for " + i + "-th authority:");
            System.out.println("partial secret-key = " + SK[i].s);
            System.out.println("partial public-key = " + SK[i].PK.h);
            System.out.println("p = " + SK[i].PK.p);
            System.out.println("q = " + SK[i].PK.q);
            System.out.println("g = " + SK[i].PK.g);

        }

        ElGamalPK[] PartialPK = new ElGamalPK[3];

        for (int i = 0; i < 3; i++) {
            PartialPK[i] = SK[i].PK;
        }
        ElGamalPK PK = AggregatePartialPublicKeys(PartialPK);
        ElGamalCT c1 = ElGamal.EncryptInTheExponent(PK, m1); // encrypt vote in CT

        //Cifro m2
        ElGamalCT c2 = ElGamal.EncryptInTheExponent(PK, m2);

        ElGamalCT c3 = ElGamal.EncryptInTheExponent(PK, m3);

        LinkedList<ElGamalCT> list = new LinkedList<>();
        list.add(c1);
        list.add(c2);
        list.add(c3);
// from the 3 partial public-keys SK[i].PK=h_i compute
// the general public-key PK=\prod_{i=1}^3 h_i=h

        ElGamalCT CTH = list.get(0);
        for (int i = 1; i < 3; i++) {

            CTH = ElGamal.Homomorphism(PK, CTH, list.get(i));

        }

        ElGamalCT PartialDecCT = CTH;
        for (int i = 0; i < 2; i++) {
            PartialDecCT = PartialDecrypt(PartialDecCT, SK[i]);
        }
System.out.println("ok");
        // the first 2 authorities  use PartialDecrypt to decrypt partial ciphertexts with respect to their partial secret-keys
        BigInteger D = ElGamal.DecryptInTheExponent(PartialDecCT, SK[2]); // finally the third authority
        // uses the standard decryption procedure to recover the message
        System.out.println("decrypted plaintext with threshold El Gamal = " + D + "\n"); // it should print the same integer as before    }
    }
}
