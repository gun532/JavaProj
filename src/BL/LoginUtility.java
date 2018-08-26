package BL;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class LoginUtility {

    public String getEncryptedPass(String pass) {

        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pass.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString().toUpperCase();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
