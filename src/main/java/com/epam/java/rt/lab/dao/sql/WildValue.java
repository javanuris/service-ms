package com.epam.java.rt.lab.dao.sql;

import java.util.List;

/**
 * service-ms
 */
public class WildValue<T> {

    private static final String WILDCARD = "?";

    private List<WildValue> wildValueList;
    private T val;

    public WildValue(T val) {
        this.val = val;
    }

    public void link(List<WildValue> wildValueList) {
        this.wildValueList = wildValueList;
    }

    public T getVal() {
        return val;
    }

    public String makeWildcard() {
        this.wildValueList.add(this);
        return WILDCARD;
    }

}
