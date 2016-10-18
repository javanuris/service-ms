package com.epam.java.rt.lab.util;

import com.epam.java.rt.lab.exception.AppException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HashGeneratorTest {

    private static final String SALT_STRING = "sdhuyt9ty4thrptwieur89";
    private static final String SOURCE_STRING = "qawsedrftgyhujikolpazsxdcfv1q2w3e4rfvg";

    @Before
    public void setUp() throws Exception {
        PropertyManager.initGlobalProperties();
        AppException.initExceptionBundle();
    }

    @Test
    public void hashString() throws Exception {
        String hash = HashGenerator.hashString();
        assertNotNull("hashString() failed", hash);
        assertNotEquals("hashString() failed", hash, HashGenerator.hashString());
    }

    @Test
    public void hashStringWithValue() throws Exception {
        String hash1 = HashGenerator.hashString(SOURCE_STRING);
        String hash2 = HashGenerator.hashString(SOURCE_STRING);
        assertEquals("hashStringWithValue() failed", hash1, hash2);
        assertNotEquals("hashStringWithValue() failed", hash1,
                HashGenerator.hashString());
    }

    @Test
    public void hashPassword() throws Exception {
        String hash1 = HashGenerator.hashPassword(SALT_STRING, SOURCE_STRING);
        String hash2 = HashGenerator.hashPassword("", SOURCE_STRING);
        assertNotEquals("hashPassword() failed", hash1, hash2);
        assertEquals("hashPassword() failed", hash1,
                HashGenerator.hashPassword(SALT_STRING, SOURCE_STRING));
    }

}