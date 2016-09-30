package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Update;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.entity.rbac.Activate;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Restore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * service-ms
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
     * @param restore
     * @return
     * @throws ServiceException
     */
    public int addRestore(Restore restore)
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

    public int addActivate(Activate activate)
            throws ServiceException {
        try {
            return dao(Activate.class.getSimpleName()).create(new DaoParameter()
                    .setEntity(activate)
            );
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.login.add-activate.dao", e.getCause());
        }
    }



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
}
