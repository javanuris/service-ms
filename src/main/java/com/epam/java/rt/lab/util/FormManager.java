package com.epam.java.rt.lab.util;

import com.epam.java.rt.lab.component.FormComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * service-ms
 */
public class FormManager {
    private static final Logger logger = LoggerFactory.getLogger(FormManager.class);
//    private static final Map<String, List<FormItemValidator>> formItemValidatorMap = new HashMap<>();

    private FormManager() {
    }

    public static void updateFormItemValidatorMap() {

    }

    public static boolean isOnlyDigits(String value) {
        if (value == null) return false;
        return value.matches("^-?[0-9]+(\\.[0-9]+)?$");
    }

    public static void setValues(HttpServletRequest req, FormComponent formComponent) {
        logger.debug("SET-VALUES");
        for (int i = 0; i < formComponent.getItemArrayLength(); i++) {
            FormComponent.Item formItem = formComponent.getItem(i);
            formItem.setValue(req.getParameter(formItem.getLabel()));
        }
    }

    public static boolean setValuesAndValidate(HttpServletRequest req, FormComponent formComponent) {
        logger.debug("SET-VALUES-AND-VALIDATE");
//        try {
//            List<FormItemValidator> formItemValidatorList;
            for (int i = 0; i < formComponent.getItemArrayLength(); i++) {
                FormComponent.Item formItem = formComponent.getItem(i);
                formItem.setValue(req.getParameter(formItem.getLabel()));
                logger.debug("{} = {}", formItem.getLabel(), formItem.getValue());
//                formItemValidatorList = FormManager.formItemValidatorMap.get(formItem.getLabel());
//                if (formItemValidatorList != null) {
//                    for (FormItemValidator formItemValidator : formItemValidatorList) {
//                        if (!formItemValidator.validate(formItem.getValue())) return false;
//                    }
//                }
            }
            return true;
//        } catch (ValidationException e) {
//            return false;
//        }
    }
//
//    private static String validateString(FormComponent.FormItem formItem, String validRegex) {
//        String value = String.valueOf(formItem.getValue());
//        // setValuesAndValidate regex
//        return value;
//    }
//
//    public static Integer validateInteger(FormComponent.FormItem formItem) {
//        try {
//            Integer value = Integer.valueOf(formItem.getValue());
//            return value;
//        } catch (NumberFormatException e) {
//            //
//        }
//        return null;
//    }
//
//    public static Integer validateInteger(FormComponent.FormItem formItem, Integer lowBound, Integer upperBound) {
//        try {
//            Integer value = Integer.valueOf(formItem.getValue());
//            if (lowBound.compareTo(value) <= 0 && upperBound.compareTo(value) >= 0) return value;
//        } catch (NumberFormatException e) {
//            //
//        }
//        return null;
//    }
//
//    public static Long validateLong(FormComponent.FormItem formItem) {
//        try {
//            Long value = Long.valueOf(formItem.getValue());
//            return value;
//        } catch (NumberFormatException e) {
//            //
//        }
//        return null;
//    }
//
//    public static Long validateLong(FormComponent.FormItem formItem, Long lowBound, Long upperBound) {
//        try {
//            Long value = Long.valueOf(formItem.getValue());
//            if (lowBound.compareTo(value) <= 0 && upperBound.compareTo(value) >= 0) return value;
//        } catch (NumberFormatException e) {
//            //
//        }
//        return null;
//    }
//
//    public static Double validateDouble(FormComponent.FormItem formItem) {
//        try {
//            Double value = Double.valueOf(formItem.getValue());
//            return value;
//        } catch (NumberFormatException e) {
//            //
//        }
//        return null;
//    }
//
//    public static Double validateDouble(FormComponent.FormItem formItem, Double lowBound, Double upperBound) {
//        try {
//            Double value = Double.valueOf(formItem.getValue());
//            if (lowBound.compareTo(value) <= 0 && upperBound.compareTo(value) >= 0) return value;
//        } catch (NumberFormatException e) {
//            //
//        }
//        return null;
//    }
//
//    public static class FormItemValidator<T> {
//        private Method method;
//        private T[] argumentArray;
//
//        public boolean validate(String value) throws ValidationException {
//            try {
//                return (boolean) method.invoke(null, argumentArray);
//            } catch (IllegalAccessException | InvocationTargetException e) {
//                throw new ValidationException(e.getMessage());
//            }
//        }
//
//        public void defineValidateMethod(String methodName, T[] argumentArray) throws ValidationException {
//            try {
//                List<Class<?>> argumentClassList = new ArrayList<>();
//                for (T argument : argumentArray) argumentClassList.add(argument.getClass());
//                method = FormManager.class.getMethod(methodName, (Class<?>[]) argumentClassList.toArray());
//            } catch (NoSuchMethodException e) {
//                throw new ValidationException(e.getMessage());
//            }
//        }
//
//    }

}