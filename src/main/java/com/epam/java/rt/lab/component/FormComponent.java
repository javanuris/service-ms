package com.epam.java.rt.lab.component;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * service-ms
 */
public class FormComponent implements Iterable<FormComponent.Item> {
    private static ConcurrentHashMap<String, FormComponent> formComponentMap = new ConcurrentHashMap<>();
    private AtomicInteger status = new AtomicInteger();
    private Def fieldDef;
    private Val fieldVal;
    private Item[] itemArray;

    private FormComponent(String name, String actionUri) {
        this.status.set(0);
        this.fieldDef = new Def();
        fieldDef.name = name;
        fieldDef.actionUri = actionUri;
    }

    public static int getStatus(String name, String actionUri, long millis) {
        FormComponent newForm = new FormComponent(name, actionUri);
        FormComponent mapForm = FormComponent.formComponentMap.putIfAbsent(name, newForm);
        if (mapForm == null) mapForm = newForm;
        System.out.println("mapForm = " + mapForm);
        return mapForm.status.get() == 0 ? mapForm.status.getAndSet(-1) : waitStatus(mapForm, millis);
    }

    private static int waitStatus(FormComponent formComponent, long millis) {
        try {
            long startWait = System.currentTimeMillis();
            while (formComponent.status.get() != 1 && System.currentTimeMillis() - startWait < millis) Thread.sleep(10);
        } catch (InterruptedException e) {
            //
        }
        return formComponent.status.get();
    }

    public static FormComponent get(String name) {
        return FormComponent.formComponentMap.get(name);
    }

    public static FormComponent set(String name, Item... itemArray) {
        System.out.println("1");
        FormComponent formComponent = FormComponent.formComponentMap.get(name);
        System.out.println("2");
        if (formComponent.status.get() != -1) return null;
        System.out.println("3");
        formComponent.itemArray = itemArray;
        System.out.println("4");
        formComponent.status.set(1);
        System.out.println("5");
        return formComponent;
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
        return this.itemArray != null ? this.itemArray.length : -1;
    }

    public Item getItem(int index) {
        return this.itemArray != null && 0 <= index && index < this.itemArray.length ? this.itemArray[index] : null;
    }

    @Override
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private int index = 0;

            public boolean hasNext(){
                System.out.println(index);
                return index < itemArray.length;
            }
            public Item next(){
                System.out.println(itemArray[index].getType());
                return itemArray[index++];
            }

            public void remove(){
                throw new UnsupportedOperationException();
            }
        };
    }

    // immutable definition
    private static class Def {
        String name;
        String actionUri;

        public Def() {
        }
    }

    // mutable values
    private static class Val {
        String actionParameterString = "";

        public Val() {
        }
    }

    // form item
    public static class Item {
        ItemDef itemDef;
        ItemVal itemVal;

        public Item(String label, String type, String placeholder) {
            this.itemDef = new ItemDef(label, type, placeholder);
        }

        public String getLabel() {
            return itemDef.label;
        }

        public String getType() {
            return itemDef.type;
        }

        public String getPlaceholder() {
            return itemDef.placeholder;
        }

        private ItemVal val() {
            if (this.itemVal == null) this.itemVal = new ItemVal();
            return this.itemVal;
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

        public String[] getValidationMessageArray() {
            return val().validationMessageArray;
        }

        public void setValidationMessageArray(String[] validationMessageArray) {
            val().validationMessageArray = validationMessageArray;
        }
    }

    // immutable item definition
    private static class ItemDef {
        String label = "";
        String type = "";
        String placeholder = "";

        public ItemDef(String label, String type, String placeholder) {
            this.label = label;
            this.type = type;
            this.placeholder = placeholder;
        }
    }

    // mutable item values
    private static class ItemVal {
        String[] availableValueArray = null;
        String value = "";
        String[] validationMessageArray = null;

        public ItemVal() {
        }
    }
}
