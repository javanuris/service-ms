package com.epam.java.rt.lab.web.component.form;

import com.epam.java.rt.lab.util.StringArray;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.util.validator.ValidatorException;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;

import java.io.IOException;
import java.util.*;

/**
 * service-ms
 */
public final class FormFactory {

    private static final String SIGN_POINT = ".";
    private static final String SIGN_COMMA = ",";
    private static final String FORMS = "forms";
    private static final String FORM_ACTION = ".action";
    private static final String FORM_CONTROLS = ".controls";
    private static final String CONTROL_LABEL = ".label";
    private static final String CONTROL_TYPE = ".type";
    private static final String CONTROL_PLACEHOLDER = ".placeholder";
    private static final String CONTROL_ACTION = ".action";
    private static final String CONTROL_SUB_ACTION = ".sub-action";
    private static final String CONTROL_VALIDATOR = ".validator";

    private static Map<String, Form> formMap = new HashMap<>();

    private static void loadProperties() throws FormException {
        Properties formProperties = new Properties();
        try {
            formProperties.load(FormFactory.class.getClassLoader().getResourceAsStream("form.properties"));
            FormFactory.formMap.clear();
            for (String formName : StringArray.splitSpaceLessNames(formProperties.getProperty(FORMS), SIGN_COMMA)) {
                Form form = new Form(formName, formProperties.getProperty(formName.concat(FORM_ACTION)));
                List<FormControl> formControlList = new ArrayList<>();
                for (String controlName : StringArray.splitSpaceLessNames(formProperties.getProperty(formName.concat(FORM_CONTROLS)), SIGN_COMMA)) {
                    String propertyPrefix = formName.concat(SIGN_POINT).concat(controlName);
                    formControlList.add(
                            new FormControl(
                                    propertyPrefix,
                                    formProperties.getProperty(propertyPrefix.concat(CONTROL_LABEL)),
                                    formProperties.getProperty(propertyPrefix.concat(CONTROL_TYPE)),
                                    formProperties.getProperty(propertyPrefix.concat(CONTROL_PLACEHOLDER)),
                                    formProperties.getProperty(propertyPrefix.concat(CONTROL_ACTION)),
                                    formProperties.getProperty(propertyPrefix.concat(CONTROL_SUB_ACTION)),
                                    ValidatorFactory.create(formProperties.getProperty(propertyPrefix.concat(CONTROL_VALIDATOR)))
                            )
                    );
                }
                form.setFormControlList(formControlList);
                FormFactory.formMap.put(formName, form);
            }
        } catch (IOException | ValidatorException e) {
            e.printStackTrace();
            throw new FormException("exception.component.form.properties", e.getCause());
        }
    }

    public static Form create(String formName) throws FormException {
        if (FormFactory.formMap.size() == 0) FormFactory.loadProperties();
        Form form = FormFactory.formMap.get(formName);
        if (form != null) return form.copyDef();
        throw new FormException("exception.component.form.form-factory.create");
    }

}
