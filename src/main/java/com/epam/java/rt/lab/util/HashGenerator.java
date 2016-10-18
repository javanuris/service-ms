package com.epam.java.rt.lab.util;

import com.epam.java.rt.lab.exception.AppException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static com.epam.java.rt.lab.util.PropertyManager.SPACE;
import static com.epam.java.rt.lab.util.UtilExceptionCode.NO_SUCH_ALGORITHM;

public class HashGenerator {

    private static final String ALGORITHM_NAME = "SHA-256";

    public static String hashString() throws AppException {
        return hashString(UUID.randomUUID().toString());
    }

    public static String hashString(String sourceString)
            throws AppException {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM_NAME);
            md.update(sourceString.getBytes());
            byte byteData[] = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                String hex = Integer.toHexString(0xff & byteData[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new AppException(NO_SUCH_ALGORITHM,
                    e.getMessage(), e.getCause());
        }
    }

    private static String saltedPassword(String salt, String password) {
        StringBuilder saltedPassword = new StringBuilder();
        int passwordIndex = 0;
        for (int i = 0; i < salt.length(); i++) {
            saltedPassword.append(password.charAt(passwordIndex)).
                    append(salt.charAt(i));
            passwordIndex = passwordIndex + 1;
            if (!(passwordIndex < password.length())) passwordIndex = 0;
        }
        return saltedPassword.toString();
    }

    public static String hashPassword(String salt, String password)
            throws AppException {
        String saltedPassword = saltedPassword(salt, password);
        return salt + SPACE + hashString(saltedPassword);
    }

}
