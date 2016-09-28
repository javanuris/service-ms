package com.epam.java.rt.lab.dao;

import com.epam.java.rt.lab.dao.sql.OrderBy;
import com.epam.java.rt.lab.dao.sql.Where;

import java.util.List;

/**
 *  used to exchange parameters between service layer and dao layer, and through dao methods
 */
public class DaoParameter {

    private Where.Predicate wherePredicate;
    private OrderBy.Criteria[] orderByCriteriaArray;
    private Integer limitOffset;
    private Integer limitCount;

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

}
