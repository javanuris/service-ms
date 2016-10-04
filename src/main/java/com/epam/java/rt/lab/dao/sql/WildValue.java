package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;

import java.util.List;

/**
 * category-ms
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

    public String makeWildcard() throws DaoException {
        if (this.wildValueList == null)
            throw new DaoException("exception.dao.sql.wild-value-list-empty");
        this.wildValueList.add(this);
        return WILDCARD;
    }

}
