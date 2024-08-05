package HillCipher;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MyClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 6666;
        Scanner sc = new Scanner(System.in);

        try (Socket socket = new Socket(host, port)) {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Enter Matrix Values 2x2:");
            int[][] matrix = new int[2][2];
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    matrix[i][j] = sc.nextInt();
                }
            }
            sc.nextLine();  // Consume newline

            System.out.println("Enter message to encrypt: ");
            String msg = console.readLine();
            if (msg.length() % 2 != 0) {
                msg += "x"; // filling character x if length is odd
            }
            String encrypted = encryption(msg, matrix);
            System.out.println("Encrypted message: " + encrypted);

            writer.println(encrypted);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    writer.println(matrix[i][j]);
                }
            }

            writer.flush();
        } catch (Exception e) {
            System.out.println("Client Exception: " + e.getMessage());
        }
    }

    private static String encryption(String msg, int[][] matrix) {
        StringBuilder encrypt = new StringBuilder();
        for (int i = 0; i < msg.length() - 1; i += 2) {
            int a = msg.charAt(i) - 'a';
            int b = msg.charAt(i + 1) - 'a';
            int x = matrix[0][0] * a + matrix[1][0] * b;
            int y = matrix[0][1] * a + matrix[1][1] * b;
            encrypt.append((char) (x % 26 + 'a'));
            encrypt.append((char) (y % 26 + 'a'));
        }
        return encrypt.toString();
    }
}
