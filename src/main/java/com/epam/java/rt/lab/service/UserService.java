package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.OrderBy;
import com.epam.java.rt.lab.dao.sql.Update;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.entity.rbac.Avatar;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Remember;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.util.*;
import com.epam.java.rt.lab.util.validator.ValidatorException;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;
import com.epam.java.rt.lab.web.access.RoleException;
import com.epam.java.rt.lab.web.access.RoleFactory;
import com.epam.java.rt.lab.web.component.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.List;
import java.util.UUID;

/**
 * category-ms
 */
public class UserService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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

    public int updateUser(User user) throws ServiceException {
        try {
            return dao(User.class.getSimpleName()).update(new DaoParameter()
                    .setSetValueArray(
                            new Update.SetValue(User.Property.FIRST_NAME, user.getFirstName()),
                            new Update.SetValue(User.Property.MIDDLE_NAME, user.getMiddleName()),
                            new Update.SetValue(User.Property.LAST_NAME, user.getLastName()),
                            new Update.SetValue(User.Property.AVATAR_ID, user.getAvatarId()),
                            new Update.SetValue(User.Property.ROLE_NAME, user.getRole().getName())
                    )
                    .setWherePredicate(Where.Predicate.get(
                            User.Property.ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            user.getId()
                    ))
            );
        } catch (DaoException e) {
            throw new ServiceException("exception.service.user.update-user.dao", e.getCause());
        }
    }

    public int updateUser(User user, String avatarPath) throws ServiceException {
        int result = 0;
        String[] pair = avatarPath.split("\\?");
        if (pair.length == 2) pair = pair[1].split("=");
        if ("id".equals(pair[0])) {
            // avatar not changed
            result = updateUser(user);
        } else if ("path".equals(pair[0])) {
            // avatar recently uploaded
            setAvatar(user, pair[1]);
            result = updateUser(user);
        } else {
            if (user.getAvatarId() != null) {
                // avatar removed
                Long avatarId = user.getAvatarId();
                user.setAvatarId(null);
                result = updateUser(user);
                removeAvatar(avatarId);
            } else {
                // avatar not changed
                result = updateUser(user);
            }
        }
        return result;
    }

    public Long addUser(Login login) throws ServiceException {
        try (LoginService loginService = new LoginService()) {
            super.daoFactory.beginTransaction(Connection.TRANSACTION_REPEATABLE_READ);
            User user = new User();
            login.setId(loginService.addLogin(login));
            user.setLogin(login);
            user.setRole(RoleFactory.getInstance().createAuthorized());
            Long userId = dao(User.class.getSimpleName()).create(new DaoParameter().setEntity(user));
            super.daoFactory.commitTransaction();
            return userId;
        } catch (DaoException e) {
            throw new ServiceException("exception.service.user.add-user.dao", e.getCause());
        } catch (RoleException e) {
            throw new ServiceException("exception.service.user.add-user.role-factory", e.getCause());
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

    public List<User> getUserList(Page page) throws ServiceException {
        try {
            DaoParameter daoParameter = new DaoParameter();
            page.setCountItems(dao(User.class.getSimpleName()).count(daoParameter));
            return dao(User.class.getSimpleName()).read(new DaoParameter()
                    .setOrderByCriteriaArray(OrderBy.Criteria.asc(
                            User.Property.FIRST_NAME
                    ))
                    .setLimit(
                            (page.getCurrentPage() - 1) * page.getItemsOnPage(),
                            page.getItemsOnPage()
                    )
            );
        } catch (DaoException e) {
            throw new ServiceException("exception.service.user.get-user-list.dao", e.getCause());
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
            remember.setValid(TimestampManager.daysToTimestamp(
                    TimestampManager.getCurrentTimestamp(),
                    Integer.valueOf(PropertyManager.getProperty("remember.days.valid"))
            ));
            dao(Remember.class.getSimpleName()).create(new DaoParameter().setEntity(remember));
            CookieManager.setCookie(
                    resp,
                    remember.getCookieName(),
                    remember.getCookieValue(),
                    TimestampManager.secondsBetweenTimestamps(
                            TimestampManager.getCurrentTimestamp(),
                            remember.getValid()
                    ),
                    UrlManager.getContextUri(req, "/")
            );
        } catch (DaoException e) {
            throw new ServiceException("exception.service.login.add-remember.dao", e.getCause());
        } catch (NoSuchAlgorithmException e) {
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
            throw new ServiceException("exception.service.user.remove-user-remember.dao", e.getCause());
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
            if (TimestampManager.secondsBetweenTimestamps(
                    TimestampManager.getCurrentTimestamp(), rememberList.get(0).getValid()) < 0)
                return null;
            return user;
        } catch (DaoException e) {
            throw new ServiceException("exception.service.user.get-user-remember.dao", e.getCause());
        }
    }

    public Avatar getAvatar(String id) throws ServiceException {
        try {
            if (ValidatorFactory.create("digits").validate(id) != null) return null;
            List<Avatar> avatarList = dao(Avatar.class.getSimpleName()).read(new DaoParameter()
                    .setWherePredicate(Where.Predicate.get(
                            Avatar.Property.ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            id
                    ))
            );
            if (avatarList == null || avatarList.size() == 0) return null;
            return avatarList.get(0);
        } catch (DaoException | ValidatorException e) {
            throw new ServiceException("exception.service.user.get-avatar.dao", e.getCause());
        }
    }

    public void setAvatar(User user, String filePath) throws ServiceException {
        String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
        int avatarInfoIndex = fileName.lastIndexOf(".avatar.");
        String avatarName = fileName.substring(0, avatarInfoIndex);
        avatarInfoIndex = avatarInfoIndex + 8;
        String contentType = fileName.substring(avatarInfoIndex,
                fileName.indexOf(".", avatarInfoIndex)).replaceAll("_", "/");
        try (InputStream inputStream = new FileInputStream(new File(filePath))) {
            Avatar avatar = new Avatar();
            avatar.setId(user.getAvatarId());
            avatar.setName(avatarName);
            avatar.setType(contentType);
            avatar.setFile((FileInputStream) inputStream);
            avatar.setModified(TimestampManager.getCurrentTimestamp());
            if (avatar.getId() == null) {
                Long avatarId = dao(Avatar.class.getSimpleName()).create(new DaoParameter()
                        .setEntity(avatar)
                );
                user.setAvatarId(avatarId);
            } else {
                dao(Avatar.class.getSimpleName()).update(new DaoParameter()
                        .setSetValueArray(
                                new Update.SetValue(Avatar.Property.NAME, avatar.getName()),
                                new Update.SetValue(Avatar.Property.TYPE, avatar.getType()),
                                new Update.SetValue(Avatar.Property.FILE, avatar.getFile()),
                                new Update.SetValue(Avatar.Property.MODIFIED, avatar.getModified())
                        )
                        .setWherePredicate(Where.Predicate.get(
                                Avatar.Property.ID,
                                Where.Predicate.PredicateOperator.EQUAL,
                                avatar.getId()
                        ))
                );
            }
        } catch (IOException e) {
            throw new ServiceException("exception.service.user.set-avatar.file", e.getCause());
        } catch (DaoException e) {
            throw new ServiceException("exception.service.user.set-avatar.dao", e.getCause());
        }
    }

    public int removeAvatar(Long avatarId) throws ServiceException {
        try {
            return dao(Avatar.class.getSimpleName()).delete(new DaoParameter()
                    .setWherePredicate(Where.Predicate.get(
                            Avatar.Property.ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            avatarId
                    ))
            );
        } catch (DaoException e) {
            throw new ServiceException("exception.service.user.remove-avatar.dao", e.getCause());
        }
    }
}
