package com.epam.java.rt.lab.dao.h2.sql;

import com.epam.java.rt.lab.dao.DaoException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * service-ms
 */
public class Query {
    private static String daoSelect;
    private static String daoFrom;
    private static String daoJoin;
    private static String daoWhere;
    private static String daoUpdate;
    private static String daoSet;
    private List<String> columnList = new ArrayList<>();
    private String tableName;
    private List<String> joinList = new ArrayList<>();
    private List<String> onList = new ArrayList<>();
    private List<String> whereList = new ArrayList<>();
    private List<Value> valueList = new ArrayList<>();
    private List<String> setList = new ArrayList<>();

    public Query() {
    }

    public static void resetProperties(Properties properties) {
        daoSelect = properties.getProperty("dao.select");
        daoFrom = properties.getProperty("dao.from");
        daoJoin = properties.getProperty("dao.join");
        daoWhere = properties.getProperty("dao.where");
        daoUpdate = properties.getProperty("dao.update");
        daoSet = properties.getProperty("dao.set");
    }

    public void setTableName(String tableName) throws DaoException {
        this.tableName = Validator.toSqlExpression(tableName);
    }

    public void addColumn(String columnName) throws DaoException {
        columnList.add(Validator.toSqlExpression(columnName));
    }

    public void addWhere(Option option) throws DaoException {
        if (tableName == null || columnList.size() == 0)
            throw new DaoException("Query not initialized");
        whereList.add(option.toString());
    }

    public void addWhere(Option option, Value value) throws DaoException {
        if (tableName == null || columnList.size() == 0)
            throw new DaoException("Query not initialized");
        whereList.add(option.toString());
        valueList.add(value);
    }

    public void addJoin(String tableName) throws DaoException {
        if (this.tableName == null || columnList.size() == 0)
            throw new DaoException("Query not initialized");
        joinList.add("\"".concat(Validator.toSqlExpression(tableName)).concat("\""));
    }

    public void addJoin(String tableName, String alias) throws DaoException {
        if (this.tableName == null || columnList.size() == 0)
            throw new DaoException("Query not initialized");
        joinList.add("\"".concat(Validator.toSqlExpression(tableName)).concat("\"").concat(" as ").concat(alias));
    }

    public void addOn(Option option) throws DaoException {
        if (tableName == null || columnList.size() == 0)
            throw new DaoException("Query not initialized");
        if (!option.asOn()) throw new DaoException("Option is not 'ON' section");
        onList.add(option.toString());
    }

    private StringBuilder listToString(List<String> stringList, String delimiter) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (String string : stringList) {
            if (first) {
                first = false;
            } else {
                result.append(delimiter);
            }
            result.append(string);
        }
        return result;
    }

    public void addSet(Set set) throws DaoException {
        if (tableName == null || columnList.size() == 0)
            throw new DaoException("Query not initialized");
        setList.add(set.toString());
    }

    public String select() throws DaoException {
        if (tableName == null || columnList.size() == 0)
            throw new DaoException("Query not initialized");
        StringBuilder sql = new StringBuilder();
        sql.append(daoSelect).append(" ").append(listToString(columnList, ", ")).append(" ");
        sql.append(daoFrom).append(" \"").append(tableName).append("\"");
        if (joinList.size() > 0)
            sql.append(" ").append(daoJoin).append(" ").append(listToString(joinList, ", "));
        if (onList.size() >0 || whereList.size() > 0) {
            sql.append(" ").append(daoWhere);
            if (onList.size() > 0) sql.append(" ").append(listToString(onList, " and "));
            if (whereList.size() > 0) {
                if (onList.size() > 0) sql.append(" and");
                sql.append(" ").append(listToString(whereList, " and "));
            }
        }
        return sql.toString();
    }

    public String update() throws DaoException {
        if (tableName == null || columnList.size() == 0)
            throw new DaoException("Query not initialized");
        StringBuilder sql = new StringBuilder();
        sql.append(daoUpdate).append(" \"").append(tableName).append("\"");
        if (setList.size() > 0)
            sql.append(" ").append(daoSet).append(" ").append(listToString(setList, ", "));
        System.out.println(sql.toString());
        return sql.toString();
    }

    public void addValue(Value value) {
        valueList.add(value);
    }

    public List<Value> getValueList() {
        return valueList;
    }

    public List<String> getColumnList() { return columnList; }
}
