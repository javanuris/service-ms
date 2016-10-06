package com.epam.java.rt.lab.web.component.view;

/**
 * category-ms
 */
public class ViewControl {

    private ControlDef controlDef;
    private ControlVal controlVal;

    public ViewControl(String name, String label, String type, String dictionary) {
        this.controlDef = new ControlDef(name, label, type, dictionary);
    }

    ViewControl(ControlDef controlDef) {
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

    public String getLocalePrefix() {
        return controlDef.localePrefix;
    }

    private ControlVal val() {
        if (this.controlVal == null) this.controlVal = new ControlVal();
        return this.controlVal;
    }

    public String getValue() {
        return val().value;
    }

    public void setValue(String value) {
        val().value = value;
    }

    public String getAction() {
        return val().action;
    }

    public void setAction(String action) {
        val().action = action;
    }

    // immutable control definition
    private static class ControlDef {
        private String name;
        private String label;
        private String type;
        private String localePrefix;

        private ControlDef(String name, String label, String type, String localePrefix) {
            this.name = name;
            this.label = label;
            this.type = type;
            this.localePrefix = localePrefix;
        }

    }

    // mutable control values
    private static class ControlVal {
        private String value;
        private String action;

        private ControlVal() {
        }
    }

}
