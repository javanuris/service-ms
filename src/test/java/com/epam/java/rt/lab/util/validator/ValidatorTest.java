package com.epam.java.rt.lab.util.validator;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * service-ms
 */
public class ValidatorTest {

    private static final String MSG = "INVALIDATE";
    private Validator validator;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getBooleanAndValidate() throws Exception {
        this.validator = Validator.getBoolean(MSG);
        assertNull("getBooleanAndValidate(true) failed", this.validator.validate("true"));
        assertNotNull("getBooleanAndValidate(null) failed", this.validator.validate(null));
        String[] messages = this.validator.validate("5948");
        assertNotNull("getBooleanAndValidate(29458) failed", messages);
        assertEquals("getBooleanAndValidate(MSG) failed", MSG, messages[0]);
    }

    @Test
    public void getNumberAndValidate() throws Exception {
        this.validator = Validator.getNumber(MSG, 0, 10);
        assertNull("getNumberAndValidate(5) failed", this.validator.validate("5"));
        assertNotNull("getNumberAndValidate(fdgdfg) failed", this.validator.validate("sdfsdfsd"));
        assertNotNull("getNumberAndValidate(null) failed", this.validator.validate(null));
        String[] messages = this.validator.validate("500");
        assertNotNull("getNumberAndValidate(500) failed", messages);
        assertEquals("getNumberAndValidate(MSG) failed", MSG, messages[0]);
    }

    @Test
    @Ignore
    public void getFuture() throws Exception {

    }

    @Test
    @Ignore
    public void getPast() throws Exception {

    }

    @Test
    public void getPatternAndValidate() throws Exception {
        this.validator = Validator.getPattern(MSG, "abcd");
        assertNull("getPatternAndValidate(abcd) failed", this.validator.validate("abcd"));
        assertNotNull("getPatternAndValidate(fdgdfg) failed", this.validator.validate("sdfsdfsd"));
        assertNotNull("getPatternAndValidate(null) failed", this.validator.validate(null));
        String[] messages = this.validator.validate("500");
        assertNotNull("getPatternAndValidate(500) failed", messages);
        assertEquals("getPatternAndValidate(MSG) failed", MSG, messages[0]);
    }

    @Test
    @Ignore
    public void getBooleanOrNull() throws Exception {

    }

    @Test
    @Ignore
    public void getNumberOrNull() throws Exception {

    }

    @Test
    @Ignore
    public void getFutureOrNull() throws Exception {

    }

    @Test
    @Ignore
    public void getPastOrNull() throws Exception {

    }

    @Test
    @Ignore
    public void getPatternOrNull() throws Exception {

    }

    @Test
    @Ignore
    public void getType() throws Exception {

    }

    @Test
    @Ignore
    public void getNumberMin() throws Exception {

    }

    @Test
    @Ignore
    public void getNumberMax() throws Exception {

    }

    @Test
    @Ignore
    public void getCompareTimestamp() throws Exception {

    }

    @Test
    @Ignore
    public void getRegex() throws Exception {

    }

    @Test
    @Ignore
    public void getMsg() throws Exception {

    }

}