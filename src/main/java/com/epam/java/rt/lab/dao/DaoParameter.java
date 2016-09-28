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

    public DaoParameter setWherePredicate(Where.Predicate predicate) {
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

    public Integer getLimitOffset() {
        return this.limitOffset;
    }

    public DaoParameter setLimitOffset(Integer limitOffset) {
        this.limitOffset = limitOffset;
        return this;
    }

    public Integer getLimitCount() {
        return limitCount;
    }

    public DaoParameter setLimitCount(Integer limitCount) {
        this.limitCount = limitCount;
        return this;
    }

}
