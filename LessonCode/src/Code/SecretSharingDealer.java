
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.io.*;
import java.net.*;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class SecretSharingDealer {

    static void Protocol(Socket sSock, byte[] secretshare) throws Exception {

        OutputStream out = sSock.getOutputStream();

        try {

            ObjectOutputStream objectOut;

            objectOut = new ObjectOutputStream(out);

            objectOut.writeObject(secretshare);
            System.out.println("secretshare sent to client" + " " + Utils.toHex(secretshare));

            sSock.close(); // close connection
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
private static String digits = "0123456789abcdef";
    static String toHex(byte[] data, int length) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i != length; i++) {
            int v = data[i] & 0xff;
            buf.append(digits.charAt(v >> 4));
            buf.append(digits.charAt(v & 0xf));
        }
        return buf.toString();
    }

    public static String toHex(byte[] data) {
        return toHex(data, data.length);
    }

    public static void main(String[] args) throws Exception {
        SSLServerSocketFactory sockfact = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault(); //
        SSLServerSocket sSock;
        SSLSocket[] sslSock = new SSLSocket[3];
        sSock = (SSLServerSocket) sockfact.createServerSocket(4000); // bind to port 4000
        byte[][] secretshares = new byte[3][16];
        byte[] secret = new byte[16];

        SecureRandom sc = new SecureRandom();
        for (int i = 0; i < 3; i++) {
            sc.nextBytes(secretshares[i]);

            System.out.println("Secret share sent to client" + i + "= " + toHex(secretshares[i]));
        }

        for (int i = 0; i < secret.length; i++) {
            for (int j = 0; j < 3; j++) {
                secret[i] = (byte) (secret[i] ^ secretshares[j][i]);
            }
        }
        System.out.println("Secret " + toHex(secret));

        for (int i = 0; i < 3; i++) {
            System.out.println("Waiting for connections...");
            sslSock[i] = (SSLSocket) sSock.accept(); // accept connections
            System.out.println("new connection\n");
            Protocol(sslSock[i], secretshares[i]);

        }

    }
}
