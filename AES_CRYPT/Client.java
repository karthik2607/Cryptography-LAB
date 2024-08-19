package AES_CRYPT;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class Client {
    public static void main(String[] args) {
        String plaintext = "cryptography1234"; // 16 characters (128 bits)
        byte[] key = "security12BytesK".getBytes(); // 16 characters (128 bits)

        try {
            Testing aes = new Testing(key);
            byte[] encrypted = aes.encrypt(plaintext.getBytes());

            // Send the encrypted data to the server
            Socket socket = new Socket("localhost", 6789);
            DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
            outToServer.write(encrypted);
            outToServer.close();
            socket.close();

            System.out.println("Encrypted data sent to the server: " + Arrays.toString(encrypted));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
