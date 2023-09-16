
import java.security.SecureRandom;
import java.security.MessageDigest;

public class CommitmentFromHash {
	 static SecureRandom sc=new SecureRandom();
	
	public static Commitment Com(String msg) 

	{
		byte[]r=new byte[32];
//		(new SecureRandom()).nextBytes(r);
	sc.nextBytes(r);
		try {
MessageDigest h= MessageDigest.getInstance("SHA256");
h.update(Utils.toByteArray(Utils.toString(r)+msg));
Commitment C=new Commitment(h.digest(),r,msg);
return C;

		} catch(Exception e) {
			e.printStackTrace();	
			}
		return null;
	
	}
	
	
	public static boolean ComVerify(byte []com,Commitment C)
	{
		try {
			MessageDigest h= MessageDigest.getInstance("SHA256");
			h.update(Utils.toByteArray(Utils.toString(C.randomness)+C.msg));
			return h.isEqual(com, h.digest());

					} catch(Exception e) {
						e.printStackTrace();	
						}
		
		
		return false;
	}


	public static void main(String[] args)
	        throws Exception

	{
		
	
		// TODO Auto-generated method stub
	Commitment C=Com("YES");
	System.out.println(ComVerify(C.commitment,C));
	C.commitment=Utils.toByteArray("NO");
	System.out.println(ComVerify(C.commitment,C));

	}

}
