package VernamCipher;

import java.io.*;
import java.net.*;
import java.util.*;
public class MyClient {
    public static String encrypt(String key,String msg){
        String encrpt="";
        for(int i=0;i<key.length();i++){
        int a=key.charAt(i)-'a';
        int b=msg.charAt(i)-'a';
        int c=(a^b)%26;
        encrpt+=(char)(c+'a');
        }
        return encrpt;
    }
       
    
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the message to be Encrypted ");
        String plaintext=sc.nextLine();
        System.out.println("Enter Key ");
        String key=sc.nextLine();
        String answer=encrypt(plaintext, key);
        System.out.println("Message Encrypted and sent to Server !!!");

        try{
            Socket s=new Socket("localhost",6666);
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());

            dout.writeUTF(answer.toString());
            dout.writeUTF(key.toString());
            dout.flush();
            dout.close();
            s.close();
        }catch(Exception e){System.out.println(e);}
    }
    
}
