package com.epam.java.rt.lab.dao;

import com.epam.java.rt.lab.dao.sql.OrderBy;
import com.epam.java.rt.lab.dao.sql.Update;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.dao.sql.WildValue;

/**
 *  used to exchange parameters between service layer and dao layer, and through dao methods
 */
public class DaoParameter {

    private Where.Predicate wherePredicate;
    private OrderBy.Criteria[] orderByCriteriaArray;
    private Integer limitOffset;
    private Integer limitCount;
    private WildValue[] wildValue;
    private Update.SetValue[] setValueArray;
//    private boolean makeCache;

    public DaoParameter() {
    }

    public Where.Predicate getWherePredicate() {
        return this.wherePredicate;
    }

    public DaoParameter setWherePredicate(Where.Predicate wherePredicate) {
        this.wherePredicate = wherePredicate;
        return this;
    }

    public OrderBy.Criteria[] getOrderByCriteriaArray() {
        return this.orderByCriteriaArray;
    }

    public DaoParameter setOrderByCriteriaArray(OrderBy.Criteria... orderByCriteriaArray) {
        this.orderByCriteriaArray = orderByCriteriaArray;
        return this;
    }

    public DaoParameter setLimit(Integer offset, Integer count) {
        this.limitOffset = offset;
        this.limitCount = count;
        return this;
    }

    public Integer getLimitOffset() {
        return this.limitOffset;
    }

    public Integer getLimitCount() {
        return this.limitCount;
    }

    public Update.SetValue[] getSetValueArray() {
        return this.setValueArray;
    }

    public DaoParameter setSetValueArray(Update.SetValue... setValueArray) {
        this.setValueArray = setValueArray;
        return this;
    }

//    public boolean isMakeCache() {
//        return makeCache;
//    }
//
//    public DaoParameter setMakeCache(boolean makeCache) {
//        this.makeCache = makeCache;
//        return this;
//    }
}
