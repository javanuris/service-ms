package com.epam.java.rt.lab.exception;

import com.epam.java.rt.lab.exception.AppException.AppExceptionCode;
import com.epam.java.rt.lab.util.PropertyManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.epam.java.rt.lab.util.PropertyManager.EQUAL;
import static org.junit.Assert.*;

public class AppExceptionTest {

    private static final ExceptionCode EXCEPTION_CODE =
            AppExceptionCode.NULL_NOT_ALLOWED;
    private static final String GET_MESSAGE_RESULT = "Null недопустим";
    private static final String GET_LOCALIZED_MESSAGE_RESULT = "Null недопустим";
    private static final String DETAIL_NAME = "test_name";
    private static final String DETAIL_VALUE = "test_value";
    private static final String GET_DETAILED_MESSAGE_RESULT = "Null недопустим (test_name=test_value)";

    private AppException appException;

    @Before
    public void setUp() throws Exception {
        AppException.initExceptionBundle();
        this.appException = new AppException(EXCEPTION_CODE);
    }

    @After
    public void tearDown() throws Exception {
        this.appException = null;
    }

    @Test
    public void appExceptionCodeTest() throws Exception {
        assertEquals("getExceptionCode() failed",
                EXCEPTION_CODE.getNumber(),
                this.appException.getExceptionCode().getNumber());
    }

    @Test
    public void getMessage() throws Exception {
        assertEquals("getMessage() failed",
                GET_MESSAGE_RESULT,
                this.appException.getMessage());
    }

    @Test
    public void getLocalizedMessage() throws Exception {
        assertEquals("getLocalizedMessage() failed",
                GET_LOCALIZED_MESSAGE_RESULT,
                this.appException.getLocalizedMessage());
    }

    @Test
    public void initiateWithDetailArrayTest() throws Exception {
        String[] detailArray = {DETAIL_NAME + EQUAL + DETAIL_VALUE};
        this.appException = new AppException(EXCEPTION_CODE, detailArray);
        assertEquals("initiateWithDetailArray() failed",
                GET_DETAILED_MESSAGE_RESULT,
                this.appException.getMessage());
    }

}