package com.epam.java.rt.lab.util.validator;

import com.epam.java.rt.lab.component.form.Form;
import com.epam.java.rt.lab.component.form.FormControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * service-ms
 */
public class FormValidator {
    private static final Logger logger = LoggerFactory.getLogger(FormValidator.class);

    public static boolean validate(HttpServletRequest req, Form form) {
        logger.debug("VALIDATE");
        boolean result = true;
        for (int i = 0; i < form.getItemArrayLength(); i++) {
            FormControl formControl = form.getItem(i);
            formControl.setValue(req.getParameter(formControl.getLabel()));
            logger.debug("TYPE: {}, LABEL: {}, VALUE: {}",
                    formControl.getValidator().getType(), formControl.getLabel(), formControl.getValue());
            if (!formControl.getIgnoreValidate() && formControl.getValidator() != null) {
                String[] msgArray = formControl.getValidator().validate(formControl.getValue());
                if (msgArray != null && msgArray.length > 0) {
                    result = false;
                    formControl.getValidationMessageList().addAll(Arrays.asList(msgArray));
                }
            }
        }
        return result;
    }
}
