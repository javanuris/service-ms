package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.exception.AppException;

import java.util.List;

import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.util.PropertyManager.QUESTION;

/**
 * {@code WildValue} class defines value which should be replaced
 * by wildcard in sql statement and should be set on prepared statement
 */
public class WildValue<T> {

    /** {@code List} valueOf {@code WildValue} objects */
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
     * @throws AppException
     */
    public String makeWildcard() throws AppException {
        if (this.wildValueList == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        this.wildValueList.add(this);
        return QUESTION;
    }

}
