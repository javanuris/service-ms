package com.epam.java.rt.lab.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectValue extends SimpleTagSupport {

    private String entityMethod;
    private Object entityObject;

    public void setEntityMethod(String entityMethod) {
        this.entityMethod = entityMethod;
    }

    public void setEntityObject(Object entityObject) {
        this.entityObject = entityObject;
    }

    private Object invokeItemMethod(Object itemObject, String itemMethod)
            throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        Method method = itemObject.getClass()
                .getMethod("get" + itemMethod.substring(0, 1).toUpperCase()
                        + itemMethod.substring(1));
        return method.invoke(itemObject);
    }

    private String getStringValue() {
        try {
            Object itemObject = this.entityObject;
            for (String itemMethod : this.entityMethod.split("\\.")) {
                itemObject = invokeItemMethod(itemObject, itemMethod);
                if (itemObject == null) return "";
            }
            return String.valueOf(itemObject);
        } catch (NoSuchMethodException | IllegalAccessException
                | InvocationTargetException e) {
            //
        }
        return "";
    }
    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        out.print(getStringValue());
    }

}
