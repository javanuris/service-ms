package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.entity.rbac.Permission;
import com.epam.java.rt.lab.entity.rbac.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class PermissionService {

    public static List<String> getPermissionUriList(Role role) {
        List<Permission> permissionList = new ArrayList<>();
        permissionList.add(new Permission(1L, "/"));
        permissionList.add(new Permission(2L, "/home"));
        permissionList.add(new Permission(3L, "/profile/view"));
        permissionList.add(new Permission(4L, "/profile/logout"));
        permissionList.add(new Permission(5L, "/application/list"));
        permissionList.add(new Permission(6L, "/execution/list"));
        permissionList.add(new Permission(7L, "/employee/list"));
        permissionList.add(new Permission(8L, "/service/list"));
        List<String> uriList = new ArrayList<>();
        for (Permission permission : permissionList)
            uriList.add(permission.getUri());
        return uriList;
    }

    @Deprecated
    public static List<String> getAnonymous() {
        List<Permission> permissionList = new ArrayList<>();
        permissionList.add(new Permission(1L, "/"));
        permissionList.add(new Permission(2L, "/home"));
        permissionList.add(new Permission(3L, "/profile/login"));
        List<String> uriList = new ArrayList<>();
        for (Permission permission : permissionList)
            uriList.add(permission.getUri());
        return uriList;
    }

}
