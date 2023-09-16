package Code;

import java.security.Security;
import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class PrecedenceTest {

    public static void main(String[] args) {
        String providerName = "BC"; // change this to SUN to test if the standard SUN provider is installed

// try to uncomment this for other IDEs like IntelliJ 
        Security.addProvider(new BouncyCastleProvider());
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            System.out.println(cipher.getProvider());
            cipher = Cipher.getInstance("AES/ECB/NoPadding", "BC");
            System.out.println(cipher.getProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
