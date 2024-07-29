package VernamCipher;

import java.io.*;
import java.net.*;
public class MyServer {
    public static  String decryption(String key,String encrpt){
        String msg="";
        for(int i=0;i<key.length();i++){
        int a=key.charAt(i)-'a';
        int c=encrpt.charAt(i)-'a';
        int b=(a^c)%26;
        msg+=(char)(b+'a');
        }
        return msg;
        }
    public static void main(String[] args){
        try{
            ServerSocket ss=new ServerSocket(6666);
            Socket s=ss.accept();
            DataInputStream dis=new DataInputStream(s.getInputStream());
            String str=(String) dis.readUTF();
            String key=(String) dis.readUTF();
            System.out.println("Encrypted Message="+str);
            String answer=decryption(str, key);
            System.out.println("Decrypted Message = "+answer);
            ss.close();
        }
        catch (Exception e){System.out.println(e);}

        
    }
    
}
