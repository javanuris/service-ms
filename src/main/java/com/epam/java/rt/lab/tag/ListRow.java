package com.epam.java.rt.lab.tag;

import com.epam.java.rt.lab.component.ListComponent;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * service-ms
 */
public class ListRow extends SimpleTagSupport {
    List<ListComponent.ListColumn> listColumnList;
    Object entityObject;

    public void setListColumnList(List<ListComponent.ListColumn> listColumnList) {
        this.listColumnList = listColumnList;
    }

    public void setEntityObject(Object entityObject) {
        this.entityObject = entityObject;
    }

    private Object invokeEntityMethod(Object entityObject, String entityMethod)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = entityObject.getClass()
                .getMethod("get".concat(entityMethod.substring(0, 1).toUpperCase()).concat(entityMethod.substring(1)));
        return method.invoke(entityObject);
    }

    private String getStringValue(ListComponent.ListColumn listColumn) {
        try {
            Object itemObject = entityObject;
            for (String item : listColumn.getFieldName().split("\\.")) {
                itemObject = invokeEntityMethod(itemObject, item);
                if (itemObject == null) return "";
            }
            return String.valueOf(itemObject);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        for (ListComponent.ListColumn listColumn : listColumnList) {
            out.println("<div class=\"col-xs-".concat(String.valueOf(listColumn.getWidth())).concat("\">"));
            out.println(getStringValue(listColumn));
            out.println("</div>");
        }
    }
}
