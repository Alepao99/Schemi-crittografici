import java.math.BigInteger;
import java.security.SecureRandom;

public class SchnorrSig{
		BigInteger a,e,z;
		
		public SchnorrSig(BigInteger a,BigInteger e,BigInteger z) {
			this.a=a;
			this.e=e;
			this.z=z;
		}
		}