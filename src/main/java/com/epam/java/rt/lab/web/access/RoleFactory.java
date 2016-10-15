package com.epam.java.rt.lab.web.access;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.StringArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.epam.java.rt.lab.web.access.AccessExceptionCode.*;

public final class RoleFactory {

    private static final String ANONYMOUS = "anonymous";
    private static final String AUTHORIZED = "authorized";
    private static final String ACCESS_PROPERTY_FILE = "access.properties";

    private static class Holder {

        private static final RoleFactory INSTANCE = new RoleFactory();

    }

    private Map<String, Role> roleMap = new HashMap<>();
    private List<Permission> permissionList = new ArrayList<>();

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
            this.permissionList.clear();
            this.roleMap.clear();
            Enumeration<?> uris = properties.propertyNames();
            while (uris.hasMoreElements()) {
                String uri = (String) uris.nextElement();
                String actionAndRoles = properties.getProperty(uri);
                String[] actionAndRolesArray = StringArray.
                        splitSpaceLessNames(actionAndRoles,
                                PropertyManager.COMMA);
                Permission permission = new Permission(uri,
                        actionAndRolesArray[0]);
                this.permissionList.add(permission);
                for (int i = 1; i < actionAndRolesArray.length; i++) {
                    addPermissionToRole(permission, actionAndRolesArray[i]);
                }
            }
            if (this.roleMap.size() == 0 || this.permissionList.size() == 0) {
                String[] detailArray = {ACCESS_PROPERTY_FILE};
                throw new AppException(PROPERTY_EMPTY_OR_CONTENT_ERROR,
                        detailArray);
            }
        } catch (IOException e) {
            String[] detailArray = {ACCESS_PROPERTY_FILE, e.getMessage()};
            throw new AppException(PROPERTY_READ_ERROR, e.getCause(),
                    detailArray);
        }
    }

    private void addPermissionToRole(Permission permission, String roleName) {
        Role role = this.roleMap.get(roleName);
        if (role == null) role = new Role(roleName);
        role.addPermission(permission.getUri());
    }

    public Role create(String roleName) throws AppException {
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

    public List<Permission> getPermissionList() {
        return this.permissionList;
    }

    public Map<String, Role> getRoleMap() {
        return this.roleMap;
    }

}
