package com.epam.java.rt.lab.dao;

/**
 * service-ms
 */
public class H2JdbcPermissionDao extends H2JdbcDao implements Dao {

    public H2JdbcPermissionDao() throws DaoException {
        H2JdbcPermissionDao jdbcDao = new H2JdbcPermissionDao();
        super.resetProperties();
    }

}
