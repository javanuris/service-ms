package com.epam.java.rt.lab.entity.rbac;

import com.epam.java.rt.lab.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class Role extends BaseEntity {
    private String name;
    private List<String> uriList = new ArrayList<>();

    public Role() {
    }

    public Role(Long id, String name, List<String> uriList) {
        super(id);
        this.name = name;
        this.uriList = uriList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUriList() {
        return uriList;
    }

    public void setUriList(List<String> uriList) {
        this.uriList = uriList;
    }
}
