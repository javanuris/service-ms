package com.epam.java.rt.lab.util.validator;

import com.epam.java.rt.lab.web.component.form.Form;
import com.epam.java.rt.lab.web.component.form.FormControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * service-ms
 */
public class FormValidator {

    private static final Logger logger = LoggerFactory.getLogger(FormValidator.class);

    public static boolean validate(HttpServletRequest req, Form form) {
        logger.debug("VALIDATE");
        boolean result = true;
        for (int i = 0; i < form.getItemListSize(); i++) {
            FormControl formControl = form.getItem(i);
            logger.debug("{} = {}", formControl.getName(), formControl.getValue());
            formControl.setValue(req.getParameter(formControl.getName()));
            logger.debug("TYPE: {}, LABEL: {}, VALUE: {}",
                    formControl.getValidator() == null ? null : formControl.getValidator().getType(), formControl.getLabel(), formControl.getValue());
            if (!formControl.getIgnoreValidate() && formControl.getValidator() != null) {
                String[] msgArray = formControl.getValidator().validate(formControl.getValue());
                if (msgArray != null && msgArray.length > 0) {
                    result = false;
                    if (formControl.getValidationMessageList() == null)
                        formControl.setValidationMessageList(new ArrayList<>());
                    formControl.getValidationMessageList().addAll(Arrays.asList(msgArray));
                }
            }
        }
        return result;
    }
}
