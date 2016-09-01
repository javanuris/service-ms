package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;

import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class RoleService {

    public static Role getRole(User user) {
        Role role = new Role();
        role.setId(1L);
        role.setName("admin");
        role.setUriList(PermissionService.getPermissionUriList(role));
        return role;
    }

    @Deprecated
    public static Role getAnonymous() {
        return new Role(2L, "anonymous", PermissionService.getAnonymous());
    }

    public static List<Role> getRoleList() {
        List<Role> roleList = new ArrayList<>();
        Role role = new Role();
        role.setId(1L);
        role.setName("admin");
        role.setUriList(PermissionService.getPermissionUriList(role));
        roleList.add(role);
        return roleList;
    }

}
