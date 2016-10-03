package com.epam.java.rt.lab.web.Rbac;

import com.epam.java.rt.lab.util.StringArray;
import com.epam.java.rt.lab.util.validator.ValidatorException;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;
import com.epam.java.rt.lab.web.component.form.Form;
import com.epam.java.rt.lab.web.component.form.FormControl;
import com.epam.java.rt.lab.web.component.form.FormException;

import java.io.IOException;
import java.util.*;

/**
 * service-ms
 */
public final class RoleFactory {

    private static final String ANONYMOUS = "anonymous";

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
            throw new RoleException("exception.web.rbac.role-factory.properties", e.getCause());
        }
    }

    public static RoleFactory getInstance() throws FormException {
        try {
            return Holder.INSTANCE;
        } catch (ExceptionInInitializerError e) {
            throw new FormException("exception.web.rbac.role-factory.init", e.getCause());
        }
    }

    public Role create(String roleName) throws RoleException {
        return this.roleMap.get(roleName);
    }

    public Role createAnonymous() throws RoleException {
        return this.roleMap.get(ANONYMOUS);
    }

    public List<Permission> getPermissionList() {
        return this.permissionList;
    }

}
