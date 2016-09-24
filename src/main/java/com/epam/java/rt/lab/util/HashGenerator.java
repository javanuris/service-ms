package com.epam.java.rt.lab.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * service-ms
 */
public class HashGenerator {

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

    public static boolean compareHashToSource(String hashString, String sourceString)
            throws NoSuchAlgorithmException {
        String hashSourceString = HashGenerator.hashString(sourceString);
        return hashString.equals(hashSourceString);
    }

}
