package com.epam.java.rt.lab.web.component;

import com.epam.java.rt.lab.web.validator.Validator;

import java.util.List;

public class FormControlValue {
    private String value;
    private List<String> availableValueList;
    private List<String> validationMessageList;

    public FormControlValue() {
    }

    public FormControlValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getAvailableValueList() {
        return availableValueList;
    }

    public void setAvailableValueList(List<String> availableValueList) {
        this.availableValueList = availableValueList;
    }

    public List<String> getValidationMessageList() {
        return validationMessageList;
    }

    public void setValidationMessageList(List<String> validationMessageList) {
        this.validationMessageList = validationMessageList;
    }

}
