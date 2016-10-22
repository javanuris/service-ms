package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.DaoStatement;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.StringCombiner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.epam.java.rt.lab.dao.DaoExceptionCode.*;
import static com.epam.java.rt.lab.exception.AppExceptionCode.*;
import static com.epam.java.rt.lab.util.PropertyManager.*;
import static java.sql.Statement.NO_GENERATED_KEYS;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public abstract class JdbcDao implements Dao {

    private static Map<String, EntitySetter> entitySetterMap = new HashMap<>();
    private static Lock mapLock = new ReentrantLock();

    private static final String DAO_PROPERTY_FILE = "dao.properties";

    private Connection connection = null;

    public JdbcDao(Connection connection) throws AppException {
        if (connection == null) throw new AppException(NULL_NOT_ALLOWED);
        this.connection = connection;
    }

    public static void initDaoProperties() throws AppException {
        if (JdbcDao.mapLock.tryLock()) {
            try {
                ClassLoader classLoader = JdbcDao.class.getClassLoader();
                URL url = classLoader.getResource(DAO_PROPERTY_FILE);
                if (url == null) {
                    String[] detailArray = {DAO_PROPERTY_FILE};
                    throw new AppException(PROPERTY_READ_ERROR, detailArray);
                }
                File daoPropertyFile = new File(url.toURI());
                FileReader fileReader = new FileReader(daoPropertyFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String readLine;
                while ((readLine  = bufferedReader.readLine()) != null) {
                    String[] readLineArray = readLine.split(EQUAL);
                    if (readLineArray.length == 2) {
                        if (!readLineArray[0].contains(POINT)) {
                            JdbcDao.entitySetterMap.put(readLineArray[0],
                                    new EntitySetter(readLineArray[1]));
                        } else {
                            String[] nameArray = readLineArray[0].
                                    split(ESCAPED_POINT);
                            JdbcDao.entitySetterMap.get(nameArray[0]).
                                    addMethod(nameArray[1], readLineArray[1]);
                        }
                    }
                }
            } catch (IOException e) {
                String[] detailArray = {DAO_PROPERTY_FILE};
                throw new AppException(PROPERTY_EMPTY_OR_CONTENT_ERROR,
                        e.getMessage(), e.getCause(), detailArray);
            } catch (URISyntaxException e) {
                String[] detailArray = {DAO_PROPERTY_FILE};
                throw new AppException(DAO_PROPERTY_FILE_URI_ERROR,
                        e.getMessage(), e.getCause(), detailArray);
            } finally {
                JdbcDao.mapLock.unlock();
            }
        }
    }

    public void setEntityProperty(String tableName, String columnName,
                                  Object entityObject, Object value)
            throws AppException {
        if (tableName == null || columnName == null
                || entityObject == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        JdbcDao.entitySetterMap.get(tableName).
                invokeMethod(columnName, entityObject, value);
    }

    Connection getConnection() {
        return this.connection;
    }

    @Override
    public Long create(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        Sql sql = getSqlCreate(daoParameter);
        try (DaoStatement statement = DaoStatement.
                create(this.connection, sql, RETURN_GENERATED_KEYS)) {
            if (statement.executeUpdate() == 0) {
                throw new AppException(UPDATE_ERROR);
            }
            ResultSet resultSet = statement.getGeneratedKeys();
            if (!resultSet.first()) {
                throw new AppException(UPDATE_ERROR);
            }
            return (Long) resultSet.getObject(1);
        } catch (SQLException e) {
            throw new AppException(SQL_OPERATION_ERROR,
                    e.getMessage(), e.getCause());
        } catch (Exception e) {
            throw new AppException(AUTO_CLOSABLE_OBJECT_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

    @Override
    public <T> List<T> read(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        Sql sql = getSqlRead(daoParameter);
        try (DaoStatement statement = DaoStatement.
                create(this.connection, sql, NO_GENERATED_KEYS)) {
            return getEntity(statement.executeQuery(), sql);
        } catch (SQLException e) {
            throw new AppException(SQL_OPERATION_ERROR,
                    e.getMessage(), e.getCause());
        } catch (Exception e) {
            throw new AppException(AUTO_CLOSABLE_OBJECT_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

    @Override
    public int update(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        Sql sql = getSqlUpdate(daoParameter);
        try (DaoStatement statement = DaoStatement.
                create(this.connection, sql, NO_GENERATED_KEYS)) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new AppException(SQL_OPERATION_ERROR,
                    e.getMessage(), e.getCause());
        } catch (Exception e) {
            throw new AppException(AUTO_CLOSABLE_OBJECT_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

    @Override
    public int delete(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        Sql sql = getSqlDelete(daoParameter);
        try (DaoStatement statement = DaoStatement.
                create(this.connection, sql, NO_GENERATED_KEYS)) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new AppException(SQL_OPERATION_ERROR,
                    e.getMessage(), e.getCause());
        } catch (Exception e) {
            throw new AppException(AUTO_CLOSABLE_OBJECT_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

    @Override
    public Long count(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        Sql sql = getSqlCount(daoParameter);
        try (DaoStatement statement = DaoStatement.
                create(this.connection, sql, NO_GENERATED_KEYS)) {
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.first()) return null;
            return resultSet.getLong(1);
        } catch (SQLException e) {
            throw new AppException(SQL_OPERATION_ERROR,
                    e.getMessage(), e.getCause());
        } catch (Exception e) {
            throw new AppException(AUTO_CLOSABLE_OBJECT_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

    abstract Sql getSqlCreate(DaoParameter daoParameter) throws AppException;

    abstract Sql getSqlRead(DaoParameter daoParameter) throws AppException;

    abstract Sql getSqlUpdate(DaoParameter daoParameter) throws AppException;

    abstract Sql getSqlDelete(DaoParameter daoParameter) throws AppException;

    abstract Sql getSqlCount(DaoParameter daoParameter) throws AppException;

    abstract <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws AppException;

    /**
     *
     */
    private static class EntitySetter {

        private Class entityClass;
        private Map<String, EntityMethod> entityMethodMap;

        EntitySetter(String entityClassName) throws AppException {
            if (entityClassName == null) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            try {
                this.entityClass = Class.forName(entityClassName);
                this.entityMethodMap = new HashMap<>();
            } catch (ClassNotFoundException e) {
                String[] detailArray = {entityClassName};
                throw new AppException(ENTITY_CLASS_NOT_FOUND,
                        e.getMessage(), e.getCause(), detailArray);
            }
        }

        void addMethod(String columnName, String methodData)
                throws AppException {
            if (columnName == null || methodData == null) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            this.entityMethodMap.put(columnName,
                    new EntityMethod(this.entityClass, methodData));
        }

        public void invokeMethod(String columnName, Object entityObject,
                                 Object value) throws AppException {
            this.entityMethodMap.get(columnName).invoke(entityObject, value);
        }

    }

    /**
     *
     */
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

        private EntityMethod(Class entityClass, String methodData)
                throws AppException {
            if (entityClass == null || methodData == null) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            try {
                String[] split = StringCombiner.
                        splitSpaceLessNames(methodData, COMMA);
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
                throw new AppException(VALUE_TYPE_NOT_FOUND,
                        e.getMessage(), e.getCause());
            } catch (NoSuchMethodException e) {
                throw new AppException(ENTITY_SETTER_METHOD_NOT_FOUND,
                        e.getMessage(), e.getCause());
            }
        }

        private void invoke(Object entityObject, Object value)
                throws AppException {
            if (entityObject == null) throw new AppException(NULL_NOT_ALLOWED);
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
                throw new AppException(ENTITY_SETTER_METHOD_INVOKE_ERROR,
                        e.getMessage(), e.getCause());
            }

        }

    }

}
