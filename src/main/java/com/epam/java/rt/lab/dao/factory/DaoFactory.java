package com.epam.java.rt.lab.dao.factory;

import com.epam.java.rt.lab.connection.ConnectionPool;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * service-ms
 */
public abstract class DaoFactory {
    private static final Logger logger = LoggerFactory.getLogger(DaoFactory.class);
    private static String dbmsName = null;
    private static Class<DaoFactory> factoryClass = null;

    public static DaoFactory createDaoFactory() throws DaoException {
        try {
            if (factoryClass == null) DaoFactory.resetProperties("database.properties");
            return factoryClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public static void resetProperties(String fileName) throws DaoException {
        try {
            InputStream inputStream = ConnectionPool.class.getClassLoader().getResourceAsStream(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String readLine;
            String[] partArray;
            String factoryClassName = null;
            while ((readLine = bufferedReader.readLine()) != null) {
                partArray = readLine.split("=");
                if (partArray[0].equals("database.management-system")) {
                    factoryClassName = DaoFactory.class
                            .getPackage().getName().concat(".").concat(partArray[1]).concat("DaoFactory");
                    break;
                }
            }
            logger.debug("factoryClassName = {}", factoryClassName);
            if (factoryClassName == null) throw new DaoException("Database properties error");
            DaoFactory.factoryClass = (Class<DaoFactory>) Class.forName(factoryClassName);
        } catch (IOException | ClassNotFoundException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public static String getDbmsName() {
        return dbmsName;
    }

    public abstract Dao getJdbcDao(String entityName) throws DaoException;

}
