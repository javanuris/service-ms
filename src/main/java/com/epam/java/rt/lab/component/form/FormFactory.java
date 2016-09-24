package com.epam.java.rt.lab.component.form;

import com.epam.java.rt.lab.component.ComponentException;
import com.epam.java.rt.lab.util.StringArray;
import com.epam.java.rt.lab.util.validator.ValidatorException;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;

import java.io.IOException;
import java.util.*;

/**
 * service-ms
 */
public class FormFactory {
    private static class Holder { // Initialization-on-demand holder
        private static final FormFactory INSTANCE = new FormFactory();
    }

    public static FormFactory getInstance() {
        return Holder.INSTANCE;
    }

    private static final String FORMS = "forms";
    private static final String FORM_ACTION = ".action";
    private static final String FORM_CONTROLS = ".controls";
    private static final String CONTROL_LABEL = ".label";
    private static final String CONTROL_TYPE = ".type";
    private static final String CONTROL_PLACEHOLDER = ".placeholder";
    private static final String CONTROL_ACTION = ".action";
    private static final String CONTROL_SUB_ACTION = ".sub-action";
    private static final String CONTROL_VALIDATOR = ".validator";

    private Map<String, Form> formMap;
    private Throwable constructorThrowable;

    private FormFactory() {
        loadProperties();
    }

    private void loadProperties() {
        Properties formProperties = new Properties();
        try {
            formProperties.load(FormFactory.class.getClassLoader().getResourceAsStream("form.properties"));
            this.formMap = new HashMap<>();
            for (String formName : StringArray.splitSpaceLessNames(formProperties.getProperty(FORMS), ",")) {
                Form form = new Form(formName, formProperties.getProperty(formName.concat(".").concat(FORM_ACTION)));
                List<FormControl> formControlList = new ArrayList<>();
                for (String controlName : StringArray.splitSpaceLessNames(formProperties.getProperty(formName), ",")) {
                    String propertyPrefix = formName.concat(".").concat(controlName).concat(".");
                    formControlList.add(
                            new FormControl(
                                    controlName,
                                    formProperties.getProperty(propertyPrefix.concat(CONTROL_LABEL)),
                                    formProperties.getProperty(propertyPrefix.concat(CONTROL_TYPE)),
                                    formProperties.getProperty(propertyPrefix.concat(CONTROL_PLACEHOLDER)),
                                    formProperties.getProperty(propertyPrefix.concat(CONTROL_ACTION)),
                                    formProperties.getProperty(propertyPrefix.concat(CONTROL_SUB_ACTION)),
                                    ValidatorFactory.getInstance()
                                            .create(formProperties.getProperty(propertyPrefix.concat(CONTROL_VALIDATOR)))
                            )
                    );
                }
                form.setFormControlArray((FormControl[]) formControlList.toArray());
                this.formMap.put(formName, form);
            }
        } catch (IOException | ValidatorException e) {
            this.constructorThrowable = e.getCause();
        }
    }

    public Form create(String formName) throws FormException {
        Form form = this.formMap.get(formName);
        if (form != null) return form.copyDef();
        throw new FormException("exception.component.form.form-factory.create", this.constructorThrowable);
    }
}
