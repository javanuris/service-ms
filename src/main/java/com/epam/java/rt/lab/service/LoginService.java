package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Update;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.entity.rbac.Activate;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Remember;
import com.epam.java.rt.lab.entity.rbac.Restore;
import com.epam.java.rt.lab.util.GlobalProperties;
import com.epam.java.rt.lab.util.TimestampCompare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * category-ms
 */
public class LoginService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    public LoginService()
            throws ServiceException {
    }

    /**
     * @param email
     * @return
     * @throws ServiceException
     */
    public Login getLogin(String email)
            throws ServiceException {
        try {
            List<Login> loginList = dao(Login.class.getSimpleName()).read(new DaoParameter()
                    .setWherePredicate(Where.Predicate.get(
                            Login.Property.EMAIL,
                            Where.Predicate.PredicateOperator.EQUAL,
                            email
                    ))
            );
            return loginList != null && loginList.size() > 0 ? loginList.get(0) : null;
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.login.get-login.dao", e.getCause());
        }
    }

    /**
     *
     * @param login
     * @return
     * @throws ServiceException
     */
    public int updateAttemptLeft(Login login)
            throws ServiceException {
        try {
            return dao(Login.class.getSimpleName()).update(new DaoParameter()
                    .setSetValueArray(new Update.SetValue(Login.Property.ATTEMPT_LEFT, login.getAttemptLeft()))
                    .setWherePredicate(Where.Predicate.get(
                            Login.Property.ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            login.getId()
                    ))
            );
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.login.update-attempt-left.dao", e.getCause());
        }
    }

    /**
     *
     * @param login
     * @return
     * @throws ServiceException
     */
    public int updateLogin(Login login)
            throws ServiceException {
        try {
            return dao(Login.class.getSimpleName()).update(new DaoParameter()
                    .setSetValueArray(
                            new Update.SetValue(Login.Property.SALT, login.getSalt()),
                            new Update.SetValue(Login.Property.PASSWORD, login.getPassword()),
                            new Update.SetValue(Login.Property.ATTEMPT_LEFT, login.getAttemptLeft()),
                            new Update.SetValue(Login.Property.STATUS, login.getStatus())
                    )
                    .setWherePredicate(Where.Predicate.get(
                            Login.Property.ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            login.getId()
                    ))
            );
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.login.update-login.dao", e.getCause());
        }
    }

    /**
     *
     * @param login
     * @return
     * @throws ServiceException
     */
    public Long addLogin(Login login)
            throws ServiceException {
        try {
            return dao(Login.class.getSimpleName()).create(new DaoParameter().setEntity(login));
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.login.add-login.dao", e.getCause());
        }
    }

    /**
     *
     * @param restore
     * @return
     * @throws ServiceException
     */
    public Long addRestore(Restore restore)
            throws ServiceException {
        try {
            return dao(Restore.class.getSimpleName()).create(new DaoParameter()
                    .setEntity(restore)
            );
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.login.add-restore.dao", e.getCause());
        }
    }

    /**
     *
     * @param restoreList
     * @return
     * @throws ServiceException
     */
    public int removeRestoreList(List<Restore> restoreList)
            throws ServiceException {
        try {
            int result = 0;
            for (Restore restore : restoreList) {
                result += dao(Restore.class.getSimpleName()).delete(new DaoParameter()
                        .setWherePredicate(Where.Predicate.get(
                                Restore.Property.ID,
                                Where.Predicate.PredicateOperator.EQUAL,
                                restore.getId()
                        ))
                );
            }
            return result;
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.login.remove-restore-list.dao", e.getCause());
        }
    }

    /**
     *
     * @param restoreEmail
     * @param restoreCode
     * @param cookieName
     * @param cookieValue
     * @return
     * @throws ServiceException
     */
    public Login getRestoreLogin(String restoreEmail, String restoreCode, String cookieName, String cookieValue)
            throws ServiceException {
        try {
            Login login = getLogin(restoreEmail);
            if (login == null) return null;
            List<Restore> restoreList = dao(Restore.class.getSimpleName()).read(new DaoParameter()
                    .setWherePredicate(Where.Predicate.get(
                            Restore.Property.LOGIN_ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            login.getId()
                    ))
            );
            if (restoreList == null || restoreList.size() == 0) return null;
            for (Restore restore : restoreList) {
                if (restore.getCode().equals(restoreCode) &&
                        restore.getCookieName().equals(cookieName) &&
                        restore.getCookieValue().equals(cookieValue)) {
                    removeRestoreList(restoreList);
                    return login;
                }
            }
            removeRestoreList(restoreList);
            return null;
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.login.get-restore-login.dao", e.getCause());
        }
    }

    public Long addActivate(Activate activate)
            throws ServiceException {
        try {
            List<Activate> activateList = dao(Activate.class.getSimpleName()).read(new DaoParameter()
                    .setWherePredicate(Where.Predicate.get(
                            Activate.Property.EMAIL,
                            Where.Predicate.PredicateOperator.EQUAL,
                            activate.getEmail()
                    ))
            );
            if (activateList != null) removeActivateList(activateList);
            return dao(Activate.class.getSimpleName()).create(new DaoParameter().setEntity(activate));
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.login.add-activate.dao", e.getCause());
        }
    }

    public int removeActivateList(List<Activate> activateList)
            throws ServiceException {
        try {
            int result = 0;
            for (Activate activate : activateList) {
                result += dao(Activate.class.getSimpleName()).delete(new DaoParameter()
                        .setWherePredicate(Where.Predicate.get(
                                Activate.Property.ID,
                                Where.Predicate.PredicateOperator.EQUAL,
                                activate.getId()
                        ))
                );
            }
            return result;
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.login.remove-activate-list.dao", e.getCause());
        }
    }

    public Login getActivateLogin(String activationEmail, String activationCode) throws ServiceException {
        if (activationEmail == null || activationCode == null) return null;
        try {
            List<Activate> activateList = dao(Activate.class.getSimpleName()).read(new DaoParameter()
                    .setWherePredicate(Where.Predicate.get(
                            Activate.Property.EMAIL,
                            Where.Predicate.PredicateOperator.EQUAL,
                            activationEmail
                    ))
            );
            if (activateList == null || activateList.size() == 0) return null;
            Activate activate = activateList.get(0);
            if (!activationCode.equals(activate.getCode()) || TimestampCompare.secondsBetweenTimestamps
                    (TimestampCompare.getCurrentTimestamp(), activate.getValid()) < 0) return null;
            Login login = new Login();
            login.setEmail(activate.getEmail());
            login.setSalt(activate.getSalt());
            login.setPassword(activate.getPassword());
            login.setAttemptLeft(Integer.valueOf(GlobalProperties.getProperty("login.attempt.max")));
            login.setStatus(0);
            return login;
        } catch (ServiceException | DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.login.get-actvate-login.dao", e.getCause());
        }
    }

    public int removeActivate(String email)
            throws ServiceException {
        try {
            return dao(Activate.class.getSimpleName()).delete(new DaoParameter()
                    .setWherePredicate(Where.Predicate.get(
                            Activate.Property.EMAIL,
                            Where.Predicate.PredicateOperator.EQUAL,
                            email
                    ))
            );
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.login.remove-activate.dao", e.getCause());
        }
    }


//    public Login confirmActivationCode(String activationEmail, String activationCode) throws DaoException {
//        if (activationEmail == null || activationCode == null) return null;
//        Login login = new Login();
//        login.setEmail(activationEmail);
//        Map<String, Object> activationMap = getRelEntity(login, "Activation");
//        removeRelEntity(login, "Activation");
//        if (activationMap == null || !activationCode.equals(activationMap.get("code")) ||
//                TimestampCompare.secondsBetweenTimestamps(TimestampCompare.getCurrentTimestamp(),
//                (Timestamp) activationMap.get("valid")) <= 0) return null;
//        login.setPassword((String) activationMap.get("password.regex"));
//        login.setAttemptLeft(Integer.valueOf(GlobalProperties.getProperty("login.attempt.max")));
//        login.setStatus(0);
//        return login;
//    }
//

//    public int addLogin(Login login) throws DaoException {
//        Dao_ dao = daoFactory.createDao("Login");
//        return dao.create(login);
//    }
//
//    public int updatePassword(Login login)
//            throws DaoException, SQLException, ConnectionException {
//        logger.debug("updateLogin");
//        Dao_ dao = daoFactory.createDao("Login");
//        return dao.update(login, "id", "password.regex");
//    }
//
//
//    public boolean isLoginExists(String email) throws DaoException {
//        Login login = new Login();
//        login.setEmail(email);
//        Dao_ dao = daoFactory.createDao("Login");
//        if (dao.getFirst(login, "email.regex", null) != null) return true;
//        Map<String, Object> activationMap = (Map<String, Object>) dao.getRelEntity(login, "Activation");
//        return activationMap != null;
//    }
//
//    private <T> Map<String, Object> getRelEntity(T entity, String relEntityName) throws DaoException {
//        Dao_ dao = daoFactory.createDao("Login");
//        Map<String, Object> relEntityMap = (Map<String, Object>) dao.getRelEntity(entity, relEntityName);
//        return relEntityMap;
//    }
//
//    private <T> void setRelEntity(T entity, String relEntityName, Object relEntity) throws DaoException {
//        if (relEntity != null) {
//            Dao_ dao = daoFactory.createDao("Login");
//            dao.setRelEntity(entity, relEntityName, relEntity);
//        }
//    }
//
//    private <T> void removeRelEntity(T entity, String relEntityName) throws DaoException {
//        Dao_ dao = daoFactory.createDao("Login");
//        dao.removeRelEntity(entity, relEntityName);
//    }
//
//    public String createActivationCode(String email, String password) throws DaoException {
//        try {
//            String activationCode = HashGenerator.hashString(email.concat(password));
//            Login login = new Login();
//            login.setEmail(email);
//            login.setPassword(password);
//            setRelEntity(login, "Activation", activationCode);
//            return activationCode;
//        } catch (DaoException e) {
//            e.printStackTrace();
//            throw e;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
}
