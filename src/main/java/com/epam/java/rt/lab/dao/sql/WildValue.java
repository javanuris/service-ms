package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;

import java.util.List;

/**
 * {@code WildValue} class defines value which should be replaced
 * by wildcard in sql statement and should be set on prepared statement
 */
public class WildValue<T> {

    private static final String WILDCARD = "?";

    /** {@code List} of {@code WildValue} objects */
    private List<WildValue> wildValueList;
    /** Generic value */
    private T val;

    /**
     * Initiates new {@code WildValue} object with defined
     * generic value
     *
     * @param val       generic value
     */
    public WildValue(T val) {
        this.val = val;
    }

    /**
     * Links this object to base single {@code List} of {@code WildValue} objects
     *
     * @param wildValueList     {@code List} of {@code WildValue} objects
     */
    public void link(List<WildValue> wildValueList) {
        this.wildValueList = wildValueList;
    }

    /**
     * Returns generic value of this object
     *
     * @return      generic value
     */
    public T getVal() {
        return val;
    }

    /**
     * Returns wildcard instead value and adds to base single {@code List}
     * of {@code WildValue} objects replaced value
     *
     * @return                  {@code String} wildcard
     * @throws DaoException
     */
    public String makeWildcard() throws DaoException {
        if (this.wildValueList == null)
            throw new DaoException("exception.dao.sql.wild-value-list-empty");
        this.wildValueList.add(this);
        return WILDCARD;
    }

}
