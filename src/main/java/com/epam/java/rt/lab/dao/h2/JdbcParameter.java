package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.entity.EntityProperty;

import java.util.List;

/**
 * service-ms
 */
public class JdbcParameter {
    private String sqlQuery;
    private List<QueryValue> valueList;

    public JdbcParameter(String sqlQuery, List<QueryValue> valueList) {
        this.sqlQuery = sqlQuery;
        this.valueList = valueList;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public List<QueryValue> getValueList() {
        return valueList;
    }

    public static class QueryValue<T> {
        private EntityProperty entityProperty;
        private String tableName;
        private String columnName;
        private T value;

        public QueryValue(String tableName, String columnName, T value) {
            this.tableName = tableName;
            this.columnName = columnName;
            this.value = value;
        }

        public QueryValue(EntityProperty entityProperty, T value) {
            this.entityProperty = entityProperty;
            this.value = value;
        }

        public EntityProperty getEntityProperty() {
            return entityProperty;
        }

        public void setEntityProperty(EntityProperty entityProperty) {
            this.entityProperty = entityProperty;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

}
