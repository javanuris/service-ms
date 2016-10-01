package com.epam.java.rt.lab.web.component.form;

import com.epam.java.rt.lab.util.StringArray;
import com.epam.java.rt.lab.util.validator.ValidatorException;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;

import java.io.IOException;
import java.util.*;

/**
 * service-ms
 */
public final class FormFactory {

    private static class Holder {

        private static final FormFactory INSTANCE;

        static {
            try {
                INSTANCE = new FormFactory();
            } catch (Exception e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    private Map<String, Form> formMap = new HashMap<>();

    private FormFactory() throws FormException {
        fillFormMap();
    }

    private void fillFormMap() throws FormException {
        Properties properties = new Properties();
        String point = ".";
        String comma = ",";
        String forms = "forms";
        String action = ".action";
        String controls = ".controls";
        String label = ".label";
        String type = ".type";
        String placeholder = ".placeholder";
        String subAction = ".sub-action";
        String validator = ".validator";
        try {
            properties.load(FormFactory.class.getClassLoader().getResourceAsStream("form.properties"));
            this.formMap.clear();
            for (String formName : StringArray.splitSpaceLessNames(properties.getProperty(forms), comma)) {
                Form form = new Form(formName, properties.getProperty(formName.concat(action)));
                List<FormControl> formControlList = new ArrayList<>();
                for (String controlName : StringArray.splitSpaceLessNames(properties.getProperty(formName.concat(controls)), comma)) {
                    String propertyPrefix = formName.concat(point).concat(controlName);
                    formControlList.add(
                            new FormControl(
                                    propertyPrefix,
                                    properties.getProperty(propertyPrefix.concat(label)),
                                    properties.getProperty(propertyPrefix.concat(type)),
                                    properties.getProperty(propertyPrefix.concat(placeholder)),
                                    properties.getProperty(propertyPrefix.concat(action)),
                                    properties.getProperty(propertyPrefix.concat(subAction)),
                                    ValidatorFactory.create(properties.getProperty(propertyPrefix.concat(validator)))
                            )
                    );
                }
                form.setFormControlList(formControlList);
                this.formMap.put(formName, form);
            }
        } catch (IOException | ValidatorException e) {
            e.printStackTrace();
            throw new FormException("exception.component.form.properties", e.getCause());
        }
    }

    public static FormFactory getInstance() throws FormException {
        try {
            return Holder.INSTANCE;
        } catch (ExceptionInInitializerError e) {
            throw new FormException("exception.component.form.init", e.getCause());
        }
    }

    public Form create(String formName) throws FormException {
        return this.formMap.get(formName).copyDef();
    }

}