package com.epam.java.rt.lab.dao;

import com.epam.java.rt.lab.entity.rbac.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * service-ms
 */
public class H2JdbcLoginDao extends H2JdbcDao implements Dao {
    private static final Logger logger = LoggerFactory.getLogger(H2JdbcLoginDao.class);

    public H2JdbcLoginDao() throws DaoException {
        H2JdbcLoginDao jdbcDao = new H2JdbcLoginDao();
        super.resetProperties();
    }

    @Override
    public Object getBy(Connection connection, String columnName, Object value) throws DaoException {
        try {
            String sql = H2JdbcDao.getDaoReadBy();
            sql = sql.replaceFirst("<?>", "*");
            sql = sql.replaceFirst("<?>", "Login");
            sql = sql.replaceFirst("<?>", columnName.concat(" = ?"));
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            Method method = H2JdbcDao.getPreparedStatementMethod(value.getClass());
            if (method == null) throw new DaoException("Prepared statement method not found");
            method.invoke(preparedStatement, 1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.first()) return null;
            Login login = new Login(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3),
                    resultSet.getInt(4), resultSet.getInt(5));
            return login;
        } catch (SQLException | InvocationTargetException | IllegalAccessException e) {
            throw new DaoException(e.getMessage());
        }
    }
}
