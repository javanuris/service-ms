package com.epam.java.rt.lab.web.component.form;

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
public class FormTest {

    private static final String FORM_NAME = "login-form";
    private static final String FORM_ACTION = "/profile/login";
    private static final String NAME = "login.email.label";
    private static final String LABEL = "login.email.label";
    private static final String TYPE = "TEXT";
    private static final String PLACEHOLDER = "login.email.label";
    private static final String ACTION = "/profile/login";
    private static final String SUB_ACTION = "/profile";

    private Form form;

    @Before
    public void setUp() throws Exception {
        this.form = new Form(FORM_NAME, FORM_ACTION);
        List<FormControl> formControlList = new ArrayList<>();
        formControlList.add(
                new FormControl(
                        NAME,
                        LABEL,
                        TYPE,
                        PLACEHOLDER,
                        ACTION,
                        SUB_ACTION,
                        null
                )
        );
        this.form.setFormControlList(formControlList);
    }

    @After
    public void tearDown() throws Exception {
        this.form = null;
    }

    @Test
    public void copyDef() throws Exception {

    }

    @Test
    public void getName() throws Exception {
        assertEquals("getName() failed", FORM_NAME, this.form.getName());
    }

    @Test
    public void getAction() throws Exception {
        assertEquals("getAction() failed", FORM_ACTION, this.form.getAction());
    }

    @Test
    @Ignore
    public void setActionParameterString() throws Exception {

    }

    @Test
    public void getItemListSize() throws Exception {
        assertEquals("getItemListSize() failed", 1, this.form.getItemListSize());
    }

    @Test
    public void getItem() throws Exception {
        assertNotNull("getItem(0) failed", this.form.getItem(0));
        assertEquals("getItem(0).name failed", NAME, this.form.getItem(0).getName());
    }

    @Test
    public void setFormControlList() throws Exception {
        List<FormControl> formControlList = new ArrayList<>();
        formControlList.add(
                new FormControl(
                        NAME,
                        LABEL,
                        TYPE,
                        PLACEHOLDER,
                        ACTION,
                        SUB_ACTION,
                        null
                )
        );
        formControlList.add(
                new FormControl(
                        NAME,
                        LABEL,
                        TYPE,
                        PLACEHOLDER,
                        ACTION,
                        SUB_ACTION,
                        null
                )
        );
        formControlList.add(
                new FormControl(
                        NAME,
                        LABEL,
                        TYPE,
                        PLACEHOLDER,
                        ACTION,
                        SUB_ACTION,
                        null
                )
        );
        this.form.setFormControlList(formControlList);
        assertEquals("setFormControlList()&getItemListSize() failed", 3, this.form.getItemListSize());
    }

    @Test
    public void iterator() throws Exception {
        for (FormControl formControl : this.form)
            assertNotNull("interator() failed", formControl);
    }

}