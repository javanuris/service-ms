package com.epam.java.rt.lab.component;

/**
 * service-ms
 */
public class FormComponent {
    private String name;
    private String action;
    private FormItem[] formItemArray;

    public FormComponent(String name, String action, FormItem ... formItemArray) {
        this.name = name;
        this.action = action;
        this.formItemArray = formItemArray;
    }

    public String getName() {
        return name;
    }

    public String getAction() {
        return action;
    }

    public FormItem[] getFormItemArray() {
        return formItemArray;
    }

    public void clear() {
        for (FormItem formItem : this.formItemArray) {
            formItem.setValue("");
            formItem.setValidationMessageArray(null);
        }
    }

    public void clearValidationMessageArray() {
        for (FormItem formItem : this.formItemArray)
            formItem.setValidationMessageArray(null);
    }

    public static class FormItem {
        private String label;
        private String type;
        private String placeholder;
        private String[] availableValueArray;
        private String value;
        private String[] validationMessageArray;

        public FormItem(String label, String type, String placeholder, String... availableValueArray) {
            this.label = label;
            this.type = type;
            this.placeholder = placeholder;
            this.availableValueArray = availableValueArray;
        }

        public String getLabel() {
            return label;
        }

        public String getType() {
            return type;
        }

        public String getPlaceholder() {
            return placeholder;
        }

        public String[] getAvailableValueArray() {
            return availableValueArray;
        }

        public void setValue(String value) { this.value = value; }

        public <T> FormItem setValueAndReturn(T value) {
            this.value = (String) value;
            return this;
        }

        public String getValue() { return value; }

        public void setValidationMessageArray(String[] validationMessageArray) {
            this.validationMessageArray = validationMessageArray;
        }

        public String[] getValidationMessageArray() {
            return validationMessageArray;
        }

    }
}
