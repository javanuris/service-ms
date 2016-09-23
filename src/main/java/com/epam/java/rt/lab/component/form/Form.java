package com.epam.java.rt.lab.component.form;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * service-ms
 */
public class Form implements Iterable<FormControl> {
    private Def fieldDef;
    private Val fieldVal;
    private FormControl[] formControlArray;

    Form(String name, String actionUri) {
        this.fieldDef = new Def();
        fieldDef.name = name;
        fieldDef.actionUri = actionUri;
    }

    Form copyDef() {
        // this method makes possible to use Form in multithreading, because client receives copy
        // with definitions and newly created fields or objects for mutable values
        Form form = new Form(this.fieldDef.name, this.fieldDef.actionUri);
        List<FormControl> formControlList = new ArrayList<>();
        for (FormControl formControl : this.formControlArray) formControlList.add(new FormControl(formControl.getControlDef()));
        FormControl[] formControlArray = new FormControl[formControlList.size()];
        form.setFormControlArray(formControlList.toArray(formControlArray));
        return form;
    }

    public String getName() {
        return this.fieldDef.name;
    }

    public String getAction() {
        return val().actionParameterString.length() == 0 ? this.fieldDef.actionUri :
                this.fieldDef.actionUri.concat("?").concat(val().actionParameterString);
    }

    private Val val() {
        if (this.fieldVal == null) this.fieldVal = new Val();
        return this.fieldVal;
    }

    public void setActionParameterString(String actionParameterString) {
        val().actionParameterString = actionParameterString;
    }

    public int getItemArrayLength() {
        return this.formControlArray != null ? this.formControlArray.length : -1;
    }

    public FormControl getItem(int index) {
        return this.formControlArray != null && 0 <= index && index < this.formControlArray.length ? this.formControlArray[index] : null;
    }

    protected void setFormControlArray(FormControl[] formControlArray) {
        this.formControlArray = formControlArray;
    }

    @Override
    public Iterator<FormControl> iterator() {
        return new Iterator<FormControl>() {
            private int index = 0;

            public boolean hasNext(){
                return index < formControlArray.length;
            }

            public FormControl next(){
                return formControlArray[index++];
            }

            public void remove(){
                throw new UnsupportedOperationException();
            }
        };
    }

    // immutable definition
    private static class Def {
        private String name;
        private String actionUri;

        private Def() {
        }
    }

    // mutable values
    private static class Val {
        private String actionParameterString = "";

        private Val() {
        }
    }

//    // form item
//    public static class FormControl {
//        private ItemDef itemDef;
//        private ItemVal itemVal;
//
//        public FormControl(String label, String type, String placeholder, ValidatorFactory.Validator validator) {
//            this.itemDef = new ItemDef(label, type, placeholder, validator);
//        }
//
//        FormControl(ItemDef itemDef) {
//            this.itemDef = itemDef;
//        }
//
//        ItemDef getControlDef() {
//            return this.itemDef;
//        }
//
//        public String getLabel() {
//            return itemDef.label;
//        }
//
//        public String getType() {
//            return itemDef.type;
//        }
//
//        public String getPlaceholder() {
//            return itemDef.placeholder;
//        }
//
//        public ValidatorFactory.Validator getValidator() {
//            return this.itemDef.validator;
//        }
//
//        private ItemVal val() {
//            if (this.itemVal == null) this.itemVal = new ItemVal();
//            return this.itemVal;
//        }
//
//        public String[] getAvailableValueArray() {
//            return val().availableValueArray;
//        }
//
//        public void setAvailableValueArray(String[] availableValueArray) {
//            val().availableValueArray = availableValueArray;
//        }
//
//        public String getValue() {
//            return val().value;
//        }
//
//        public void setValue(String value) {
//            val().value = value;
//        }
//
//        public String getGenericValue() {
//            return (String) val().genericValue;
//        }
//
//        public <T> void setGenericValue(T genericValue) {
//            val().genericValue = genericValue;
//        }
//
//        public String[] getValidationMessageArray() {
//            return val().validationMessageArray;
//        }
//
//        public void setValidationMessageArray(String[] validationMessageArray) {
//            val().validationMessageArray = validationMessageArray;
//        }
//
//        public boolean getIgnoreValidate() {
//            return val().ignoreValidate;
//        }
//
//        public void setIgnoreValidate(boolean value) {
//            val().ignoreValidate = value;
//        }
//    }
//
//    // immutable item definition
//    private static class ItemDef {
//        private String label = null;
//        private String type = null;
//        private String placeholder = null;
//        private ValidatorFactory.Validator validator;
//
//        private ItemDef(String label, String type, String placeholder, ValidatorFactory.Validator validator) {
//            this.label = label;
//            this.type = type;
//            this.placeholder = placeholder;
//            this.validator = validator;
//        }
//    }
//
//    // mutable item values
//    private static class ItemVal<T> {
//        private String[] availableValueArray = null;
//        private String value = null;
//        private T genericValue = null;
//        private String[] validationMessageArray = null;
//        private boolean ignoreValidate = false;
//
//        private ItemVal() {
//        }
//    }
}
