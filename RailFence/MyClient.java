package RailFence;

import java.io.*;
import java.net.*;
import java.util.*;

public class MyClient {
    public static String encryptRailFense(String text,int key)
    {
 
        char[][] rail = new char[key][text.length()];
 
        for (int i = 0; i < key; i++)
            Arrays.fill(rail[i], '\n');
 
        boolean dirDown = false;
        int row = 0, col = 0;
 
        for (int i = 0; i < text.length(); i++) {
 
            if (row == 0 || row == key - 1)
                dirDown = !dirDown;
 
            rail[row][col++] = text.charAt(i);
 
            if (dirDown)
                row++;
            else
                row--;
        }
 
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < key; i++)
            for (int j = 0; j < text.length(); j++)
                if (rail[i][j] != '\n')
                    result.append(rail[i][j]);
 
        return result.toString();
    }
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the Message to be encrypted ");
        String plaintext=sc.nextLine();
        System.out.println("Enter the number of shifts/key ");
        int shift=sc.nextInt();
        String answer=encryptRailFense(plaintext, shift);
        System.out.println("Message is Encrypted and sent to Server !!!");

        try{
            Socket s=new Socket("localhost",6666);
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());

            dout.writeUTF(answer.toString());
            dout.writeInt(shift);
            dout.flush();
            dout.close();
            s.close();
        }catch(Exception e){System.out.println(e);}
    }


    
}
