package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * service-ms
 */
public class UserService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final UUID REMEMBER_COOKIE_NAME = UUID.randomUUID();
    private static Map<UUID, Long> rememberUserIdMap = new HashMap<>();

    public UserService() throws DaoException {
        super();
    }

    private Long loginId(Login login) {
        if (login == null) return null;
        return login.getId();
    }

    User getUser(Login login, Connection connection) throws DaoException, SQLException, ConnectionException {
        logger.debug("getUser");
        Dao userDao = null;
        try {
            userDao = super.getJdbcDao(connection);
            logger.debug("userDao = {}", userDao.getClass().getSimpleName());
            User user = userDao.query("*").filter("login_id", loginId(login)).first();
            if (user == null) return null;
            user.setLogin(login);
            user.setRole((new RoleService())
                    .getRole(userDao.getResultSet().getLong("role_id"), connection));
            logger.debug("user.name = {}, login = {}, role = {}",
                    user.getName(), user.getLogin(), user.getRole().getName());
            return user;
        } finally {
            if (userDao != null) userDao.close();
        }
    }

    public User getUser(Login login) throws DaoException, SQLException, ConnectionException {
        logger.debug("getUser");
        Connection connection = null;
        try {
            connection = DaoFactory.getDaoFactory().getConnection();
            return getUser(login, connection);
        } finally {
            if (connection != null) DaoFactory.getDaoFactory().releaseConnection(connection);
        }
    }

    public static User getAnonymous() throws DaoException, SQLException, ConnectionException {
        logger.debug("getAnonymous");
        Connection connection = null;
        try {
            connection = DaoFactory.getDaoFactory().getConnection();
            return (new UserService()).getUser((Login) null, connection);
        } finally {
            if (connection != null) DaoFactory.getDaoFactory().releaseConnection(connection);
        }
    }

    User getUser(Long id, Connection connection) throws DaoException, SQLException, ConnectionException {
        logger.debug("getUser");
        Dao userDao = null;
        try {
            userDao = super.getJdbcDao(connection);
            logger.debug("userDao = {}", userDao.getClass().getSimpleName());
            User user = userDao.query("*").filter("id", id).first();
            if (user == null) return null;
            user.setLogin((new LoginService()
                    .getLogin(userDao.getResultSet().getLong("login_id"), connection)));
            user.setRole((new RoleService())
                    .getRole(userDao.getResultSet().getLong("role_id"), connection));
            logger.debug("user.name = {}, login = {}, role = {}",
                    user.getName(), user.getLogin(), user.getRole().getName());
            return user;
        } finally {
            if (userDao != null) userDao.close();
        }
    }

    public User getUser(Long id) throws DaoException, SQLException, ConnectionException {
        logger.debug("getUser");
        Connection connection = null;
        try {
            connection = DaoFactory.getDaoFactory().getConnection();
            return getUser(id, connection);
        } finally {
            if (connection != null) DaoFactory.getDaoFactory().releaseConnection(connection);
        }
    }

    public static String getRememberCookieName() {
        return UserService.REMEMBER_COOKIE_NAME.toString();
    }

    public static String setRememberUserId(Long id) {
        if (rememberUserIdMap.containsValue(id)) {
            for (Map.Entry<UUID, Long> entry : rememberUserIdMap.entrySet()) {
                if (entry.getValue() == id) {
                    rememberUserIdMap.remove(entry.getKey());
                    break;
                }
            }
        }
        UUID uuid = UUID.randomUUID();
        while (rememberUserIdMap.containsKey(uuid)) uuid = UUID.randomUUID();
        rememberUserIdMap.put(uuid, id);
        return uuid.toString();
    }

    public static Long getRememberUserId(String rememberCookieValue) {
        return rememberUserIdMap.get(UUID.fromString(rememberCookieValue));
    }

    public int updateName(User user) throws DaoException, SQLException, ConnectionException {
        logger.debug("updateName");
        Connection connection = null;
        Dao jdbcDao = null;
        try {
            connection = DaoFactory.getDaoFactory().getConnection();
            jdbcDao = super.getJdbcDao(connection);
            logger.debug("jdbcDao = {}", jdbcDao.getClass().getSimpleName());
            return jdbcDao.update("first_name", "middle_name", "last_name").set(user).execute().getLastUpdateCount();
        } finally {
            if (jdbcDao != null) jdbcDao.close();
            if (connection != null) DaoFactory.getDaoFactory().releaseConnection(connection);
        }
    }

}
