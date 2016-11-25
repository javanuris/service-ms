package com.epam.java.rt.lab.web.action;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.TimestampManager;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;
import org.junit.Before;
import org.junit.Test;

import static com.epam.java.rt.lab.util.PropertyManager.GET;
import static com.epam.java.rt.lab.util.PropertyManager.HOME_PATH;
import static org.junit.Assert.assertNotNull;

public class ActionFactoryTest {

    @Before
    public void setUp() throws Exception {
        PropertyManager.initGlobalProperties();
        AppException.initExceptionBundle();
        TimestampManager.initDateList();
        TimestampManager.initTimeList();
        ValidatorFactory.getInstance().initValidatorMap();
        ActionFactory.getInstance().initActionMap();
    }

    @Test
    public void create() throws Exception {
        assertNotNull("create() failed", ActionFactory.getInstance().create(GET, HOME_PATH));
    }

}