package Code;

import java.security.Provider;
import java.security.Security;
import java.util.Iterator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * List the available capabilities for ciphers, key agreement, macs, message
 * digests, signatures and other objects in the SUN or BC or other providers.
 */
public class ListProviderCapabilities {

    public static void main(String[] args) {
        String providerName = "SUN"; // change this to SUN to test if the standard SUN provider is installed

// try to uncomment this for other IDEs like IntelliJ 
        Security.addProvider(new BouncyCastleProvider());
        Provider provider = Security.getProvider("SUN"); //change the string to BC or other providers to list capabilities of other providers.

        Iterator it = provider.keySet().iterator();

        while (it.hasNext()) {

            String entry = (String) it.next();
            System.out.println(entry);

        }
    }
}
