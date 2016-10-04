package com.epam.java.rt.lab.web.access;

import com.epam.java.rt.lab.util.StringArray;

import java.io.IOException;
import java.util.*;

/**
 * category-ms
 */
public final class RoleFactory {

    private static final String ANONYMOUS = "anonymous";
    private static final String AUTHORIZED = "authorized";

    private static class Holder {

        private static final RoleFactory INSTANCE;

        static {
            try {
                INSTANCE = new RoleFactory();
            } catch (Exception e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    private Map<String, Role> roleMap = new HashMap<>();
    private List<Permission> permissionList = new ArrayList<>();

    private RoleFactory() throws RoleException {
        fillRoleMap();
    }

    private void fillRoleMap() throws RoleException {
        Properties properties = new Properties();
        String comma = ",";
        String roles = "roles";
        try {
            properties.load(RoleFactory.class.getClassLoader().getResourceAsStream("rbac.properties"));
            this.roleMap.clear();
            this.permissionList.clear();
            List<String> uriList = new ArrayList<>();
            for (String roleName : StringArray.splitSpaceLessNames(properties.getProperty(roles), comma)) {
                Role role = new Role(roleName);
                for (String uri : StringArray.splitSpaceLessNames(properties.getProperty(roleName), comma)) {
                    if (!uriList.contains(uri)) {
                        uriList.add(uri);
                        this.permissionList.add(new Permission(uri, properties.getProperty(uri)));
                    }
                    role.addPermission(uri);
                }
                this.roleMap.put(roleName, role);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RoleException("exception.web.access.role-factory.properties", e.getCause());
        }
    }

    public static RoleFactory getInstance() throws RoleException {
        try {
            return Holder.INSTANCE;
        } catch (ExceptionInInitializerError e) {
            throw new RoleException("exception.web.access.role-factory.init", e.getCause());
        }
    }

    public Role create(String roleName) throws RoleException {
        return this.roleMap.get(roleName);
    }

    public Role createAnonymous() throws RoleException {
        return this.roleMap.get(ANONYMOUS);
    }

    public Role createAuthorized() throws RoleException {
        return this.roleMap.get(AUTHORIZED);
    }

    public List<Permission> getPermissionList() {
        return this.permissionList;
    }

    public Map<String, Role> getRoleMap() {
        return this.roleMap;
    }

}
