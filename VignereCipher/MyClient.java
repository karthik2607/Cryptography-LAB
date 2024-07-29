package VignereCipher;
import java.io.*;
import java.net.*;
import java.util.*;

public class MyClient {
    static String generateKey(String str, String key)
{
    int x = str.length();

    for (int i = 0; ; i++)
    {
        if (x == i)
            i = 0;
        if (key.length() == str.length())
            break;
        key+=(key.charAt(i));
    }
    return key;
}

static String encrypt(String str, String key)
{
    String cipher_text="";

    for (int i = 0; i < str.length(); i++)
    {
        int x = (str.charAt(i) + key.charAt(i)) %26;

        x += 'A';

        cipher_text+=(char)(x);
    }
    return cipher_text;
}

static String LowerToUpper(String s)
{
    StringBuffer str =new StringBuffer(s); 
    for(int i = 0; i < s.length(); i++)
    {
        if(Character.isLowerCase(s.charAt(i)))
        {
            str.setCharAt(i, Character.toUpperCase(s.charAt(i)));
        }
    }
    s = str.toString();
    return s;
}
    
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the message to be encrypted ");
        String Plaintext=sc.nextLine();
        System.out.println("Enter the key ");
        String Shift=sc.nextLine();
        
        String plaintext = LowerToUpper(Plaintext);
        String shift = LowerToUpper(Shift);

        String key = generateKey(plaintext, shift);


        String answer=encrypt(plaintext, key);
        System.out.println("Message is Encrypted and sent to Server !!!");

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
