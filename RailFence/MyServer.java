package RailFence;
import java.util.*;
import java.io.*;
import java.net.*;

public class MyServer {
    public static String decryptRailFence(String cipher,int key)
    {
 
        char[][] rail = new char[key][cipher.length()];
 
        for (int i = 0; i < key; i++)
            Arrays.fill(rail[i], '\n');
 
        boolean dirDown = true;
 
        int row = 0, col = 0;
 
        for (int i = 0; i < cipher.length(); i++) {
            if (row == 0)
                dirDown = true;
            if (row == key - 1)
                dirDown = false;
 
            rail[row][col++] = '*';
 
            if (dirDown)
                row++;
            else
                row--;
        }
 
        int index = 0;
        for (int i = 0; i < key; i++)
            for (int j = 0; j < cipher.length(); j++)
                if (rail[i][j] == '*'
                    && index < cipher.length())
                    rail[i][j] = cipher.charAt(index++);
 
        StringBuilder result = new StringBuilder();
 
        row = 0;
        col = 0;
        for (int i = 0; i < cipher.length(); i++) {
            if (row == 0)
                dirDown = true;
            if (row == key - 1)
                dirDown = false;
 
            if (rail[row][col] != '*')
                result.append(rail[row][col++]);
 
            if (dirDown)
                row++;
            else
                row--;
        }
        return result.toString();
    }
    public static void main(String[] args){
        try{
            ServerSocket ss=new ServerSocket(6666);
            Socket s=ss.accept();
            DataInputStream dis=new DataInputStream(s.getInputStream());
            String str=(String) dis.readUTF();
            int key=(Integer)dis.readInt();
            String answer=decryptRailFence(str,key);
            
            System.out.println("Encrypted Message from client = "+str);
            System.out.println("Decrypted Message="+answer);
            ss.close();
        }
        catch (Exception e){System.out.println(e);}

        
    }

    
}
