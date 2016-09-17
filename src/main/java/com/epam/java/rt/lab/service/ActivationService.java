package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;

/**
 * service-ms
 */
public class ActivationService extends BaseService {

    public ActivationService() throws ConnectionException, DaoException {
    }

    public String getActivationCode(String firstName, String email) {
        return null;
    }

}
