package AES_CRYPT;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        byte[] key = "security12BytesK".getBytes(); // 16 characters (128 bits)

        try {
            ServerSocket welcomeSocket = new ServerSocket(6789);
            System.out.println("Server is running and waiting for the client...");

            Socket connectionSocket = welcomeSocket.accept();
            DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());

            // Receive the encrypted data from the client
            byte[] encrypted = new byte[16]; // 128-bit block
            inFromClient.readFully(encrypted);

            Testing aes = new Testing(key);
            byte[] decrypted = aes.decrypt(encrypted);

            System.out.println("Decrypted data: " + new String(decrypted));

            connectionSocket.close();
            welcomeSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
