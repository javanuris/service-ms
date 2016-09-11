package com.epam.java.rt.lab.dao.query;

import java.util.List;

/**
 * service-ms
 */
public class Set<T> {
    public String name;
    public T value;

    public Set(String name, T value) {
        this.name = SqlAdapter.convertName(name);
        this.value = value;
    }

    static String setListToString(List<Set> setList) {
        boolean firstSet = true;
        StringBuilder result = new StringBuilder();
        for (Set set : setList) {
            if (firstSet) {
                firstSet = false;
            } else {
                result.append(", ");
            }
            result.append(set.name).append(" = ?");
        }
        return result.toString();
    }

}
