package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Permission;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;

import java.util.*;

/**
 * service-ms
 */
public class UserService {
    private static final UUID REMEMBER_COOKIE_NAME = UUID.randomUUID();
    private static Map<UUID, User> rememberUserMap = new HashMap<>();


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

    public static String getRememberCookieName() {
        return UserService.REMEMBER_COOKIE_NAME.toString();
    }

    public static String setRememberUser(User user) {
        if (rememberUserMap.containsValue(user)) {
            for (Map.Entry<UUID, User> entry : rememberUserMap.entrySet()) {
                if (entry.getValue() == user) {
                    rememberUserMap.remove(entry.getKey());
                    break;
                }
            }
        }
        UUID uuid = UUID.randomUUID();
        while (rememberUserMap.containsKey(uuid)) uuid = UUID.randomUUID();
        rememberUserMap.put(uuid, user);
        return uuid.toString();
    }

    public static User getRememberUser(String rememberCookieValue) {
        return rememberUserMap.get(UUID.fromString(rememberCookieValue));
    }

}
