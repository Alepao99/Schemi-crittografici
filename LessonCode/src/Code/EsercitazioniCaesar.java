package Code;

public class EsercitazioniCaesar {
	public static void main (String [] args) {

        char[] letters = {
        	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 
            'w', 'x', 'y', 'z'};
 int shift=3;
 char message[]="zebra".toCharArray();
 char encoded[]=new char[message.length];
 char decoded[]=new char[message.length];
 
 for (int i=0;i<message.length;i++) {
 encoded[i]=  (char)  ( 'a'+((message[i]-'a'+shift) % 26) )  ;
 System.out.print(encoded[i]);
}
 System.out.print("\n");
 for (int i=0;i<message.length;i++) {
	 decoded[i]=  (char)  ( 'a'+ Math.floorMod(encoded[i]-'a'-shift , 26) )  ;
	 System.out.print(decoded[i]);
	}
 
 
	}
}
