package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.component.ListComponent;
import com.epam.java.rt.lab.component.PageComponent;
import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * service-ms
 */
public class UserService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final UUID REMEMBER_COOKIE_NAME = UUID.randomUUID();
    private static Map<UUID, Long> rememberUserIdMap = new HashMap<>();

    public UserService() throws ConnectionException, DaoException {
    }

    public User getUser(Login login) throws DaoException {
        User user = new User();
        user.setLogin(login);
        Dao dao = daoFactory.createDao("User");
        user = dao.getFirst(user, "login", "");
        return user;
    }

    public User getUser(Long id) throws DaoException {
        User user = new User();
        user.setId(id);
        Dao dao = daoFactory.createDao("User");
        user = dao.getFirst(user, "id", "");
        return user;
    }

    public User getAnonymous() throws DaoException {
        return getUser(new Login());
    }

    public static String getRememberCookieName() {
        return UserService.REMEMBER_COOKIE_NAME.toString();
    }

    public static String setRememberUserId(Long id) {
        if (rememberUserIdMap.containsValue(id)) {
            for (Map.Entry<UUID, Long> entry : rememberUserIdMap.entrySet()) {
                if (entry.getValue().equals(id)) {
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

    public int updateUser(User user) throws DaoException {
        Dao dao = daoFactory.createDao("User");
        int updateCount = dao.update(user, "id", "firstName, middleName, lastName, avatarId");
        return updateCount;
    }

    public List<User> getUserList(PageComponent pageComponent) throws DaoException {
        Dao dao = daoFactory.createDao("User");
        List<User> userList = dao.getAll(null, null, null, "",
                (pageComponent.getCurrentPage() - 1) * pageComponent.getItemsOnPage(), pageComponent.getItemsOnPage());
        pageComponent.setCountPages((long) Math.ceil((dao.getSelectCount() * 1.0) / pageComponent.getItemsOnPage()));
        return userList;
    }

    public void putAvatar(User user, String fileName) throws DaoException {
        if (fileName != null) {
            Dao dao = daoFactory.createDao("User");
            dao.putRelEntity(user, "Avatar", fileName);
            new File(fileName).delete();
        }
    }

    public void removeAvatar(User user) throws DaoException {
        if (user.getAvatarId() != null) {
            Dao dao = daoFactory.createDao("User");
            dao.removeRelEntity(user, "Avatar");
        }
    }

    public Map<String, Object> getAvatar(User user) throws DaoException {
        Dao dao = daoFactory.createDao("User");
        Map<String, Object>  avatarMap = (Map<String, Object>) dao.getRelEntity(user, "Avatar");
        return avatarMap;
    }

    public Map<String, Object> getAvatar(Long avatarId) throws DaoException {
        User user = new User();
        user.setAvatarId(avatarId);
        return getAvatar(user);
    }

}
