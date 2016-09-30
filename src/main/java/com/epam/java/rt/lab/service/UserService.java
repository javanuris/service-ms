package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Remember;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * service-ms
 */
public class UserService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService()
            throws ServiceException {
    }

    public User getUser(Login login) throws ServiceException {
        try {
            List<User> userList = dao(User.class.getSimpleName()).read(new DaoParameter()
                    .setWherePredicate(Where.Predicate.get(
                            User.Property.LOGIN_ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            login.getId()
                    ))
            );
            return userList != null && userList.size() > 0 ? userList.get(0) : null;
        } catch (DaoException e) {
            throw new ServiceException("exception.service.user.get-user.dao", e.getCause());
        }
    }

    public User getUser(Long id) throws ServiceException {
        try {
            List<User> userList = dao(User.class.getSimpleName()).read(new DaoParameter()
                    .setWherePredicate(Where.Predicate.get(
                            User.Property.ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            id
                    ))
            );
            return userList != null && userList.size() > 0 ? userList.get(0) : null;
        } catch (DaoException e) {
            throw new ServiceException("exception.service.user.get-user.dao", e.getCause());
        }
    }

    public User getAnonymous() throws ServiceException {
        try {
            List<User> userList = dao(User.class.getSimpleName()).read(new DaoParameter()
                    .setWherePredicate(Where.Predicate.get(
                            Login.Property.EMAIL,
                            Where.Predicate.PredicateOperator.EQUAL,
                            ""
                    ))
            );
            return userList != null && userList.size() > 0 ? userList.get(0) : null;
        } catch (DaoException e) {
            throw new ServiceException("exception.service.user.get-user.dao", e.getCause());
        }
    }

    public Remember getRemember(String rememberCookieName, String rememberCookieValue) throws ServiceException {
//        try {
//            Dao_ dao = dao("User");
//            Remember remember = dao.getFirst(new Parameter_()
//                    .result("id", "firstName", "middleName", "lastName", "role",
//                            "remember.id", "remember.cookieName", "remember.cookieValue", "remember.valid")
//                    .filter(Parameter_.Property.set("remember.cookieName", rememberCookieName))
//            );
//            if (remember == null) return null;
//            if (remember.getCookieValue().equals(rememberCookieValue) && TimestampCompare.secondsBetweenTimestamps
//                    (TimestampCompare.getCurrentTimestamp(), remember.getValid()) > 0) {
//                remember.setCookieValue(HashGenerator.hashString(UUID.randomUUID().toString()));
//                return remember;
//            } else {
////                dao.remove(remember);
//
//                return null;
//            }
//        } catch (DaoException e) {
//            throw new ServiceException("exception.service.user-service.get-remember.dao", e.getCause());
//        } catch (NoSuchAlgorithmException e) {
//            throw new ServiceException("exception.service.user-service.get-remember.hash", e.getCause());
//        }
        return null;
    }

    public void setRemember(Remember remember) throws ServiceException {
//        try {
//            Dao_ dao = dao("User");
//            remember.setCookieValue(HashGenerator.hashString(UUID.randomUUID().toString()));
////            dao.update()
//
//        } catch (DaoException e) {
//            throw new ServiceException("exception.service.user-service.get-remember.dao", e.getCause());
//        } catch (NoSuchAlgorithmException e) {
//            throw new ServiceException("exception.service.user-service.get-remember.hash", e.getCause());
//        }
    }

//    public int addUser(User user) throws DaoException {
//        Dao_ dao = daoFactory.createDao("User");
//        return dao.create(user);
//    }
//
//    private <T> Map<String, Object> getRelEntity(T entity, String relEntityName) throws DaoException {
//        Dao_ dao = daoFactory.createDao("User");
//        Map<String, Object> relEntityMap = (Map<String, Object>) dao.getRelEntity(entity, relEntityName);
//        return relEntityMap;
//    }
//
//    private <T> void setRelEntity(T entity, String relEntityName, Object relEntity) throws DaoException {
//        if (relEntity != null) {
//            Dao_ dao = daoFactory.createDao("User");
//            dao.setRelEntity(entity, relEntityName, relEntity);
//        }
//    }
//
//    private <T> void removeRelEntity(T entity, String relEntityName) throws DaoException {
//        Dao_ dao = daoFactory.createDao("User");
//        dao.removeRelEntity(entity, relEntityName);
//    }
//
//    public void removeRemember(Long userId) throws DaoException {
//        removeRelEntity(userId, "Remember");
//    }
//
//    public int updateUser(User user) throws DaoException {
//        Dao_ dao = daoFactory.createDao("User");
//        int updateCount = dao.update(user, "id", "firstName, middleName, lastName, avatarId");
//        return updateCount;
//    }
//
//    public List<User> getUserList(PageComponent pageComponent) throws DaoException {
//        Dao_ dao = dao("User");
//        pageComponent.setCountItems(dao.count(new Parameter_()
//                .result("id", "firstName", "middleName", "lastName", "role.name", "login.email", "login.attemptLeft", "login.status"))
//        );
//        logger.debug("COUNT = {}", pageComponent.getCountItems());
//        return dao.getAll(new Parameter_()
//                .result("id", "firstName", "middleName", "lastName", "role.name", "login.email", "login.attemptLeft", "login.status")
//                .limit((pageComponent.getCurrentPage() - 1) * pageComponent.getItemsOnPage(), pageComponent.getItemsOnPage())
//                .order(QueryBuilder_.OrderType.ASC, "firstName", "middleName", "lastName")
//        );
//    }
//
//
//    public Map<String, Object> getAvatar(User user) throws DaoException {
//        return getRelEntity(user, "Avatar");
//    }
//
//    public Map<String, Object> getAvatar(Long avatarId) throws DaoException {
//        User user = new User();
//        user.setAvatarId(avatarId);
//        return getAvatar(user);
//    }
//
//    public void setAvatar(User user, String fileName) throws DaoException {
//        setRelEntity(user, "Avatar", fileName);
//    }
//
//    public void removeAvatar(User user) throws DaoException {
//        removeRelEntity(user, "Avatar");
//    }

}
