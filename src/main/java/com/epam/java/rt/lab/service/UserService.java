package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.OrderBy;
import com.epam.java.rt.lab.dao.sql.Update.SetValue;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.dao.sql.Where.Predicate.PredicateOperator;
import com.epam.java.rt.lab.entity.access.Avatar;
import com.epam.java.rt.lab.entity.access.Login;
import com.epam.java.rt.lab.entity.access.Remember;
import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.entity.access.User.Property;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.*;
import com.epam.java.rt.lab.util.file.UploadManager;
import com.epam.java.rt.lab.web.access.RoleFactory;
import com.epam.java.rt.lab.web.component.Page;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

import static com.epam.java.rt.lab.entity.access.Avatar.NULL_AVATAR;
import static com.epam.java.rt.lab.entity.access.Login.NULL_LOGIN;
import static com.epam.java.rt.lab.entity.access.User.NULL_USER;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.service.ServiceExceptionCode.AVATAR_FILE_ACCESS_ERROR;
import static com.epam.java.rt.lab.util.CookieManager.getCookieValue;
import static com.epam.java.rt.lab.util.CookieManager.getUserAgentCookieName;
import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.web.validator.ValidatorFactory.DIGITS;

public class UserService extends BaseService {

    public User getUser(Login login) throws AppException {
        if (login == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Where.Predicate.
                get(Property.LOGIN_ID, PredicateOperator.EQUAL,
                        login.getId()));
        List<User> userList = dao(User.class.getSimpleName()).
                read(daoParameter);
        return ((userList != null) && (userList.size() > 0))
                ? userList.get(0)
                : NULL_USER;
    }

    public int updateUser(User user) throws AppException {
        if (user == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setSetValueArray(
                new SetValue(Property.FIRST_NAME, user.getFirstName()),
                new SetValue(Property.MIDDLE_NAME, user.getMiddleName()),
                new SetValue(Property.LAST_NAME, user.getLastName()),
                new SetValue(Property.AVATAR_ID, user.getAvatarId()),
                new SetValue(Property.ROLE_NAME, user.getRole().getName()));
        daoParameter.setWherePredicate(Where.Predicate.
                get(Property.ID, PredicateOperator.EQUAL, user.getId()));
        return dao(User.class.getSimpleName()).update(daoParameter);
    }

    public int updateUser(User user, String avatarPath)
            throws AppException {
        if (user == null || avatarPath == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        int result;
        String[] pair = avatarPath.split(ESCAPED_QUESTION);
        if (pair.length == 2) pair = pair[1].split(EQUAL);
        if (ID.equals(pair[0])) {
            // avatar not changed
            result = updateUser(user);
        } else if (PATH.equals(pair[0])) {
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

    public User addUser(Login login) throws AppException {
        if (login == null || login == NULL_LOGIN) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        try (LoginService loginService = new LoginService()) {
            dao(User.class.getSimpleName()); // initiating daoFactory
            super.daoFactory.
                    beginTransaction(Connection.TRANSACTION_REPEATABLE_READ);
            User user = new User();
            login.setId(loginService.addLogin(login));
            user.setLogin(login);
            user.setRole(RoleFactory.getInstance().createAuthorized());
            DaoParameter daoParameter = new DaoParameter();
            daoParameter.setEntity(user);
            user.setId(dao(User.class.getSimpleName()).create(daoParameter));
            super.daoFactory.commitTransaction();
            return user;
        }
    }

    public User getUser(Long id) throws AppException {
        if (id == null) throw new AppException(NULL_NOT_ALLOWED);
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Where.Predicate.
                get(Property.ID, PredicateOperator.EQUAL, id));
        List<User> userList = dao(User.class.getSimpleName()).
                read(daoParameter);
        return ((userList != null) && (userList.size() > 0))
                ? userList.get(0)
                : NULL_USER;
    }

    public User getAnonymous() throws AppException {
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Where.Predicate.
                get(Login.Property.EMAIL, PredicateOperator.EQUAL, ""));
        List<User> userList = dao(User.class.getSimpleName()).
                read(daoParameter);
        return ((userList != null) && (userList.size() > 0))
                ? userList.get(0)
                : NULL_USER;
    }

    public List<User> getUserList(Page page) throws AppException {
        if (page == null) throw new AppException(NULL_NOT_ALLOWED);
        DaoParameter daoParameter = new DaoParameter();
        Long count = dao(User.class.getSimpleName()).count(daoParameter);
        page.setCountItems(count);
        daoParameter.setOrderByCriteriaArray(OrderBy.Criteria.
                asc(Property.FIRST_NAME));
        daoParameter.setLimit((page.getCurrentPage() - 1)
                * page.getItemsOnPage(), page.getItemsOnPage());
        return dao(User.class.getSimpleName()).read(daoParameter);
    }

    public User getUserRemember(HttpServletRequest req,
                                HttpServletResponse resp)
            throws AppException {
        if (req == null || resp == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        String rememberCookieName = getUserAgentCookieName(req);
        String rememberCookieValue = getCookieValue(req, rememberCookieName);
        if (rememberCookieValue == null) return NULL_USER;
        List<Remember> rememberList =
                getRememberList(rememberCookieName, rememberCookieValue);
        if (rememberList == null
                || rememberList.size() == 0) return NULL_USER;
        User user = rememberList.get(0).getUser();
        if (user == null) return NULL_USER;
        Timestamp currentTimestamp = TimestampManager.getCurrentTimestamp();
        Timestamp validTimestamp = rememberList.get(0).getValid();
        if (TimestampManager.secondsBetweenTimestamps(currentTimestamp,
                validTimestamp) < 0) return NULL_USER;
        if (user.getLogin().getAttemptLeft() == 0
                || user.getLogin().getStatus() < 0) return NULL_USER;
        removeUserRemember(user);
        CookieManager.removeCookie(resp, rememberCookieName,
                UrlManager.getUriWithContext(req, ""));
        rememberCookieValue = HashGenerator.hashString();
        Remember remember = addUserRemember(user,
                rememberCookieName, rememberCookieValue);
        currentTimestamp = TimestampManager.getCurrentTimestamp();
        int maxAge = TimestampManager.
                secondsBetweenTimestamps(currentTimestamp, remember.getValid());
        CookieManager.setCookie(resp, rememberCookieName, rememberCookieValue,
                maxAge, UrlManager.getUriWithContext(req, SLASH));
        return user;
    }

    public int removeUserRemember(User user)
            throws AppException {
        if (user == null) throw new AppException(NULL_NOT_ALLOWED);
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Where.Predicate.
                get(Remember.Property.USER_ID, PredicateOperator.EQUAL,
                        user.getId()));
        return dao(Remember.class.getSimpleName()).delete(daoParameter);
    }

    public Remember addUserRemember(User user, String rememberCookieName,
                                     String rememberCookieValue)
            throws AppException {
        String rememberValidString = PropertyManager.
                getProperty(REMEMBER_DAYS_VALID_KEY);
        if (rememberValidString == null
                || ValidatorFactory.getInstance().create(DIGITS).
                validate(rememberValidString).length > 0) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        Remember remember = new Remember();
        remember.setUser(user);
        remember.setCookieName(rememberCookieName);
        remember.setCookieValue(rememberCookieValue);
        Timestamp currentTimestamp = TimestampManager.getCurrentTimestamp();
        remember.setValid(TimestampManager.daysToTimestamp(currentTimestamp,
                Integer.valueOf(rememberValidString)));
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setEntity(remember);
        dao(Remember.class.getSimpleName()).create(daoParameter);
        return remember;
    }

    private List<Remember> getRememberList(String cookieName,
                                           String cookieValue)
            throws AppException {
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Where.Predicate.
                get(Where.Predicate.
                                get(Remember.Property.COOKIE_NAME,
                                        PredicateOperator.EQUAL, cookieName),
                        PredicateOperator.AND,
                        Where.Predicate.get(Remember.Property.COOKIE_VALUE,
                                PredicateOperator.EQUAL, cookieValue)));
        return dao(Remember.class.getSimpleName()).read(daoParameter);
    }

    public Avatar getAvatar(String id) throws AppException {
        if (id == null) throw new AppException(NULL_NOT_ALLOWED);
        if (ValidatorFactory.getInstance().create(ValidatorFactory.DIGITS).
                validate(id).length > 0) return NULL_AVATAR;
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Where.Predicate.
                get(Avatar.Property.ID, PredicateOperator.EQUAL, id));
        List<Avatar> avatarList = dao(Avatar.class.getSimpleName()).
                read(daoParameter);
        if (avatarList == null || avatarList.size() == 0) {
            return NULL_AVATAR;
        }
        return avatarList.get(0);
    }

    public void setAvatar(User user, String filePath) throws AppException {
        if (user == null || filePath == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        int backslashLastIndex = filePath.lastIndexOf(ESCAPED_BACKSLASH);
        String fileName = filePath.substring(backslashLastIndex + 1);
        String[] metaInfo = UploadManager.
                getMetaInfoFromPrefix(fileName, AVATAR_UPLOAD_TYPE);
        String avatarName = metaInfo[0];
        String contentType = metaInfo[1];
        File file = new File(filePath);
        try (InputStream inputStream = new FileInputStream(file)) {
            Avatar avatar = new Avatar();
            avatar.setId(user.getAvatarId());
            avatar.setName(avatarName);
            avatar.setType(contentType);
            avatar.setFile(inputStream);
            avatar.setModified(TimestampManager.getCurrentTimestamp());
            if (avatar.getId() == null) {
                DaoParameter daoParameter = new DaoParameter();
                daoParameter.setEntity(avatar);
                Long avatarId = dao(Avatar.class.getSimpleName()).
                        create(daoParameter);
                user.setAvatarId(avatarId);
            } else {
                DaoParameter daoParameter = new DaoParameter();
                daoParameter.setSetValueArray(
                        new SetValue(Avatar.Property.NAME, avatar.getName()),
                        new SetValue(Avatar.Property.TYPE, avatar.getType()),
                        new SetValue(Avatar.Property.FILE, avatar.getFile()),
                        new SetValue(Avatar.Property.MODIFIED,
                                avatar.getModified()));
                daoParameter.setWherePredicate(Where.Predicate.
                        get(Avatar.Property.ID, PredicateOperator.EQUAL,
                                avatar.getId()));
                dao(Avatar.class.getSimpleName()).update(daoParameter);
            }
        } catch (IOException e) {
            String[] detailArray = {filePath};
            throw new AppException(AVATAR_FILE_ACCESS_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }
    }

    public int removeAvatar(Long avatarId) throws AppException {
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Where.Predicate.
                get(Avatar.Property.ID, PredicateOperator.EQUAL, avatarId));
        return dao(Avatar.class.getSimpleName()).delete(daoParameter);
    }

}