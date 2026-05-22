package ipl.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    public static String getSHA256Hash(String input) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = sha256.digest(input.getBytes());
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }
            return hashString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("SHA-256 algorithm not found: " + e.getMessage());
            return null;
        }
    }
//    public static void main(String[] args) {
//        String input = "1234";
//        String hashedValue = getSHA256Hash(input);
//        String a = "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4";
//        System.out.println("SHA-256 Hash: " + hashedValue);
//        if(a.equals(hashedValue)) System.out.println("Same Same");
//    }
}

