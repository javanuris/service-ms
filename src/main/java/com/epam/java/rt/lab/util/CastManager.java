package com.epam.java.rt.lab.util;

import java.util.ArrayList;
import java.util.List;

/**
 * category-ms
 */
public class CastManager {

    @SuppressWarnings("Cast Object to List<T>")
    public static <T> List<T> getList(Object object, Class<T> targetItemClass) {
        if (object == null || !(object instanceof ArrayList)) return null;
        if (!((List<?>) object).getClass().isAssignableFrom(targetItemClass)) return null;
        return (List<T>) object;
    }

    @SuppressWarnings("Cast Object to Object[]")
    public static <T> Object[] getArray(Object object, Class<T> targetItemClass) {
        if (object == null || !object.getClass().isArray()) return null;
        if (!((Class<?>[]) object).getClass().isAssignableFrom(targetItemClass)) return null;
        return (Class<T>[]) object;
    }



}
