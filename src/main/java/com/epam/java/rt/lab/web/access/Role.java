package com.epam.java.rt.lab.web.access;

import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class Role {

    private String name;
    private List<String> permissionList = new ArrayList<>();

    public Role(String name) {
        this.name = name;
    }

    public void addPermission(String uri) {
        this.permissionList.add(uri);
    }

    public boolean verifyPermission(String uri) {
        return this.permissionList.contains(uri);
    }

    public String getName() {
        return this.name;
    }

}
