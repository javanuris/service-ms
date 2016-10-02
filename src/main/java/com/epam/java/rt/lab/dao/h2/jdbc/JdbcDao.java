package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.DaoStatement;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.util.StringArray;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * service-ms
 */
public abstract class JdbcDao implements Dao {

    private static final String SIGN_EQUAL = "=";
    private static final String SIGN_POINT = ".";

    private static Map<String, EntitySetter> entitySetterMap = new HashMap<>();
    private static Lock mapLock = new ReentrantLock();

    private Connection connection = null;

    public JdbcDao(Connection connection) {
        this.connection = connection;
    }

    private static void init() throws DaoException {
        if (JdbcDao.mapLock.tryLock()) {
            try {
                File daoPropertyFile = new File(JdbcDao.class.getClassLoader().getResource("dao.properties").toURI());
                FileReader fileReader = new FileReader(daoPropertyFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String readLine;
                while ((readLine  = bufferedReader.readLine()) != null) {
                    String[] readLineArray = readLine.split(SIGN_EQUAL);
                    if (readLineArray.length == 2) {
                        if (!readLineArray[0].contains(SIGN_POINT)) {
                            JdbcDao.entitySetterMap.put(readLineArray[0], new EntitySetter(readLineArray[1]));
                        } else {
                            String[] nameArray = readLineArray[0].split("\\.");
                            JdbcDao.entitySetterMap.get(nameArray[0]).addMethod(nameArray[1], readLineArray[1]);
                        }
                    }
                }
            } catch (IOException e) {
                throw new DaoException("exception.dao.jdbc.dao-properties.read-file", e.getCause());
            } catch (URISyntaxException e) {
                throw new DaoException("exception.dao.jdbc.dao-properties.resource", e.getCause());
            } finally {
                JdbcDao.mapLock.unlock();
            }
        }
    }

    public void setEntityProperty(String tableName, String columnName, Object entityObject, Object value) throws DaoException {
        if (JdbcDao.entitySetterMap.size() == 0) JdbcDao.init();
        JdbcDao.entitySetterMap.get(tableName).invokeMethod(columnName, entityObject, value);
    }

    Connection getConnection() {
        return this.connection;
    }

    @Override
    public Long create(DaoParameter daoParameter) throws DaoException {
        Sql sql = getSqlCreate(daoParameter);
        try (DaoStatement statement = new DaoStatement(this.connection, sql, Statement.RETURN_GENERATED_KEYS)) {
            if (statement.executeUpdate() == 0) return null;
            ResultSet resultSet = statement.getGeneratedKeys();
            if (!resultSet.first()) return null;
            return (Long) resultSet.getObject(1);
        } catch (NoSuchMethodException e) {
            throw new DaoException("exception.dao.jdbc.create.statement-method", e.getCause());
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.create.sql", e.getCause());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.create.close", e.getCause());
        }
    }

    @Override
    public <T> List<T> read(DaoParameter daoParameter) throws DaoException {
        Sql sql = getSqlRead(daoParameter);
        try (DaoStatement statement = new DaoStatement(this.connection, sql, Statement.NO_GENERATED_KEYS)) {
            return getEntity(statement.executeQuery(), sql);
        } catch (NoSuchMethodException e) {
            throw new DaoException("exception.dao.jdbc.read.statement-method", e.getCause());
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.read.sql", e.getCause());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.read.close", e.getCause());
        }
    }

    @Override
    public int update(DaoParameter daoParameter) throws DaoException {
        Sql sql = getSqlUpdate(daoParameter);
        try (DaoStatement statement = new DaoStatement(this.connection, sql, Statement.NO_GENERATED_KEYS)) {
            return statement.executeUpdate();
        } catch (NoSuchMethodException e) {
            throw new DaoException("exception.dao.jdbc.update.statement-method", e.getCause());
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.update.sql", e.getCause());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.update.close", e.getCause());
        }
    }

    @Override
    public int delete(DaoParameter daoParameter) throws DaoException {
        Sql sql = getSqlDelete(daoParameter);
        System.out.println("\n\n\n" + sql);
        try (DaoStatement statement = new DaoStatement(this.connection, sql, Statement.NO_GENERATED_KEYS)) {
            return statement.executeUpdate();
        } catch (NoSuchMethodException e) {
            throw new DaoException("exception.dao.jdbc.delete.statement-method", e.getCause());
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.delete.sql", e.getCause());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.delete.close", e.getCause());
        }
    }

    @Override
    public Long count(DaoParameter daoParameter) throws DaoException {
        Sql sql = getSqlCount(daoParameter);
        try (DaoStatement statement = new DaoStatement(this.connection, sql, Statement.NO_GENERATED_KEYS)) {
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.first()) return null;
            return resultSet.getLong(1);
        } catch (NoSuchMethodException e) {
            throw new DaoException("exception.dao.jdbc.read.statement-method", e.getCause());
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.read.sql", e.getCause());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.read.close", e.getCause());
        }
    }

    abstract Sql getSqlCreate(DaoParameter daoParameter) throws DaoException;

    abstract Sql getSqlRead(DaoParameter daoParameter) throws DaoException;

    abstract Sql getSqlUpdate(DaoParameter daoParameter) throws DaoException;

    abstract Sql getSqlDelete(DaoParameter daoParameter) throws DaoException;

    abstract Sql getSqlCount(DaoParameter daoParameter) throws DaoException;

    abstract <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws DaoException;


//    abstract <T> List<T> getCachedEntity(DaoParameter daoParameter) throws DaoException;

    /**
     *
     */
    private static class EntitySetter {

        private Class entityClass;
        private Map<String, EntityMethod> entityMethodMap;

        EntitySetter(String entityClassName) throws DaoException {
            try {
                this.entityClass = Class.forName(entityClassName);
                this.entityMethodMap = new HashMap<>();
            } catch (ClassNotFoundException e) {
                throw new DaoException("exception.dao.jdbc.entity-setter.class-not-found", e.getCause());
            }
        }

        void addMethod(String columnName, String methodData) throws DaoException {
            this.entityMethodMap.put(columnName, new EntityMethod(this.entityClass, methodData));
        }

        public void invokeMethod(String columnName, Object entityObject, Object value) throws DaoException {
            this.entityMethodMap.get(columnName).invoke(entityObject, value);
        }

    }

    private static class EntityMethod {

        private enum Primitive {
            BOOLEAN,
            CHARACTER,
            BYTE,
            SHORT,
            INT,
            LONG,
            DOUBLE,
            FLOAT
        }

        private Primitive primitive;
        private Class type;
        private Method method;

        private EntityMethod(Class entityClass, String methodData) throws DaoException {
            try {
                String[] split = StringArray.splitSpaceLessNames(methodData, ",");
                switch (split[0]) {
                    case "boolean":
                        this.primitive = Primitive.BOOLEAN;
                        this.type = boolean.class;
                        break;
                    case "char":
                        this.primitive = Primitive.CHARACTER;
                        this.type = char.class;
                        break;
                    case "byte":
                        this.primitive = Primitive.BYTE;
                        this.type = byte.class;
                        break;
                    case "short":
                        this.primitive = Primitive.SHORT;
                        this.type = short.class;
                        break;
                    case "int":
                        this.primitive = Primitive.INT;
                        this.type = int.class;
                        break;
                    case "long":
                        this.primitive = Primitive.LONG;
                        this.type = long.class;
                        break;
                    case "double":
                        this.primitive = Primitive.DOUBLE;
                        this.type = double.class;
                        break;
                    case "float":
                        this.primitive = Primitive.FLOAT;
                        this.type = float.class;
                        break;
                    default:
                        this.type = Class.forName(split[0]);
                }
                this.method = entityClass.getMethod(split[1], this.type);
            } catch (ClassNotFoundException e) {
                throw new DaoException("exception.dao.jdbc.entity-method.class-not-found", e.getCause());
            } catch (NoSuchMethodException e) {
                throw new DaoException("exception.dao.jdbc.entity-method.method-not-found", e.getCause());
            }
        }

        private void invoke(Object entityObject, Object value) throws DaoException {
            try {
                if (this.primitive != null) {
                    switch (this.primitive) {
                        case BOOLEAN:
                            this.method.invoke(entityObject, (boolean) value);
                            break;
                        case CHARACTER:
                            this.method.invoke(entityObject, (char) value);
                            break;
                        case BYTE:
                            this.method.invoke(entityObject, (byte) value);
                            break;
                        case SHORT:
                            this.method.invoke(entityObject, (short) value);
                            break;
                        case INT:
                            this.method.invoke(entityObject, (int) value);
                            break;
                        case LONG:
                            this.method.invoke(entityObject, (long) value);
                            break;
                        case DOUBLE:
                            this.method.invoke(entityObject, (double) value);
                            break;
                        case FLOAT:
                            this.method.invoke(entityObject, (float) value);
                            break;
                    }
                } else {
                    this.method.invoke(entityObject, this.type.cast(value));
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new DaoException("exception.dao.jdbc.entity-method.invoke", e.getCause());
            }
        }

    }

}
