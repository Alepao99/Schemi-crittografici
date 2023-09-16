import java.math.BigInteger;
import java.security.SecureRandom;

public class SchnorrSK{
		BigInteger s;
		SchnorrPK PK;
		
		public SchnorrSK(BigInteger s,SchnorrPK PK) {
			this.s=s;
			this.PK=PK;
					
		}
		}