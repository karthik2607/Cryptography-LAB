package PlayfairCipher;

// import java.io.*;
// import java.net.*;
// import java.util.*;

// public class MyClient {
//     public static String pair(String plain) {
// 		StringBuilder pair = new StringBuilder();
// 		char filler = 'X';
// 		for(int i = 0; i < plain.length(); i += 2) {
// 			if (i + 1 < plain.length()) {
// 				if (plain.charAt(i) != plain.charAt(i + 1)) {
// 					pair.append(plain.charAt(i)).append(plain.charAt(i + 1));
// 				} else {
// 					pair.append(plain.charAt(i)).append(filler);
// 					i--;
// 				}
// 			} else {
// 				pair.append(plain.charAt(i)).append(filler);
// 			}
// 		}
// 		return pair.toString();
// 	}

//     public static char[][] formKeyMatrix(String key) {
// 		key = key.replaceAll("J", "I");
// 		char[][] keymatrix = new char[5][5];
// 		int a = 0;
// 		int Jascii = 74;
// 		Set<Character> unique = new LinkedHashSet<Character>();
// 		String uniquestr = "";
// 		for (int p = 0; p < key.length(); p++) {
// 			unique.add(key.charAt(p));
// 		}
// 		for (char i = 65; i < 91; i++) {
// 			if (i != Jascii)
// 				unique.add(i);
// 		}
// 		Iterator<Character> iter = unique.iterator();
// 		while (iter.hasNext()) {
// 			uniquestr += iter.next();
// 		}
// 		for (int i = 0; i < 5; i++) {
// 			for (int j = 0; j < 5; j++) {
// 				keymatrix[i][j] = uniquestr.charAt(a);
// 				a++;
// 			}
// 		}
// 		return keymatrix;
// 	}

//     public static String toCipherOrPlain(char[][] key, String pair, boolean encrypt) {
// 		boolean found1 = false;
// 		boolean found2 = false;
// 		int frow = 0, fcol = 0, srow = 0, scol = 0;
// 		StringBuilder cipherorplain = new StringBuilder();
// 		for (int ind = 0; ind + 1 < pair.length(); ind += 2) {
// 			found1 = false;
// 			found2 = false;
// 			for (int i = 0; i < 5; i++) {
// 				for (int j = 0; j < 5; j++) {
// 					if (pair.charAt(ind) == key[i][j]) {
// 						frow = i;
// 						fcol = j;
// 						found1 = true;
// 					}
// 					if (pair.charAt(ind + 1) == key[i][j]) {
// 						srow = i;
// 						scol = j;
// 						found2 = true;
// 					}
// 					if (found1 & found2)
// 						break;
// 				}
// 				if (found1 & found2)
// 					break;
// 			}

// 			if (frow == srow) { //case I - both the letters are in the same row
// 				cipherorplain.append(key[frow][(fcol + (encrypt ? 1 : 4)) % 5]);
// 				cipherorplain.append(key[srow][(scol + (encrypt ? 1 : 4)) % 5]);
// 			} else if (fcol == scol) { //case II - both the letters are in the same column
// 				cipherorplain.append(key[(frow + (encrypt ? 1 : 4)) % 5][fcol]);
// 				cipherorplain.append(key[(srow + (encrypt ? 1 : 4)) % 5][scol]);
// 			} else { // Case III - both the letters are neither in the same row nor in the same column
// 				cipherorplain.append(key[frow][scol]);
// 				cipherorplain.append(key[srow][fcol]);
// 			}
// 		}
// 		return cipherorplain.toString();
// 	}




//     public static void main(String[] args){
//         Scanner sc=new Scanner(System.in);
// 		System.out.println("Enter the plain text: ");
// 		String plain=sc.next();
// 		System.out.println("Enter the key: ");
// 		String key=sc.next();
//                 System.out.println("\nEncryption using playfair:\n");
// 		String pairplain=pair(plain);
// 		char[][] keymatrix=formKeyMatrix(key);
// 		System.out.println("Filler letter: X\n");
// 		System.out.println("5x5 key matrix (considering I and J as a single letter): ");
// 		for(int i=0;i<5;i++)
// 		{
// 			for(int j=0;j<5;j++)
// 			{
// 				System.out.print(keymatrix[i][j]+" ");
// 			}
// 			System.out.println("");
// 		}
// 		String ciphertext=toCipherOrPlain(keymatrix,pairplain, true);
// 		System.out.println("\nCorresponding Cipher text: "+ciphertext);
//         System.out.println("Message Encrypted and sent to Server !!!");

//         try{
//             Socket s=new Socket("localhost",6666);
//             DataOutputStream dout=new DataOutputStream(s.getOutputStream());

//             dout.writeUTF(ciphertext.toString());
//             dout.
//             dout.flush();
//             dout.close();
//             s.close();
//         }catch(Exception e){System.out.println(e);}
//     }


    
// }
import java.io.*;
import java.net.*;
import java.util.*;

public class MyClient {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the message to be encrypted: ");
        String plaintext = sc.nextLine();

        System.out.println("Enter the key: ");
        String key = sc.nextLine();

        char[][] keymatrix = formKeyMatrix(key);
        String pairedText = pair(plaintext);
        String encryptedText = toCipherOrPlain(keymatrix, pairedText, true);

        System.out.println("Encrypted Message: " + encryptedText);
        System.out.println("Message Encrypted and sent to Server!");

        try {
            Socket s = new Socket("localhost", 6666);
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());

            dout.writeUTF(encryptedText);
            dout.writeUTF(key);

            dout.flush();
            dout.close();
            s.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String pair(String plain) {
        StringBuilder pair = new StringBuilder();
        char filler = 'X';
        for (int i = 0; i < plain.length(); i += 2) {
            if (i + 1 < plain.length()) {
                if (plain.charAt(i) != plain.charAt(i + 1)) {
                    pair.append(plain.charAt(i)).append(plain.charAt(i + 1));
                } else {
                    pair.append(plain.charAt(i)).append(filler);
                    i--;
                }
            } else {
                pair.append(plain.charAt(i)).append(filler);
            }
        }
        return pair.toString();
    }

    public static char[][] formKeyMatrix(String key) {
        key = key.replaceAll("J", "I");
        char[][] keymatrix = new char[5][5];
        int a = 0;
        int Jascii = 74;
        Set<Character> unique = new LinkedHashSet<>();
        String uniquestr = "";
        for (int p = 0; p < key.length(); p++) {
            unique.add(key.charAt(p));
        }
        for (char i = 65; i < 91; i++) {
            if (i != Jascii)
                unique.add(i);
        }
        Iterator<Character> iter = unique.iterator();
        while (iter.hasNext()) {
            uniquestr += iter.next();
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                keymatrix[i][j] = uniquestr.charAt(a);
                a++;
            }
        }
        return keymatrix;
    }

    public static String toCipherOrPlain(char[][] key, String pair, boolean encrypt) {
        boolean found1 = false;
        boolean found2 = false;
        int frow = 0, fcol = 0, srow = 0, scol = 0;
        StringBuilder cipherorplain = new StringBuilder();
        for (int ind = 0; ind + 1 < pair.length(); ind += 2) {
            found1 = false;
            found2 = false;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (pair.charAt(ind) == key[i][j]) {
                        frow = i;
                        fcol = j;
                        found1 = true;
                    }
                    if (pair.charAt(ind + 1) == key[i][j]) {
                        srow = i;
                        scol = j;
                        found2 = true;
                    }
                    if (found1 & found2)
                        break;
                }
                if (found1 & found2)
                    break;
            }

            if (frow == srow) {
                cipherorplain.append(key[frow][(fcol + (encrypt ? 1 : 4)) % 5]);
                cipherorplain.append(key[srow][(scol + (encrypt ? 1 : 4)) % 5]);
            } else if (fcol == scol) {
                cipherorplain.append(key[(frow + (encrypt ? 1 : 4)) % 5][fcol]);
                cipherorplain.append(key[(srow + (encrypt ? 1 : 4)) % 5][scol]);
            } else {
                cipherorplain.append(key[frow][scol]);
                cipherorplain.append(key[srow][fcol]);
            }
        }
        return cipherorplain.toString();
    }
}
