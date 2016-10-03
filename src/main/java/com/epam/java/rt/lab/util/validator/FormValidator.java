package com.epam.java.rt.lab.util.validator;

import com.epam.java.rt.lab.web.component.form.Form;
import com.epam.java.rt.lab.web.component.form.FormControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            if (!formControl.getIgnoreValidate()) {
                if (formControl.getValidator() != null) {
                    String[] msgArray = formControl.getValidator().validate(formControl.getValue());
                    if (msgArray != null && msgArray.length > 0) {
                        result = false;
                        if (formControl.getValidationMessageList() == null)
                            formControl.setValidationMessageList(new ArrayList<>());
                        formControl.getValidationMessageList().addAll(Arrays.asList(msgArray));
                    }
                }
                if ("select".equals(formControl.getType())) {
                    if (formControl.getAvailableValueList() != null) {
                        boolean contains = false;
                        for (FormControl.SelectValue selectValue : formControl.getAvailableValueList()) {
                            if (selectValue.getValue().equals(formControl.getValue())) {
                                contains = true;
                                break;
                            }
                        }
                        if (!contains) {
                            result = false;
                            formControl.addValidationMessage("message.validation.select");
                        }
                    }
                } else if ("multi-select".equals(formControl.getType())) {
                    if (formControl.getAvailableValueList() != null) {
                        List<String> uriList = new ArrayList<>();
                        String[] valueList = req.getParameterValues(formControl.getName());
                        System.out.println(">>> " + valueList.length);
                        formControl.setGenericValue(valueList);
                        for (FormControl.SelectValue selectValue : formControl.getAvailableValueList())
                            for (String value : valueList)
                                if (selectValue.getValue().equals(formControl.getValue()))
                                    uriList.add(value);
                        System.out.println(">>> " + uriList.size());
                        if (uriList.size() != valueList.length) {
                            result = false;
                            formControl.addValidationMessage("message.validation.select");
                        }
                    }
                }
            }
        }
        return result;
    }
}
