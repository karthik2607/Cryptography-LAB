package Mixed3_Cipher;

import java.io.*;
import java.net.*;
import java.util.*;

public class MyClient {

    static String generateKey(String str, String key) {
        int x = str.length();
        StringBuilder newKey = new StringBuilder(key);

        for (int i = 0; ; i++) {
            if (x == i)
                i = 0;
            if (newKey.length() == str.length())
                break;
            newKey.append(key.charAt(i));
        }
        return newKey.toString();
    }

    static String encrypt_level1(String str, String key) {
        StringBuilder cipher_text = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char plainChar = str.charAt(i);
            char keyChar = key.charAt(i);
            int x;

            if (Character.isUpperCase(plainChar)) {
                x = (plainChar + Character.toUpperCase(keyChar)) % 26;
                x += 'A';
            } else {
                x = (plainChar + Character.toLowerCase(keyChar)) % 26;
                x += 'a';
            }
            cipher_text.append((char) x);
        }
        return encrypt_level2(cipher_text.toString(), key);
    }

    static String encrypt_level2(String cipher_text, String key) {
        StringBuilder encrypted = new StringBuilder();

        for (int i = 0; i < cipher_text.length(); i++) {
            char cipherChar = cipher_text.charAt(i);
            char keyChar = key.charAt(i);
            int a, b, c;

            if (Character.isUpperCase(cipherChar)) {
                a = keyChar - 'A';
                b = cipherChar - 'A';
                c = (a ^ b) % 26;
                encrypted.append((char) (c + 'A'));
            } else {
                a = keyChar - 'a';
                b = cipherChar - 'a';
                c = (a ^ b) % 26;
                encrypted.append((char) (c + 'a'));
            }
        }
        return encrypt(encrypted.toString());
    }

    public static String encrypt(String encrypt) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < encrypt.length(); i++) {
            if (Character.isUpperCase(encrypt.charAt(i))) {
                char ch = (char) (((int) encrypt.charAt(i) + 3 - 65) % 26 + 65);
                result.append(ch);
            } else {
                char ch = (char) (((int) encrypt.charAt(i) + 3 - 97) % 26 + 97);
                result.append(ch);
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the message to be encrypted: ");
        String Plaintext = sc.nextLine();
        System.out.println("Enter the key: ");
        String Shift = sc.nextLine();

        String key = generateKey(Plaintext, Shift);
        String answer = encrypt_level1(Plaintext, key);

        System.out.println("Message is Encrypted and sent to Server!!!");

        try {
            Socket s = new Socket("localhost", 6666);
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());

            dout.writeUTF(answer);
            dout.writeUTF(key);
            dout.flush();
            dout.close();
            s.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
