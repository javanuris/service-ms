package com.epam.java.rt.lab.dao;

import com.epam.java.rt.lab.dao.h2.JdbcParameter;
import com.epam.java.rt.lab.entity.EntityProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class DaoParameter {

    private Value value;
    private Where where;
    private Limit limit;
    private Order order;
    private boolean completeEntity = true;

    public DaoParameter() {
    }

    public Value getValue() {
        return value;
    }

    public DaoParameter setValue(Value value) {
        this.value = value;
        return this;
    }

    public Where getWhere() {
        return where;
    }

    public DaoParameter setWhere(Where where) {
        this.where = where;
        return this;
    }

    public Limit getLimit() {
        return limit;
    }

    public DaoParameter setLimit(Limit limit) {
        this.limit = limit;
        return this;
    }

    public Order getOrder() {
        return order;
    }

    public DaoParameter setOrder(Order order) {
        this.order = order;
        return this;
    }

    public boolean isCompleteEntity() {
        return completeEntity;
    }

    public void setCompleteEntity(boolean completeEntity) {
        this.completeEntity = completeEntity;
    }

    /**
     *
     */
    public static class Value implements Iterable<JdbcParameter.QueryValue> {

        private enum ResultType {
            INSERT,
            UPDATE
        }

        private static final String INSERT_VALUES_PART_A = "(";
        private static final String INSERT_VALUES_PART_B = ") VALUES (";
        private static final String INSERT_VALUES_PART_C = "?";
        private static final String INSERT_VALUES_PART_D = ")";
        private static final String UPDATE_VALUES_PART_A = "SET ";
        private static final String UPDATE_VALUES_PART_B = " = ?";
        private static final String DELIMITER = ", ";
        private static final String TABLE_COLUMN_DELIMITER = ".";

        private List<JdbcParameter.QueryValue> queryValueList;
        private ResultType resultType;

        public Value() {
            this.queryValueList = new ArrayList<>();
        }

        public Value setInsert() {
            this.resultType = ResultType.INSERT;
            return this;
        }

        public Value setUpdate() {
            this.resultType = ResultType.UPDATE;
            return this;
        }

        public <T> Value set(EntityProperty entityProperty, T value) {
            this.queryValueList.add(new JdbcParameter.QueryValue(entityProperty, value));
            return this;
        }

        @Override
        public Iterator<JdbcParameter.QueryValue> iterator() {
            return new Iterator<JdbcParameter.QueryValue>() {
                private int index = 0;

                public boolean hasNext(){
                    return index < queryValueList.size();
                }

                public JdbcParameter.QueryValue next(){
                    return queryValueList.get(index);
                }

                public void remove(){
                    throw new UnsupportedOperationException();
                }
            };
        }

        private String combineInsert() {
            boolean first = true;
            StringBuilder result = new StringBuilder();
            for (JdbcParameter.QueryValue queryValue : this.queryValueList) {
                if (first) {
                    result.append(INSERT_VALUES_PART_A);
                    first = false;
                } else {
                    result.append(DELIMITER);
                }
                result.append(queryValue.getTableName())
                        .append(TABLE_COLUMN_DELIMITER)
                        .append(queryValue.getColumnName());
            }
            result.append(INSERT_VALUES_PART_B);
            first = true;
            for (int i = 0; i < this.queryValueList.size(); i++) {
                if (first) {
                    first = false;
                } else {
                    result.append(DELIMITER);
                }
                result.append(INSERT_VALUES_PART_C);
            }
            result.append(INSERT_VALUES_PART_D);
            return result.toString();
        }

        private String combineUpdate() {
            boolean first = true;
            StringBuilder result = new StringBuilder();
            for (JdbcParameter.QueryValue queryValue : this.queryValueList) {
                if (first) {
                    result.append(UPDATE_VALUES_PART_A);
                    first = false;
                } else {
                    result.append(DELIMITER);
                }
                result.append(queryValue.getTableName())
                        .append(TABLE_COLUMN_DELIMITER)
                        .append(queryValue.getColumnName())
                        .append(UPDATE_VALUES_PART_B);
            }
            return result.toString();
        }

        @Override
        public String toString() {
            return this.resultType == null || this.resultType == ResultType.INSERT ?
                    combineInsert() : combineUpdate();
        }

    }

    /**
     *
     */
    public static class Where implements Iterable<JdbcParameter.QueryValue> {

        private static final String WHERE = "WHERE ";
        private static final String AND = "AND ";
        private static final String OR = "OR ";
        private static final String EQUAL = " = ";
        private static final String NULL = " IS NULL";
        private static final String DELIMITER = " ";
        private static final String TABLE_COLUMN_DELIMITER = ".";

        private List<JdbcParameter.QueryValue> queryValueList;
        private List<String> prefixList;
        private List<String> optionList;

        public Where() {
            this.queryValueList = new ArrayList<>();
            this.prefixList = new ArrayList<>();
            this.optionList = new ArrayList<>();
        }

        public <T> Where andEqual(EntityProperty entityProperty, T value) {
            this.queryValueList.add(new JdbcParameter.QueryValue(entityProperty, value));
            this.prefixList.add(AND);
            this.optionList.add(EQUAL);
            return this;
        }

        public <T> Where orEqual(EntityProperty entityProperty, T value) {
            this.queryValueList.add(new JdbcParameter.QueryValue(entityProperty, value));
            this.prefixList.add(OR);
            this.optionList.add(EQUAL);
            return this;
        }

        @Override
        public Iterator<JdbcParameter.QueryValue> iterator() {
            return new Iterator<JdbcParameter.QueryValue>() {
                private int index = 0;

                public boolean hasNext(){
                    return index < queryValueList.size();
                }

                public JdbcParameter.QueryValue next(){
                    return queryValueList.get(index);
                }

                public void remove(){
                    throw new UnsupportedOperationException();
                }
            };
        }

        private String combineValue(int i) {
            if (EQUAL.equals(optionList.get(i))) {
                String part = this.prefixList.get(i)
                        .concat(this.queryValueList.get(i).getTableName())
                        .concat(TABLE_COLUMN_DELIMITER)
                        .concat(this.queryValueList.get(i).getColumnName());
                if (queryValueList.get(i).getValue() == null)
                    return part.concat(NULL);
                return part.concat(EQUAL).concat(String.valueOf(this.queryValueList.get(i).getValue()));
            }
            return "";
        }

        @Override
        public String toString() {
            boolean first = true;
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < this.queryValueList.size(); i++) {
                if (first) {
                    result.append(WHERE);
                    first = false;
                } else {
                    result.append(DELIMITER);
                }
                result.append(combineValue(i));
            }
            return result.toString();
        }

    }

    /**
     *
     */
    public static class Limit {

        private static final String LIMIT = "LIMIT ";
        private static final String DELIMITER = ", ";

        private Long offset;
        private Long count;

        public Limit(Long offset, Long count) {
            this.offset = offset;
            this.count = count;
        }

        public Long getOffset() {
            return offset;
        }

        public void setOffset(Long offset) {
            this.offset = offset;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return LIMIT.concat(String.valueOf(this.offset))
                    .concat(DELIMITER).concat(String.valueOf(this.count));
        }

    }

    /**
     *
     */
    public static class Order implements Iterable<EntityProperty> {

        private static final String ORDER_BY = "ORDER BY ";
        private static final String ASC = " ASC";
        private static final String DESC = " DESC";
        private static final String DELIMITER = ", ";

        private List<EntityProperty> entityPropertyList;
        private List<String> orderTypeList;
        private List<String> columnList;

        public Order() {
            this.entityPropertyList = new ArrayList<>();
            this.orderTypeList = new ArrayList<>();
            this.columnList = new ArrayList<>();
        }

        public Order asc(EntityProperty entityProperty) {
            this.entityPropertyList.add(entityProperty);
            this.orderTypeList.add(ASC);
            return this;
        }

        public Order desc(EntityProperty entityProperty) {
            this.entityPropertyList.add(entityProperty);
            this.orderTypeList.add(DESC);
            return this;
        }

        public void clearColumnList() {
            this.columnList = new ArrayList<>();
        }

        public void addColumn(String columnFullName) {
            this.columnList.add(columnFullName);
        }

        @Override
        public Iterator<EntityProperty> iterator() {
            return new Iterator<EntityProperty>() {
                private int index = 0;

                public boolean hasNext(){
                    return index < entityPropertyList.size();
                }

                public EntityProperty next(){
                    return entityPropertyList.get(index);
                }

                public void remove(){
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public String toString() {
            boolean first = true;
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < this.columnList.size(); i++) {
                if (first) {
                    result.append(ORDER_BY);
                    first = false;
                } else {
                    result.append(DELIMITER);
                }
                result.append(this.columnList.get(i)).append(this.orderTypeList.get(i));
            }
            return result.toString();
        }

    }

}
