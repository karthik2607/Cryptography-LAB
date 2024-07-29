import java.io.*;
import java.net.*;
import java.util.*;
public class MyClient {
    public static StringBuffer encrypt(String plaintext, int shift) {
        StringBuffer result = new StringBuffer();
 
        for (int i = 0; i < plaintext.length(); i++) {
            if (Character.isUpperCase(plaintext.charAt(i))) {
                char ch = (char)(((int)plaintext.charAt(i) +
                                  shift - 65) % 26 + 65);
                result.append(ch);
            } else {
                char ch = (char)(((int)plaintext.charAt(i) +
                                  shift - 97) % 26 + 97);
                result.append(ch);
            }
        }
        return result;
    }
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the message to be Encrypted ");
        String plaintext=sc.nextLine();
        System.out.println("Enter the number of shifts ");
        int shift=sc.nextInt();
        StringBuffer answer=encrypt(plaintext, shift);
        System.out.println("Message Encrypted and sent to Server !!!");

        try{
            Socket s=new Socket("localhost",6666);
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());

            dout.writeUTF(answer.toString());
            dout.flush();
            dout.close();
            s.close();
        }catch(Exception e){System.out.println(e);}
    }
    
}
