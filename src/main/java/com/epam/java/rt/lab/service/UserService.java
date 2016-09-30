package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Remember;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

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

    /**
     *
     * @param user
     * @return
     * @throws ServiceException
     */
    public void addRemember(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServiceException {
        try {
            Remember remember = new Remember();
            remember.setUser(user);
            remember.setCookieName(CookieManager.getUserAgentCookieName(req));
            remember.setCookieValue(HashGenerator.hashString(UUID.randomUUID().toString()));
            remember.setValid(TimestampCompare.daysToTimestamp(
                    TimestampCompare.getCurrentTimestamp(),
                    Integer.valueOf(GlobalProperties.getProperty("remember.days.valid"))
            ));
            dao(Remember.class.getSimpleName()).create(new DaoParameter().setEntity(remember));
            CookieManager.setCookie(
                    resp,
                    remember.getCookieName(),
                    remember.getCookieValue(),
                    TimestampCompare.secondsBetweenTimestamps(
                            TimestampCompare.getCurrentTimestamp(),
                            remember.getValid()
                    )
            );
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.login.add-remember.dao", e.getCause());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.user.add-remember.hash", e.getCause());
        }
    }

    /**
     *
     * @param user
     * @return
     * @throws ServiceException
     */
    public int removeUserRemember(User user)
            throws ServiceException {
        try {
            return dao(Remember.class.getSimpleName()).delete(new DaoParameter()
                    .setWherePredicate(Where.Predicate.get(
                            Remember.Property.USER_ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            user.getId()
                    ))
            );
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.login.remove-restore-list.dao", e.getCause());
        }
    }

    /**
     *
     * @param cookieName
     * @param cookieValue
     * @return
     * @throws ServiceException
     */
    public User getUserRemember(String cookieName, String cookieValue) throws ServiceException {
        try {
            Dao dao = dao(Remember.class.getSimpleName());
            List<Remember> rememberList = dao.read(new DaoParameter()
                    .setWherePredicate(Where.Predicate.get(
                            Where.Predicate.get(
                                    Remember.Property.COOKIE_NAME,
                                    Where.Predicate.PredicateOperator.EQUAL,
                                    cookieName
                            ),
                            Where.Predicate.PredicateOperator.AND,
                            Where.Predicate.get(
                                    Remember.Property.COOKIE_VALUE,
                                    Where.Predicate.PredicateOperator.EQUAL,
                                    cookieValue
                            )
                    ))
            );
            if (rememberList == null || rememberList.size() == 0) return null;
            User user = rememberList.get(0).getUser();
            removeUserRemember(user);
            if (TimestampCompare.secondsBetweenTimestamps(
                    TimestampCompare.getCurrentTimestamp(), rememberList.get(0).getValid()) < 0)
                return null;
            return user;
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.user.get-user-remember.dao", e.getCause());
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
