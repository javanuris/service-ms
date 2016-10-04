package com.epam.java.rt.lab.web.component.form;

import com.epam.java.rt.lab.util.validator.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * category-ms
 */
public class FormControl {

    private ControlDef controlDef;
    private ControlVal controlVal;

    public FormControl(
            String name,
            String label,
            String type,
            String placeholder,
            String action,
            String subAction,
            Validator validator,
            String dictionary
    ) {

        this.controlDef = new ControlDef(
                name,
                label,
                type,
                placeholder,
                action,
                subAction,
                validator,
                dictionary
        );
    }

    FormControl(ControlDef controlDef) {
        this.controlDef = controlDef;
    }

    ControlDef getControlDef() {
        return this.controlDef;
    }

    public String getName() {
        return controlDef.name;
    }

    public String getLabel() {
        return controlDef.label;
    }

    public String getType() {
        return controlDef.type;
    }

    public String getPlaceholder() {
        return controlDef.placeholder;
    }

    public String getAction() {
        return controlDef.action;
    }

    public String getSubAction() {
        return controlDef.subAction;
    }

    public Validator getValidator() {
        return this.controlDef.validator;
    }

    public String getDictionary() {
        return this.controlDef.dictionary;
    }

    private ControlVal val() {
        if (this.controlVal == null) this.controlVal = new ControlVal();
        return this.controlVal;
    }

    public List<SelectValue> getAvailableValueList() {
        return val().availableValueList;
    }

    public void setAvailableValueList(List<SelectValue> availableValueList) {
        val().availableValueList = availableValueList;
    }

    public String getValue() {
        return val().value;
    }

    public void setValue(String value) {
        val().value = value;
    }

    public <T> T getGenericValue() {
        return (T) val().genericValue;
    }

    public <T> void setGenericValue(T genericValue) {
        val().genericValue = genericValue;
    }

    public List<String> getValidationMessageList() {
        return val().validationMessageList;
    }

    public void setValidationMessageList(List<String> validationMessageList) {
        val().validationMessageList = validationMessageList;
    }

    public void addValidationMessage(String validationMessage) {
        if (val().validationMessageList == null)
            val().validationMessageList = new ArrayList<String>();
        val().validationMessageList.add(validationMessage);
    }

    public boolean getIgnoreValidate() {
        return val().ignoreValidate;
    }

    public void setIgnoreValidate(boolean value) {
        val().ignoreValidate = value;
    }

    public String getActionParameters() {
        return val().actionParameters;
    }

    public void setActionParameters(String actionParameters) {
        val().actionParameters = actionParameters;
    }

    // immutable item definition
    private static class ControlDef {

        private String name;
        private String label;
        private String type;
        private String placeholder;
        private String action;
        private String subAction;
        private Validator validator;
        private String dictionary;

        private ControlDef(
                String name,
                String label,
                String type,
                String placeholder,
                String action,
                String subAction,
                Validator validator,
                String dictionary
        ) {
            this.name = name;
            this.label = label;
            this.type = type;
            this.placeholder = placeholder;
            this.action = action;
            this.subAction = subAction;
            this.validator = validator;
            this.dictionary = dictionary;
        }
    }

    // mutable item values
    private static class ControlVal<T> {

        private List<SelectValue> availableValueList = null;
        private String value = null;
        private T genericValue = null;
        private List<String> validationMessageList = null;
        private boolean ignoreValidate = false;
        private String actionParameters;

        private ControlVal() {
        }
    }

    public static class SelectValue {

        private String value;
        private String label;

        public SelectValue(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }

    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("LABEL: ").append(getLabel())
                .append(", NAME: ").append(getName())
                .append(", TYPE: ").append(getType())
                .append(", PLACEHOLDER: ").append(getPlaceholder())
                .append(", ACTION: ").append(getAction())
                .append(", ACTION-PARAMETERS: ").append(getActionParameters())
                .append(", SUB-ACTION: ").append(getSubAction())
                .append(", VALIDATOR: ").append(getValidator())
                .append(", DICTIONARY: ").append(getDictionary())
                .append(", AVAILABLE-VALUE-LIST: ").append(getAvailableValueList())
                .append(", VALUE: ").append(getValue())
                .append(", GENERIC-VALUE: ").append(getGenericValue() == null ? "" : String.valueOf(getGenericValue()))
                .append(", VALIDATION-MESSAGE-LIST: ").append(getValidationMessageList())
                .append(", IGNORE-VALIDATE: ").append(getIgnoreValidate()).toString();
    }
}
