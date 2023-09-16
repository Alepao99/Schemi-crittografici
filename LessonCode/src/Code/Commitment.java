	public class Commitment{
		public byte []commitment;
		public byte[] randomness;
		public String msg;
		public Commitment(byte []c,byte[]r,String m) {
		this.commitment=c;
		this.randomness=r;
		this.msg=m;
		}

		}
