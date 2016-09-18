package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.util.HashManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * service-ms
 */
public class LoginService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    public LoginService() throws ConnectionException, DaoException {
    }

    public Login getLogin(String email, String password) throws DaoException {
        Login login = new Login();
        login.setEmail(email);
        login.setPassword(password);
        Dao dao = daoFactory.createDao("Login");
        login = dao.getFirst(login, "email, password", null);
        return login;
    }

    public int addLogin(Login login) throws DaoException {
        Dao dao = daoFactory.createDao("Login");
        return dao.create(login);
    }

    public int updatePassword(Login login)
            throws DaoException, SQLException, ConnectionException {
        logger.debug("updateLogin");
        Dao dao = daoFactory.createDao("Login");
        int updateCount = dao.update(login, "id", "password");
        return updateCount;
    }

    public boolean isLoginExists(String email) throws DaoException {
        Login login = new Login();
        login.setEmail(email);
        Dao dao = daoFactory.createDao("Login");
        if (dao.getFirst(login, "email", null) != null) return true;
        Map<String, Object> activationMap = (Map<String, Object>) dao.getRelEntity(login, "Activation");
        return activationMap != null;
    }

    private <T> Map<String, Object> getRelEntity(T entity, String relEntityName) throws DaoException {
        Dao dao = daoFactory.createDao("Login");
        Map<String, Object> relEntityMap = (Map<String, Object>) dao.getRelEntity(entity, relEntityName);
        return relEntityMap;
    }

    private <T> void setRelEntity(T entity, String relEntityName, Object relEntity) throws DaoException {
        if (relEntity != null) {
            Dao dao = daoFactory.createDao("Login");
            dao.setRelEntity(entity, relEntityName, relEntity);
        }
    }

    private <T> void removeRelEntity(T entity, String relEntityName) throws DaoException {
        Dao dao = daoFactory.createDao("Login");
        dao.removeRelEntity(entity, relEntityName);
    }

//    public Map<String, Object> getRemember(String rememberName) throws DaoException {
//        return getRelEntity(rememberName, "Remember");
//    }
//
//    public void setRemember(Map<String, Object> rememberValueMap) throws DaoException {
//        setRelEntity(null, "Remember", rememberValueMap);
//    }
//
//    public void removeRemember(Long userId) throws DaoException {
//        removeRelEntity(userId, "Remember");
//    }
//
    public String createActivationCode(String email, String password) throws DaoException {
        try {
            String activationCode = HashManager.hashString(email.concat(password));
            Login login = new Login();
            login.setEmail(email);
            login.setPassword(password);
            setRelEntity(login, "Activation", activationCode);
            return activationCode;
        } catch (DaoException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Login confirmActivationCode(String activationEmail, String activationCode) throws DaoException {
        if (activationEmail == null || activationCode == null) return null;
        Login login = new Login();
        login.setEmail(activationEmail);
        Map<String, Object> activationMap = getRelEntity(login, "Activation");
        removeRelEntity(login, "Activation");
        if (activationMap == null && activationCode.equals(activationMap.get("code"))) return null;
        login.setPassword((String) activationMap.get("password"));
        login.setAttemptLeft(5);
        login.setStatus(0);
        return login;
    }

}
