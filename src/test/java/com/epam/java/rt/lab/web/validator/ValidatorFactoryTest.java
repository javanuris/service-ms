package com.epam.java.rt.lab.web.validator;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.TimestampManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ValidatorFactoryTest {

    @Before
    public void setUp() throws Exception {
        PropertyManager.initGlobalProperties();
        AppException.initExceptionBundle();
        TimestampManager.initDateList();
        TimestampManager.initTimeList();
        ValidatorFactory.getInstance().initValidatorMap();
    }

    @Test
    public void create() throws Exception {
        assertNotNull("create() failed", ValidatorFactory.getInstance().create(ValidatorFactory.DIGITS));
    }

}