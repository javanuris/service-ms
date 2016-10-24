package com.epam.java.rt.lab.web.component;

import java.util.List;

public class FormControlValue {
    private String value;
    private List<SelectValue> availableValueList;
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

    public List<SelectValue> getAvailableValueList() {
        return availableValueList;
    }

    public void setAvailableValueList(List<SelectValue> availableValueList) {
        this.availableValueList = availableValueList;
    }

    public List<String> getValidationMessageList() {
        return validationMessageList;
    }

    public void setValidationMessageList(List<String> validationMessageList) {
        this.validationMessageList = validationMessageList;
    }

}
