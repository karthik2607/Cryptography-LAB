package HillCipher;

import java.io.*;
import java.net.*;

public class MyServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(6666)) {
            System.out.println("Server is listening on port 6666");
            while (true) {
                Socket socket = serverSocket.accept();
                new ServerThread(socket).start();
            }
        } catch (IOException e) {
            System.out.println("Server Exception: " + e.getMessage());
        }
    }
}

class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (InputStream input = socket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input));
             OutputStream output = socket.getOutputStream();
             PrintWriter writer = new PrintWriter(output, true)) {

            String encryptedText = reader.readLine();
            int[][] matrix = new int[2][2];
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    matrix[i][j] = Integer.parseInt(reader.readLine());
                }
            }

            String decryptedText = decryption(encryptedText, matrix);
            System.out.println("Decrypted message: " + decryptedText);

        } catch (IOException e) {
            System.out.println("Server Exception: " + e.getMessage());
        }
    }

    private String decryption(String encryptedText, int[][] matrix) {
        StringBuilder decrypt = new StringBuilder();
        int det = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        int detInv = modInverse(det, 26);
        int[][] inverseMatrix = new int[2][2];
        inverseMatrix[0][0] = matrix[1][1] * detInv % 26;
        inverseMatrix[0][1] = (-matrix[0][1] + 26) * detInv % 26;
        inverseMatrix[1][0] = (-matrix[1][0] + 26) * detInv % 26;
        inverseMatrix[1][1] = matrix[0][0] * detInv % 26;

        for (int i = 0; i < encryptedText.length() - 1; i += 2) {
            int a = encryptedText.charAt(i) - 'a';
            int b = encryptedText.charAt(i + 1) - 'a';
            int x = inverseMatrix[0][0] * a + inverseMatrix[1][0] * b;
            int y = inverseMatrix[0][1] * a + inverseMatrix[1][1] * b;
            decrypt.append((char) (mod(x, 26) + 'a'));
            decrypt.append((char) (mod(y, 26) + 'a'));
        }
        return decrypt.toString();
    }

    private int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1;
    }

    private int mod(int x, int m) {
        int r = x % m;
        if (r < 0) {
            r += m;
        }
        return r;
    }
}
