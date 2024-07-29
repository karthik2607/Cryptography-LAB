package VignereCipher;

import java.util.*;
import java.io.*;
import java.net.*;

public class MyServer {
    static String decrypt(String cipher_text, String key)
{
    String orig_text="";

    for (int i = 0 ; i < cipher_text.length() && 
                            i < key.length(); i++)
    {
        int x = (cipher_text.charAt(i) - 
                    key.charAt(i) + 26) %26;

        x += 'A';
        orig_text+=(char)(x);
    }
    return orig_text;
}
    public static void main(String[] args){
        try{
            ServerSocket ss=new ServerSocket(6666);
            Socket s=ss.accept();
            DataInputStream dis=new DataInputStream(s.getInputStream());
            String str=(String) dis.readUTF();
            String key=(String) dis.readUTF();
            String answer=decrypt(str,key);

            System.out.println("Encrypted Message from client = "+str);
            System.out.println("Decrypted Message="+answer);
            ss.close();
        }
        catch (Exception e){System.out.println(e);}

        
    }

    
}
