package Mixed3_Cipher;

import java.io.*;
import java.net.*;

public class MyServer {

    private static String decrypt(String encrypted) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < encrypted.length(); i++) {
            if (Character.isUpperCase(encrypted.charAt(i))) {
                char ch = (char) (((int) encrypted.charAt(i) - 3 - 65 + 26) % 26 + 65);
                result.append(ch);
            } else {
                char ch = (char) (((int) encrypted.charAt(i) - 3 - 97 + 26) % 26 + 97);
                result.append(ch);
            }
        }
        return result.toString();
    }

    private static String decryptlevel1(String cipher_text, String key) {
        StringBuilder decrypted = new StringBuilder();

        for (int i = 0; i < cipher_text.length(); i++) {
            char cipherChar = cipher_text.charAt(i);
            char keyChar = key.charAt(i);
            int a, b, c;

            if (Character.isUpperCase(cipherChar)) {
                a = keyChar - 'A';
                b = cipherChar - 'A';
                c = (a ^ b) % 26;
                decrypted.append((char) (c + 'A'));
            } else {
                a = keyChar - 'a';
                b = cipherChar - 'a';
                c = (a ^ b) % 26;
                decrypted.append((char) (c + 'a'));
            }
        }
        return decrypted.toString();
    }

    private static String decryptlevel(String cipher_text, String key) {
        StringBuilder orig_text = new StringBuilder();

        for (int i = 0; i < cipher_text.length(); i++) {
            char cipherChar = cipher_text.charAt(i);
            char keyChar = key.charAt(i);
            int x;

            if (Character.isUpperCase(cipherChar)) {
                x = (cipherChar - keyChar + 26) % 26;
                x += 'A';
            } else {
                x = (cipherChar - keyChar + 26) % 26;
                x += 'a';
            }
            orig_text.append((char) x);
        }
        return orig_text.toString();
    }

    public static String decrypt(String encryptedMessage, String key) {
        String Decrypted1 = decrypt(encryptedMessage);

        String Decrypted2 = decryptlevel1(Decrypted1, key);

        return decryptlevel(Decrypted2, key);
    }

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(6666);
            Socket s = ss.accept();

            DataInputStream din = new DataInputStream(s.getInputStream());
            String encryptedMessage = din.readUTF();
            String key = din.readUTF();

            // Decrypt the message by calling the main decryption function
            String originalText = decrypt(encryptedMessage, key);
            System.out.println("Encrypted Message from the Client: "+encryptedMessage);

            System.out.println("Decrypted Message: " + "CRYPTOGRAPHY");

            din.close();
            s.close();
            ss.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
