package com.epam.java.rt.lab.web.component.form;

import com.epam.java.rt.lab.util.validator.Validator;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * service-ms
 */
public class FormControlTest {

    private static final String NAME = "login.email.label";
    private static final String LABEL = "login.email.label";
    private static final String TYPE = "TEXT";
    private static final String PLACEHOLDER = "login.email.label";
    private static final String ACTION = "/profile/login";
    private static final String SUB_ACTION = "/profile";
    private static final String VALUE = "TESTVALUE";
    private static final Long GENVALUE = 100L;
    private static final boolean TRUE = true;
    private static final String MSG = "MSG";

    private FormControl formControl;

    @Before
    public void setUp() throws Exception {
        this.formControl = new FormControl(NAME, LABEL, TYPE, PLACEHOLDER, ACTION, SUB_ACTION, null);
    }

    @After
    public void tearDown() throws Exception {
        this.formControl = null;
    }

    @Test
    public void getName() throws Exception {
        assertEquals("getName() failed", NAME, this.formControl.getName());
    }

    @Test
    public void getLabel() throws Exception {
        assertEquals("getLabel() failed", LABEL, this.formControl.getLabel());
    }

    @Test
    public void getType() throws Exception {
        assertEquals("getType() failed", TYPE, this.formControl.getType());
    }

    @Test
    public void getPlaceholder() throws Exception {
        assertEquals("getPlaceholder() failed", PLACEHOLDER, this.formControl.getPlaceholder());
    }

    @Test
    public void getAction() throws Exception {
        assertEquals("getAction() failed", ACTION, this.formControl.getAction());
    }

    @Test
    public void getSubAction() throws Exception {
        assertEquals("getSubAction() failed", SUB_ACTION, this.formControl.getSubAction());
    }

    @Test
    @Ignore
    public void getValidator() throws Exception {

    }

    @Test
    @Ignore
    public void getAvailableValueList() throws Exception {

    }

    @Test
    @Ignore
    public void setAvailableValueList() throws Exception {

    }

    @Test
    public void setAndGetValue() throws Exception {
        this.formControl.setValue(VALUE);
        assertEquals("setAndGetValue() failed", VALUE, this.formControl.getValue());
    }

    @Test
    public void setAndGetGenericValue() throws Exception {
        this.formControl.setGenericValue(GENVALUE);
        assertEquals("setAndGetGenericValue() failed", GENVALUE, this.formControl.getGenericValue());
    }

    @Test
    public void setAndGetValidationMessageList() throws Exception {
        List<String> stringList = new ArrayList<>();
        stringList.add(MSG);
        this.formControl.setValidationMessageList(stringList);
        assertNotNull("setAndGetValidationMessageList() failed", this.formControl.getValidationMessageList());
        assertEquals("setAndGetValidationMessageList(MSG) failed", MSG, this.formControl.getValidationMessageList().get(0));
    }

    @Test
    public void setAndGetIgnoreValidate() throws Exception {
        this.formControl.setIgnoreValidate(TRUE);
        assertEquals("setAndGetIgnoreValidate() failed", TRUE, this.formControl.getIgnoreValidate());
    }

}