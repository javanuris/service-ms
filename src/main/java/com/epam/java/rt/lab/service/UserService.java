package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Permission;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * service-ms
 */
public class UserService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final UUID REMEMBER_COOKIE_NAME = UUID.randomUUID();
    private static Map<UUID, User> rememberUserMap = new HashMap<>();

    public UserService() throws DaoException {
        super();
    }

    public User getUser(Login login) throws DaoException, SQLException, ConnectionException {
        logger.debug("getUser");
        Dao jdbcDao = null;
        Connection connection = null;
        try {
            jdbcDao = super.getJdbcDao();
            logger.debug("jdbcDao = {}", jdbcDao.getClass().getSimpleName());
            connection = DaoFactory.getDaoFactory().getConnection();
            User user = jdbcDao.find(connection, "login_id", login.getId()).first();
            if (user == null) return null;
            user.setLogin(login);
            user.setRole((new RoleService()).getRole(jdbcDao.getResultSet().getLong("role_id")));
            logger.debug("user.name = {}, login = {}, role = {}",
                    user.getName(), user.getLogin().getEmail(), user.getRole().getName());
            return user;
        } finally {
            if (connection != null) DaoFactory.getDaoFactory().releaseConnection(connection);
            if (jdbcDao != null) jdbcDao.close();
        }
    }

    public static User getAnonymous() {
        return new User(2L, "anonymous", "", "", null, RoleService.getAnonymous());
    }

    public static String getRememberCookieName() {
        return UserService.REMEMBER_COOKIE_NAME.toString();
    }

    public static String setRememberUser(User user) {
        if (rememberUserMap.containsValue(user)) {
            for (Map.Entry<UUID, User> entry : rememberUserMap.entrySet()) {
                if (entry.getValue() == user) {
                    rememberUserMap.remove(entry.getKey());
                    break;
                }
            }
        }
        UUID uuid = UUID.randomUUID();
        while (rememberUserMap.containsKey(uuid)) uuid = UUID.randomUUID();
        rememberUserMap.put(uuid, user);
        return uuid.toString();
    }

    public static User getRememberUser(String rememberCookieValue) {
        return rememberUserMap.get(UUID.fromString(rememberCookieValue));
    }

}
