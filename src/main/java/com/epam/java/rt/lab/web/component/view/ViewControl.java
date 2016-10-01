package com.epam.java.rt.lab.web.component.view;

/**
 * service-ms
 */
public class ViewControl {

    private ControlDef controlDef;
    private ControlVal controlVal;

    public ViewControl(String name, String label, String type) {
        this.controlDef = new ControlDef(name, label, type);
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

        private ControlDef(String name, String label, String type) {
            this.name = name;
            this.label = label;
            this.type = type;
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
