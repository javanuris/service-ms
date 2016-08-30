package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Permission;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;

import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class UserService {

    public static User getUser(Login login) {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Rollan");
        user.setMiddleName("M.");
        user.setLastName("TAIGULOV");
        user.setRole(RoleService.getRole(user));
        return user;
    }

    public static User getAnonymous() {
        return new User(2L, "anonymous", "", "", null, RoleService.getAnonymous());
    }

}
