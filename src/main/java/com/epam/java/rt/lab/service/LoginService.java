package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Update;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.dao.sql.Where.Predicate.PredicateOperator;
import com.epam.java.rt.lab.entity.access.*;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.*;
import com.epam.java.rt.lab.web.component.FormControlValue;
import com.epam.java.rt.lab.web.validator.Validator;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

import static com.epam.java.rt.lab.entity.access.Login.NULL_LOGIN;
import static com.epam.java.rt.lab.entity.access.Login.Property;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.service.ServiceExceptionCode.CREATE_DAO_FACTORY_OR_DAO_ERROR;
import static com.epam.java.rt.lab.service.ServiceExceptionCode.NO_ASSIGNED_USER_TO_LOGIN;
import static com.epam.java.rt.lab.util.CookieManager.getUserAgentCookieName;
import static com.epam.java.rt.lab.util.HashGenerator.hashPassword;
import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.web.validator.ValidatorFactory.*;

public class LoginService extends BaseService {

    /**
     * @param req
     * @param resp
     * @param emailValue
     * @param passwordValue
     * @param rememberMeValue
     * @return
     * @throws AppException
     */
    public boolean login(HttpServletRequest req, HttpServletResponse resp,
                         FormControlValue emailValue,
                         FormControlValue passwordValue,
                         FormControlValue rememberMeValue)
            throws AppException {
        if (req == null || resp == null
                || emailValue == null || passwordValue == null
                || rememberMeValue == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        boolean formValid = true;
        Validator emailValidator = ValidatorFactory.getInstance().
                create(EMAIL);
        String[] validationMessageArray = emailValidator.
                validate(emailValue.getValue());
        if (validationMessageArray.length > 0) {
            emailValue.setValidationMessageList(new ArrayList<>
                    (Arrays.asList(validationMessageArray)));
            formValid = false;
        }
        Validator passwordValidator = ValidatorFactory.getInstance().
                create(PASSWORD);
        validationMessageArray = passwordValidator.
                validate(passwordValue.getValue());
        if (validationMessageArray.length > 0) {
            passwordValue.setValidationMessageList(new ArrayList<>
                    (Arrays.asList(validationMessageArray)));
            formValid = false;
        }
        if (!formValid) return false;
        Login login = getLogin(emailValue.getValue());
        if (login == NULL_LOGIN) {
            String[] messageArray =
                    {"message.profile.email-password-incorrect"};
            req.setAttribute(FORM_MESSAGE_LIST, new ArrayList<>
                    (Arrays.asList(messageArray)));
            return false;
        } else if (login.getAttemptLeft() == 0 || login.getStatus() < 0) {
            String[] messageArray = {"message.profile.login-blocked"};
            req.setAttribute(FORM_MESSAGE_LIST, new ArrayList<>
                    (Arrays.asList(messageArray)));
            return false;
        } else if (!hashPassword(login.getSalt(), passwordValue.getValue()).
                equals(login.getPassword())) {
            updateAttemptLeft(login.getId(), login.getAttemptLeft() - 1);
            String[] messageArray =
                    {"message.profile.email-password-incorrect"};
            req.setAttribute(FORM_MESSAGE_LIST, new ArrayList<>
                    (Arrays.asList(messageArray)));
            return false;
        } else {
            String attemptMax = PropertyManager.
                    getProperty("login.attempt.max");
            if (attemptMax == null || ValidatorFactory.getInstance().
                    create(DIGITS).validate(attemptMax).length > 0) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            updateAttemptLeft(login.getId(), Integer.valueOf(attemptMax));
            UserService userService = new UserService();
            User user = userService.getUser(login);
            if (user == null) {
                String[] detailArray = {String.valueOf(login.getId())};
                throw new AppException(NO_ASSIGNED_USER_TO_LOGIN, detailArray);
            }
            req.getSession().setAttribute(USER_ATTR, user);
            userService.removeUserRemember(user);
            String rememberCookieName =
                    getUserAgentCookieName(req);
            String rememberCookieValue =
                    HashGenerator.hashString();
            CookieManager.removeCookie(resp, rememberCookieName,
                    UrlManager.getUriWithContext(req, SLASH));
            if (rememberMeValue.getValue() != null) {
                Remember remember = userService.addUserRemember(user,
                        rememberCookieName, rememberCookieValue);
                Timestamp currentTimestamp =
                        TimestampManager.getCurrentTimestamp();
                int maxAge = TimestampManager.
                        secondsBetweenTimestamps(currentTimestamp,
                                remember.getValid());
                CookieManager.setCookie(resp, rememberCookieName,
                        rememberCookieValue, maxAge,
                        UrlManager.getUriWithContext(req, SLASH));
            }
            return true;
        }
    }

    /**
     * @param req
     * @param resp
     * @param emailValue
     * @return
     * @throws AppException
     */
    public boolean restore(HttpServletRequest req, HttpServletResponse resp,
                           FormControlValue emailValue) throws AppException {
        if (req == null || resp == null || emailValue == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        boolean formValid = true;
        Validator emailValidator = ValidatorFactory.getInstance().
                create(EMAIL);
        String[] validationMessageArray = emailValidator.
                validate(emailValue.getValue());
        if (validationMessageArray.length > 0) {
            emailValue.setValidationMessageList(new ArrayList<>
                    (Arrays.asList(validationMessageArray)));
            formValid = false;
        }
        if (!formValid) return false;
        Login login = getLogin(emailValue.getValue());
        if (login == NULL_LOGIN) {
            String[] messageArray = {"message.profile.email-not-exist"};
            req.setAttribute(FORM_MESSAGE_LIST, new ArrayList<>
                    (Arrays.asList(messageArray)));
            return false;
        } else if (login.getAttemptLeft() == 0 || login.getStatus() < 0) {
            String[] messageArray = {"message.profile.login-blocked"};
            req.setAttribute(FORM_MESSAGE_LIST, new ArrayList<>
                    (Arrays.asList(messageArray)));
            return false;
        } else {
            String restoreCode = UUID.randomUUID().toString();
            String restoreCookieName =
                    CookieManager.getUserAgentCookieName(req);
            String restoreCookieValue =
                    HashGenerator.hashString(restoreCode);
            String restoreSeconds = PropertyManager.
                    getProperty(RESTORE_SECONDS_VALID_KEY);
            if (restoreSeconds == null || ValidatorFactory.getInstance().
                    create(DIGITS).validate(restoreSeconds).length > 0) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            Timestamp currentTimestamp =
                    TimestampManager.getCurrentTimestamp();
            Timestamp validTimestamp = TimestampManager.
                    secondsToTimestamp(currentTimestamp,
                            Integer.parseInt(restoreSeconds));
            Restore restore = new Restore();
            restore.setLogin(login);
            restore.setCode(restoreCode);
            restore.setCookieName(restoreCookieName);
            restore.setCookieValue(restoreCookieValue);
            restore.setValid(validTimestamp);
            addRestore(restore);
            int maxAge = TimestampManager.
                    secondsBetweenTimestamps(currentTimestamp, validTimestamp);
            CookieManager.setCookie(resp, restoreCookieName, restoreCookieValue,
                    maxAge, UrlManager.getUriWithContext(req, ""));
            req.getSession().setAttribute(RESTORE_EMAIL_ATTR,
                    emailValue.getValue());
            Map<String, String> parameterMap = new HashMap<>();
            parameterMap.put(FORM_EMAIL, emailValue.getValue());
            parameterMap.put(FORM_CODE, restoreCode);
            req.getSession().setAttribute(RESTORE_REF_ATTR,
                    UrlManager.getUriWithContext(req,
                            PROFILE_RESTORE_PATH, parameterMap));
            return true;
        }
    }

    /**
     * @param login
     * @param newPasswordValue
     * @param repeatPasswordValue
     * @return
     * @throws AppException
     */
    public boolean resetPassword(Login login,
                                 FormControlValue newPasswordValue,
                                 FormControlValue repeatPasswordValue)
            throws AppException {
        if (login == null || login == NULL_LOGIN
                || newPasswordValue == null || repeatPasswordValue == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        boolean formValid = true;
        Validator passwordValidator = ValidatorFactory.getInstance().
                create(PASSWORD);
        String[] validationMessageArray = passwordValidator.
                validate(newPasswordValue.getValue());
        if (validationMessageArray.length > 0) {
            newPasswordValue.setValidationMessageList(new ArrayList<>
                    (Arrays.asList(validationMessageArray)));
            formValid = false;
        }
        validationMessageArray = passwordValidator.
                validate(repeatPasswordValue.getValue());
        if (validationMessageArray.length > 0) {
            repeatPasswordValue.setValidationMessageList(new ArrayList<>
                    (Arrays.asList(validationMessageArray)));
            formValid = false;
        }
        if (!formValid) return false;
        if (!newPasswordValue.getValue().
                equals(repeatPasswordValue.getValue())) {
            validationMessageArray =
                    new String[]{"message.profile.repeat-not-equal"};
            repeatPasswordValue.setValidationMessageList(new ArrayList<>
                    (Arrays.asList(validationMessageArray)));
            return false;
        }
        String attemptMax = PropertyManager.getProperty("login.attempt.max");
        if (attemptMax == null || ValidatorFactory.getInstance().
                create(DIGITS).validate(attemptMax).length > 0) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        login.setSalt(UUID.randomUUID().toString());
        login.setPassword(HashGenerator.hashPassword(login.getSalt(),
                newPasswordValue.getValue()));
        login.setAttemptLeft(Integer.valueOf(attemptMax));
        updateLogin(login);
        return true;
    }

    public boolean register(HttpServletRequest req,
                            FormControlValue emailValue,
                            FormControlValue newPasswordValue,
                            FormControlValue repeatPasswordValue)
            throws AppException {
        if (req == null || emailValue == null || newPasswordValue == null
                || repeatPasswordValue == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        boolean formValid = true;
        Validator emailValidator = ValidatorFactory.getInstance().
                create(EMAIL);
        String[] validationMessageArray = emailValidator.
                validate(emailValue.getValue());
        if (validationMessageArray.length > 0) {
            emailValue.setValidationMessageList(new ArrayList<>
                    (Arrays.asList(validationMessageArray)));
            formValid = false;
        }
        Validator passwordValidator = ValidatorFactory.getInstance().
                create(PASSWORD);
        validationMessageArray = passwordValidator.
                validate(newPasswordValue.getValue());
        if (validationMessageArray.length > 0) {
            newPasswordValue.setValidationMessageList(new ArrayList<>
                    (Arrays.asList(validationMessageArray)));
            formValid = false;
        }
        validationMessageArray = passwordValidator.
                validate(repeatPasswordValue.getValue());
        if (validationMessageArray.length > 0) {
            repeatPasswordValue.setValidationMessageList(new ArrayList<>
                    (Arrays.asList(validationMessageArray)));
            formValid = false;
        }
        if (!formValid) return false;
        if (!newPasswordValue.getValue().
                equals(repeatPasswordValue.getValue())) {
            validationMessageArray =
                    new String[]{"message.profile.repeat-not-equal"};
            repeatPasswordValue.setValidationMessageList(new ArrayList<>
                    (Arrays.asList(validationMessageArray)));
            return false;
        }
        Login login = getLogin(emailValue.getValue());
        if (login != null && login != NULL_LOGIN) {
            validationMessageArray =
                    new String[]{"message.profile.email-already-used"};
            emailValue.setValidationMessageList(new ArrayList<>
                    (Arrays.asList(validationMessageArray)));
            return false;
        }
        String activationDaysValid =
                PropertyManager.getProperty("activation.days.valid");
        if (activationDaysValid == null || ValidatorFactory.getInstance().
                create(DIGITS).validate(activationDaysValid).length > 0) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        Activate activate = new Activate();
        activate.setEmail(emailValue.getValue());
        activate.setSalt(UUID.randomUUID().toString());
        activate.setPassword(HashGenerator.hashPassword(activate.getSalt(),
                newPasswordValue.getValue()));
        activate.setCode(UUID.randomUUID().toString());
        Timestamp currentTimestamp = TimestampManager.getCurrentTimestamp();
        int daysValid = Integer.valueOf(activationDaysValid);
        activate.setValid(TimestampManager.
                daysToTimestamp(currentTimestamp, daysValid));
        addActivate(activate);
        req.getSession().setAttribute(ACTIVATION_EMAIL_ATTR,
                emailValue.getValue());
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put(FORM_EMAIL, emailValue.getValue());
        parameterMap.put(FORM_CODE, activate.getCode());
        req.getSession().setAttribute(ACTIVATION_REF_ATTR,
                UrlManager.getUriWithContext(req, PROFILE_ACTIVATE_PATH,
                        parameterMap));
        return true;
    }

    /**
     * @param email
     * @return
     * @throws ServiceException
     */
    public Login getLogin(String email)
            throws AppException {
        try {
            if (email == null) throw new AppException(NULL_NOT_ALLOWED);
            DaoParameter daoParameter = new DaoParameter();
            daoParameter.setWherePredicate(Where.Predicate.
                    get(Property.EMAIL, PredicateOperator.EQUAL, email));
            List<Login> loginList = dao(Login.class.getSimpleName()).
                    read(daoParameter);
            return ((loginList != null) && (loginList.size() > 0))
                    ? loginList.get(0)
                    : NULL_LOGIN;
        } catch (DaoException e) {
            //TODO: replace DaoException by AppException
            String[] detailArray = {email};
            throw new AppException(CREATE_DAO_FACTORY_OR_DAO_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }
    }

    private int updateAttemptLeft(Long loginId, int attemptLeft)
            throws AppException {
        try {
            if (loginId == null) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            DaoParameter daoParameter = new DaoParameter();
            daoParameter.setSetValueArray(new Update.SetValue(
                    Property.ATTEMPT_LEFT, attemptLeft));
            daoParameter.setWherePredicate(Where.Predicate.
                    get(Property.ID, PredicateOperator.EQUAL, loginId));
            return dao(Login.class.getSimpleName()).update(daoParameter);
        } catch (DaoException e) {
            String[] detailArray = {String.valueOf(loginId)};
            throw new AppException(CREATE_DAO_FACTORY_OR_DAO_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }
    }

    /**
     *
     * @param login
     * @return
     * @throws AppException
     */
    public int updateLogin(Login login)
            throws AppException {
        try {
            if (login == null) throw new AppException(NULL_NOT_ALLOWED);
            DaoParameter daoParameter = new DaoParameter();
            daoParameter.setSetValueArray(
                    new Update.SetValue(Property.SALT, login.getSalt()),
                    new Update.SetValue(Property.PASSWORD, login.getPassword()),
                    new Update.SetValue(Property.ATTEMPT_LEFT,
                            login.getAttemptLeft()),
                    new Update.SetValue(Property.STATUS, login.getStatus()));
            daoParameter.setWherePredicate(Where.Predicate.
                    get(Property.ID, PredicateOperator.EQUAL, login.getId()));
            return dao(Login.class.getSimpleName()).update(daoParameter);
        } catch (DaoException e) {
            throw new AppException(CREATE_DAO_FACTORY_OR_DAO_ERROR);
        }
    }

    /**
     *
     * @param login
     * @return
     * @throws AppException
     */
    public Long addLogin(Login login)
            throws AppException {
        try {
            if (login == null) throw new AppException(NULL_NOT_ALLOWED);
            DaoParameter daoParameter = new DaoParameter();
            daoParameter.setEntity(login);
            return dao(Login.class.getSimpleName()).create(daoParameter);
        } catch (DaoException e) {
            throw new AppException(CREATE_DAO_FACTORY_OR_DAO_ERROR);
        }
    }

    /**
     *
     * @param restore
     * @return
     * @throws AppException
     */
    public Long addRestore(Restore restore)
            throws AppException {
        try {
            if (restore == null) throw new AppException(NULL_NOT_ALLOWED);
            DaoParameter daoParameter = new DaoParameter();
            daoParameter.setEntity(restore);
            return dao(Restore.class.getSimpleName()).create(daoParameter);
        } catch (DaoException e) {
            String[] detailArray = {restore.getLogin().getEmail()};
            throw new AppException(CREATE_DAO_FACTORY_OR_DAO_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }
    }

    /**
     *
     * @param restoreList
     * @return
     * @throws AppException
     */
    public int removeRestoreList(List<Restore> restoreList)
            throws AppException {
        try {
            if (restoreList == null) throw new AppException(NULL_NOT_ALLOWED);
            int result = 0;
            for (Restore restore : restoreList) {
                DaoParameter daoParameter = new DaoParameter();
                daoParameter.setWherePredicate(Where.Predicate.
                        get(Restore.Property.ID, PredicateOperator.EQUAL,
                                restore.getId()));
                result += dao(Restore.class.getSimpleName()).
                        delete(daoParameter);
            }
            return result;
        } catch (DaoException e) {
            throw new AppException(CREATE_DAO_FACTORY_OR_DAO_ERROR);
        }
    }


    public Login getRestoreLogin(String restoreEmail, String restoreCode,
                                 String cookieName, String cookieValue)
            throws AppException {
        try {
            if (restoreEmail == null || restoreCode == null
                    || cookieName == null || cookieValue == null) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            Login login = getLogin(restoreEmail);
            if (login == NULL_LOGIN) return login;
            DaoParameter daoParameter = new DaoParameter();
            daoParameter.setWherePredicate(Where.Predicate.
                    get(Restore.Property.LOGIN_ID, PredicateOperator.EQUAL,
                            login.getId()));
            List<Restore> restoreList = dao(Restore.class.getSimpleName()).
                    read(daoParameter);
            if (restoreList == null || restoreList.size() == 0) {
                return NULL_LOGIN;
            }
            for (Restore restore : restoreList) {
                if (restore.getCode().equals(restoreCode) &&
                        restore.getCookieName().equals(cookieName) &&
                        restore.getCookieValue().equals(cookieValue)) {
                    removeRestoreList(restoreList);
                    return login;
                }
            }
            removeRestoreList(restoreList);
            return NULL_LOGIN;
        } catch (DaoException e) {
            throw new AppException(CREATE_DAO_FACTORY_OR_DAO_ERROR);
        }
    }

    public Long addActivate(Activate activate) throws AppException {
        try {
            DaoParameter daoParameter = new DaoParameter();
            daoParameter.setWherePredicate(Where.Predicate.
                    get(Activate.Property.EMAIL, PredicateOperator.EQUAL,
                            activate.getEmail()));
            List<Activate> activateList = dao(Activate.class.getSimpleName()).
                    read(daoParameter);
            if (activateList != null || activateList.size() > 0) {
                removeActivateList(activateList);
            }
            daoParameter = new DaoParameter();
            daoParameter.setEntity(activate);
            return dao(Activate.class.getSimpleName()).create(daoParameter);
        } catch (DaoException e) {
            throw new AppException(CREATE_DAO_FACTORY_OR_DAO_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

    private int removeActivateList(List<Activate> activateList)
            throws AppException {
        try {
            int result = 0;
            for (Activate activate : activateList) {
                DaoParameter daoParameter = new DaoParameter();
                daoParameter.setWherePredicate(Where.Predicate.
                        get(Activate.Property.ID, PredicateOperator.EQUAL,
                                activate.getId()));
                result += dao(Activate.class.getSimpleName()).
                        delete(daoParameter);
            }
            return result;
        } catch (DaoException e) {
            throw new AppException(CREATE_DAO_FACTORY_OR_DAO_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

    public Login getActivateLogin(String activationEmail, String activationCode)
            throws AppException {
        if (activationEmail == null || activationCode == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        try {
            DaoParameter daoParameter = new DaoParameter();
            daoParameter.setWherePredicate(Where.Predicate.
                    get(Activate.Property.EMAIL, PredicateOperator.EQUAL,
                            activationEmail));
            List<Activate> activateList = dao(Activate.class.getSimpleName()).
                    read(daoParameter);
            if (activateList == null || activateList.size() == 0) {
                return NULL_LOGIN;
            }
            Activate activate = activateList.get(0);
            Timestamp currentTimestamp = TimestampManager.getCurrentTimestamp();
            if (!activationCode.equals(activate.getCode()) || TimestampManager.
                    secondsBetweenTimestamps(currentTimestamp,
                            activate.getValid()) < 0) return NULL_LOGIN;
            String attemptMax = PropertyManager.
                    getProperty("login.attempt.max");
            if (attemptMax == null || ValidatorFactory.getInstance().
                    create(DIGITS).validate(attemptMax).length > 0) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            Login login = new Login();
            login.setEmail(activate.getEmail());
            login.setSalt(activate.getSalt());
            login.setPassword(activate.getPassword());
            login.setAttemptLeft(Integer.valueOf(attemptMax));
            login.setStatus(0);
            return login;
        } catch (DaoException e) {
            throw new AppException(CREATE_DAO_FACTORY_OR_DAO_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

    public int removeActivate(String email) throws AppException {
        if (email == null) throw new AppException(NULL_NOT_ALLOWED);
        try {
            DaoParameter daoParameter = new DaoParameter();
            daoParameter.setWherePredicate(Where.Predicate.
                    get(Activate.Property.EMAIL, PredicateOperator.EQUAL,
                            email));
            return dao(Activate.class.getSimpleName()).delete(daoParameter);
        } catch (DaoException e) {
            throw new AppException(CREATE_DAO_FACTORY_OR_DAO_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

}
