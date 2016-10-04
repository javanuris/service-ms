package com.epam.java.rt.lab.web.component.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * category-ms
 */
public class Form implements Iterable<FormControl> {
    private Def fieldDef;
    private Val fieldVal;
    private List<FormControl> formControlList;

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
        for (FormControl formControl : this.formControlList)
            formControlList.add(new FormControl(formControl.getControlDef()));
        form.setFormControlList(formControlList);
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

    public int getItemListSize() {
        return this.formControlList != null ? this.formControlList.size() : -1;
    }

    public FormControl getItem(int index) {
        return this.formControlList != null && 0 <= index && index < this.formControlList.size() ? this.formControlList.get(index) : null;
    }

    protected void setFormControlList(List<FormControl> formControlList) {
        this.formControlList = formControlList;
    }

    @Override
    public Iterator<FormControl> iterator() {
        return new Iterator<FormControl>() {
            private int index = 0;

            public boolean hasNext(){
                return index < formControlList.size();
            }

            public FormControl next(){
                return formControlList.get(index++);
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("NAME: ").append(getName()).append(", ACTION: ").append(getAction());
        for (FormControl formControl : this) result.append("\nFORM-CONTROL: ").append(formControl);
        return result.toString();
    }

}
