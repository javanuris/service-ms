package com.epam.java.rt.lab.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * service-ms
 */
public class HashGenerator {

    private static final String SALT_DELIMITER = " ";

    public static String hashString(String sourceString) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(sourceString.getBytes());
        byte byteData[] = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static String saltedPassword(String salt, String password) {
        StringBuilder saltedPassword = new StringBuilder();
        int passwordIndex = 0;
        for (int i = 0; i < salt.length(); i++) {
            saltedPassword.append(password.charAt(passwordIndex)).append(salt.charAt(i));
            passwordIndex = passwordIndex + 1;
            if (!(passwordIndex < password.length())) passwordIndex = 0;
        }
        return saltedPassword.toString();
    }

    public static String hashPassword(String salt, String password) throws NoSuchAlgorithmException {
        String saltedPassword = saltedPassword(salt, password);
        return salt.concat(SALT_DELIMITER).concat(hashString(saltedPassword));
    }

}
