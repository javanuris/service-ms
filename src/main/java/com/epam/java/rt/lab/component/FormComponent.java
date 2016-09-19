package com.epam.java.rt.lab.component;

import com.epam.java.rt.lab.util.FormManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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
        return FormComponent.formComponentMap.get(name).copyDef();
    }

    public static FormComponent set(String name, Item... itemArray) {
        FormComponent formComponent = FormComponent.formComponentMap.get(name);
        if (formComponent.status.get() != -1) return null;
        formComponent.itemArray = itemArray;
        formComponent.status.set(1);
        return formComponent.copyDef();
    }

    private FormComponent copyDef() {
        // this method makes possible to use FormComponent in multithreading, because client receives copy
        // with definitions and newly created fields or objects for mutable values
        FormComponent formComponent = new FormComponent(this.fieldDef.name, this.fieldDef.actionUri);
        List<Item> itemList = new ArrayList<>();
        for (Item item : this.itemArray) itemList.add(new Item(item.getItemDef()));
        Item[] itemArray = new Item[itemList.size()];
        formComponent.setItemArray(itemList.toArray(itemArray));
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

    private void setItemArray(Item[] itemArray) {
        this.itemArray = itemArray;
    }

    @Override
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private int index = 0;

            public boolean hasNext(){
                return index < itemArray.length;
            }
            public Item next(){
                return itemArray[index++];
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

    // form item
    public static class Item {
        private ItemDef itemDef;
        private ItemVal itemVal;

        public Item(String label, String type, String placeholder) {
            this.itemDef = new ItemDef(label, type, placeholder);
        }

        Item(ItemDef itemDef) {
            this.itemDef = itemDef;
        }

        ItemDef getItemDef() {
            return this.itemDef;
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

        public ItemDef getValidatorList() {
            return this.itemDef;
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
    }

    // immutable item definition
    private static class ItemDef implements Iterable<FormManager.Validator> {
        private String label = null;
        private String type = null;
        private String placeholder = null;
        private List<FormManager.Validator> validatorList;

        private ItemDef(String label, String type, String placeholder, FormManager.Validator... validatorArray) {
            this.label = label;
            this.type = type;
            this.placeholder = placeholder;
            if (validatorArray.length > 0) {
                this.validatorList = new ArrayList<>();
                Collections.addAll(this.validatorList, validatorArray);
            }
        }

        @Override
        public Iterator<FormManager.Validator> iterator() {
            return new Iterator<FormManager.Validator>() {
                private int index = 0;

                public boolean hasNext() {
                    return index < validatorList.size();
                }

                public FormManager.Validator next() {
                    return validatorList.get(index++);
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

    }

    // mutable item values
    private static class ItemVal<T> {
        private String[] availableValueArray = null;
        private String value = null;
        private T genericValue = null;
        private String[] validationMessageArray = null;

        private ItemVal() {
        }
    }
}
