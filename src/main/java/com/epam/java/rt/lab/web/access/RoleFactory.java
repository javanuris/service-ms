package com.epam.java.rt.lab.web.access;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.StringCombiner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.exception.AppExceptionCode.PROPERTY_EMPTY_OR_CONTENT_ERROR;
import static com.epam.java.rt.lab.exception.AppExceptionCode.PROPERTY_READ_ERROR;
import static com.epam.java.rt.lab.util.PropertyManager.COMMA;
import static com.epam.java.rt.lab.web.access.AccessExceptionCode.ROLE_NOT_FOUND;

public final class RoleFactory {

    private static final String ANONYMOUS = "anonymous";
    private static final String AUTHORIZED = "authorized";
    private static final String ACCESS_PROPERTY_FILE = "access.properties";

    private static class Holder {

        private static final RoleFactory INSTANCE = new RoleFactory();

    }

    private Map<String, Role> roleMap = new HashMap<>();

    private RoleFactory() {
    }

    public static RoleFactory getInstance() {
        return Holder.INSTANCE;
    }

    public void initRoleMap() throws AppException {
        ClassLoader classLoader = RoleFactory.class.getClassLoader();
        InputStream inputStream = classLoader.
                getResourceAsStream(ACCESS_PROPERTY_FILE);
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            this.roleMap.clear();
            Enumeration<?> uris = properties.propertyNames();
            while (uris.hasMoreElements()) {
                String uri = (String) uris.nextElement();
                String uriRoles = properties.getProperty(uri);
                String[] uriRoleArray = StringCombiner.
                        splitSpaceLessNames(uriRoles, COMMA);
                Permission permission = new Permission(uri);
                for (String anUriRoleArray : uriRoleArray) {
                    addPermissionToRole(permission, anUriRoleArray);
                }
            }
            if (this.roleMap.size() == 0) {
                String[] detailArray = {ACCESS_PROPERTY_FILE};
                throw new AppException(PROPERTY_EMPTY_OR_CONTENT_ERROR,
                        detailArray);
            }
        } catch (IOException e) {
            String[] detailArray = {ACCESS_PROPERTY_FILE};
            throw new AppException(PROPERTY_READ_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }
    }

    private void addPermissionToRole(Permission permission, String roleName) {
        Role role = this.roleMap.get(roleName);
        if (role == null) {
            role = new Role(roleName);
            this.roleMap.put(roleName, role);
        }
        role.addPermission(permission.getUri());
    }

    public Role create(String roleName) throws AppException {
        if (roleName == null) throw new AppException(NULL_NOT_ALLOWED);
        Role role = this.roleMap.get(roleName);
        if (role == null) {
            String[] detailArray = {ACCESS_PROPERTY_FILE, roleName};
            throw new AppException(ROLE_NOT_FOUND, detailArray);
        }
        return this.roleMap.get(roleName);
    }

    public Role createAnonymous() throws AppException {
        return this.roleMap.get(ANONYMOUS);
    }

    public Role createAuthorized() throws AppException {
        return this.roleMap.get(AUTHORIZED);
    }

    public Map<String, Role> getRoleMap() {
        return this.roleMap;
    }

}
