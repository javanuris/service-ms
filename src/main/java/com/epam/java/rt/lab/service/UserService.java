package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.component.PageComponent;
import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
        user = dao.getFirst(user, "login", "first_name ASC");
        return user;
    }

    public User getUser(Long id) throws DaoException {
        User user = new User();
        user.setId(id);
        Dao dao = daoFactory.createDao("User");
        user = dao.getFirst(user, "id", "first_name ASC");
        return user;
    }

    public User getAnonymous() throws DaoException {
        return getUser(new Login());
    }

    public int addUser(User user) throws DaoException {
        Dao dao = daoFactory.createDao("User");
        return dao.create(user);
    }

    private <T> Map<String, Object> getRelEntity(T entity, String relEntityName) throws DaoException {
        Dao dao = daoFactory.createDao("User");
        Map<String, Object> relEntityMap = (Map<String, Object>) dao.getRelEntity(entity, relEntityName);
        return relEntityMap;
    }

    private <T> void setRelEntity(T entity, String relEntityName, Object relEntity) throws DaoException {
        if (relEntity != null) {
            Dao dao = daoFactory.createDao("User");
            dao.setRelEntity(entity, relEntityName, relEntity);
        }
    }

    private <T> void removeRelEntity(T entity, String relEntityName) throws DaoException {
        Dao dao = daoFactory.createDao("User");
        dao.removeRelEntity(entity, relEntityName);
    }

    public Map<String, Object> getRemember(String rememberName) throws DaoException {
        return getRelEntity(rememberName, "Remember");
    }

    public void setRemember(Map<String, Object> rememberValueMap) throws DaoException {
        setRelEntity(null, "Remember", rememberValueMap);
    }

    public void removeRemember(Long userId) throws DaoException {
        removeRelEntity(userId, "Remember");
    }

    public int updateUser(User user) throws DaoException {
        Dao dao = daoFactory.createDao("User");
        int updateCount = dao.update(user, "id", "firstName, middleName, lastName, avatarId");
        return updateCount;
    }

    public List<User> getUserList(PageComponent pageComponent) throws DaoException {
        Dao dao = daoFactory.createDao("User");
        List<User> userList = dao.getAll(null, null, null, "first_name ASC",
                (pageComponent.getCurrentPage() - 1) * pageComponent.getItemsOnPage(), pageComponent.getItemsOnPage());
        pageComponent.setCountPages((long) Math.ceil((dao.getSelectCount() * 1.0) / pageComponent.getItemsOnPage()));
        return userList;
    }

    public Map<String, Object> getAvatar(User user) throws DaoException {
        return getRelEntity(user, "Avatar");
    }

    public Map<String, Object> getAvatar(Long avatarId) throws DaoException {
        User user = new User();
        user.setAvatarId(avatarId);
        return getAvatar(user);
    }

    public void setAvatar(User user, String fileName) throws DaoException {
        setRelEntity(user, "Avatar", fileName);
    }

    public void removeAvatar(User user) throws DaoException {
        removeRelEntity(user, "Avatar");
    }

}
