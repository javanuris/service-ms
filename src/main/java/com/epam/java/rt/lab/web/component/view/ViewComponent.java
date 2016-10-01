package com.epam.java.rt.lab.web.component.view;

/**
 * service-ms
 */
public class ViewComponent {
    private ViewItem[] viewItemArray;

    public ViewComponent(ViewItem... viewItemArray) {
        this.viewItemArray = viewItemArray;
    }

    public ViewItem[] getViewItemArray() {
        return viewItemArray;
    }

    public static class ViewItem {
        private String label;
        private String type;
        private String value;

        public ViewItem(String label, String type, String value) {
            this.label = label;
            this.type = type;
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setValue(String value) { this.value = value; }

        public String getValue() { return value; }

    }
}
