package com.epam.java.rt.lab.component.form;

import com.epam.java.rt.lab.util.validator.Validator;

/**
 * service-ms
 */
public class FormControl {
    private ControlDef controlDef;
    private ControlVal controlVal;

    public FormControl(
            String label,
            String type,
            String placeholder,
            String action,
            String subAction,
            Validator validator
    ) {

        this.controlDef = new ControlDef(
                label,
                type,
                placeholder,
                action,
                subAction,
                validator
        );
    }

    FormControl(ControlDef controlDef) {
        this.controlDef = controlDef;
    }

    ControlDef getControlDef() {
        return this.controlDef;
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

    private ControlVal val() {
        if (this.controlVal == null) this.controlVal = new ControlVal();
        return this.controlVal;
    }

    public String[] getAvailableValueArray() {
        return val().availableValueArray;
    }

    public void setAvailableValueArray(String[] availableValueArray) {
        val().availableValueArray = availableValueArray;
    }

    public String getValue() {
        return val().value;
    }

    public void setValue(String value) {
        val().value = value;
    }

    public String getGenericValue() {
        return (String) val().genericValue;
    }

    public <T> void setGenericValue(T genericValue) {
        val().genericValue = genericValue;
    }

    public String[] getValidationMessageArray() {
        return val().validationMessageArray;
    }

    public void setValidationMessageArray(String[] validationMessageArray) {
        val().validationMessageArray = validationMessageArray;
    }

    public boolean getIgnoreValidate() {
        return val().ignoreValidate;
    }

    public void setIgnoreValidate(boolean value) {
        val().ignoreValidate = value;
    }


    // immutable item definition
    private static class ControlDef {
        private String label = null;
        private String type = null;
        private String placeholder = null;
        private String action = null;
        private String subAction = null;
        private Validator validator = null;

        private ControlDef(
                String label,
                String type,
                String placeholder,
                String action,
                String subAction,
                Validator validator
        ) {
            this.label = label;
            this.type = type;
            this.placeholder = placeholder;
            this.action = action;
            this.subAction = subAction;
            this.validator = validator;
        }
    }

    // mutable item values
    private static class ControlVal<T> {
        private String[] availableValueArray = null;
        private String value = null;
        private T genericValue = null;
        private String[] validationMessageArray = null;
        private boolean ignoreValidate = false;

        private ControlVal() {
        }
    }
}
