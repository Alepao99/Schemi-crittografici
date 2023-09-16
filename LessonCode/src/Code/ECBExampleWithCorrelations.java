package Code;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
/**
* Basic symmetric encryption example with padding and ECB using DES
*/
public class ECBExampleWithCorrelations
{
public static void main(String[] args) throws Exception
{

byte[] keyBytes = new byte[] {
0x01, 0x23, 0x45, 0x67,
(byte)0x89, (byte)0xab, (byte)0xcd, (byte)0xef };
// In this example we show that the last DES block of the ciphertext encrypting input is equal to the second block. This shows that in ECB there are repetitions and so ECB is not safe
// when there are correlations in the plaintext
byte []input=("Hi Miss "+"Jennifer"+", I'll call you in the morning. "+"Jennifer").getBytes();
byte []input2=("Hi Mr. "+"Johnatan"+",  I'll call you in the morning. "+"Jennifer").getBytes();

SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");

Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
System.out.println("input : " + input.length+ " "+Utils.toHex(input));
// encryption pass
cipher.init(Cipher.ENCRYPT_MODE, key);
byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
ctLength += cipher.doFinal(cipherText, ctLength);
System.out.println("cipher: " + Utils.toHex(cipherText, ctLength)
+ " bytes: " + ctLength+" "+cipher.getOutputSize(ctLength));
// decryption pass
cipher.init(Cipher.DECRYPT_MODE, key);
byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
ptLength += cipher.doFinal(plainText, ptLength);
System.out.println("plain : " + Utils.toHex(plainText, ptLength)
+ " bytes: " + ptLength);









System.out.println("\ninput2 : " + input2.length+ " "+Utils.toHex(input2));
Cipher cipher2 = Cipher.getInstance("DES/ECB/NoPadding");

cipher2.init(Cipher.ENCRYPT_MODE, key);
 cipherText = new byte[cipher2.getOutputSize(input2.length)];
 ctLength = cipher2.update(input2, 0, input2.length, cipherText, 0);
ctLength += cipher2.doFinal(cipherText, ctLength);
System.out.println("cipher: " + Utils.toHex(cipherText, ctLength)
+ " bytes: " + ctLength+" "+cipher.getOutputSize(ctLength));
// decryption pass
cipher2.init(Cipher.DECRYPT_MODE, key);
plainText = new byte[cipher2.getOutputSize(ctLength)];
 ptLength = cipher2.update(cipherText, 0, ctLength, plainText, 0);
ptLength += cipher2.doFinal(plainText, ptLength);
System.out.println("plain : " + Utils.toHex(plainText, ptLength)
+ " bytes: " + ptLength);




}
}