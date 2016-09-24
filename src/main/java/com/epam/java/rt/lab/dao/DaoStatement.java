package com.epam.java.rt.lab.dao;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Map;

/**
 * service-ms
 */
public class DaoStatement {

    private static class Holder { // Initialization-on-demand holder
        private static final DaoStatement INSTANCE = new DaoStatement();
    }

    private Map<Type, Method> statementMethodMap;
    private Throwable constructorThrowable;

    private DaoStatement() {
        initStatementMethodMap();
    }

    public static DaoStatement getInstance() {
        return DaoStatement.Holder.INSTANCE;
    }

    private void initStatementMethodMap() {
        try {
            this.statementMethodMap.put(int.class, PreparedStatement.class.getMethod("setInt", int.class, int.class));
            this.statementMethodMap.put(Integer.class, PreparedStatement.class.getMethod("setInt", int.class, int.class));
            this.statementMethodMap.put(boolean.class, PreparedStatement.class.getMethod("setBoolean", int.class, boolean.class));
            this.statementMethodMap.put(Boolean.class, PreparedStatement.class.getMethod("setBoolean", int.class, boolean.class));
            this.statementMethodMap.put(byte.class, PreparedStatement.class.getMethod("setByte", int.class, byte.class));
            this.statementMethodMap.put(Byte.class, PreparedStatement.class.getMethod("setByte", int.class, byte.class));
            this.statementMethodMap.put(short.class, PreparedStatement.class.getMethod("setShort", int.class, short.class));
            this.statementMethodMap.put(Short.class, PreparedStatement.class.getMethod("setShort", int.class, short.class));
            this.statementMethodMap.put(long.class, PreparedStatement.class.getMethod("setLong", int.class, long.class));
            this.statementMethodMap.put(Long.class, PreparedStatement.class.getMethod("setLong", int.class, long.class));
            this.statementMethodMap.put(BigDecimal.class, PreparedStatement.class.getMethod("setBigDecimal", int.class, BigDecimal.class));
            this.statementMethodMap.put(double.class, PreparedStatement.class.getMethod("setDouble", int.class, double.class));
            this.statementMethodMap.put(Double.class, PreparedStatement.class.getMethod("setDouble", int.class, double.class));
            this.statementMethodMap.put(float.class, PreparedStatement.class.getMethod("setFloat", int.class, float.class));
            this.statementMethodMap.put(Float.class, PreparedStatement.class.getMethod("setFloat", int.class, float.class));
//            statementMethodMap.put(Time.class, PreparedStatement.class.getMethod("setTime", int.class, Time.class));
//            statementMethodMap.put(Date.class, PreparedStatement.class.getMethod("setDate", int.class, Date.class));
            this.statementMethodMap.put(Timestamp.class, PreparedStatement.class.getMethod("setTimestamp", int.class, Timestamp.class));
            this.statementMethodMap.put(String.class, PreparedStatement.class.getMethod("setString", int.class, String.class));
            this.statementMethodMap.put(Blob.class, PreparedStatement.class.getMethod("setBlob", int.class, Blob.class));
            this.statementMethodMap.put(Clob.class, PreparedStatement.class.getMethod("setClob", int.class, Clob.class));
            this.statementMethodMap.put(FileInputStream.class, PreparedStatement.class.getMethod("setBinaryStream", int.class, InputStream.class));
        } catch (NoSuchMethodException e) {
            this.constructorThrowable = e.getCause();
        }
    }

    public <T> void setValue(PreparedStatement statement, int setIndex, T setValue)
            throws DaoException {
        try {
            if (setValue == null) {
                statement.setNull(setIndex, Types.NULL);
            } else {
                Method method = this.statementMethodMap.get(setValue.getClass());
                if (method == null)
                    throw new DaoException("exception.dao.jdbc.dao-statement.set-statement-value.get-method", this.constructorThrowable);
                method.invoke(statement, setIndex, setValue);
            }
        } catch (IllegalAccessException | SQLException | InvocationTargetException e) {
            throw new DaoException("exception.dao.jdbc.dao-statement.set-statement-value.get-invoke-method", e.getCause());
        }
    }

}
