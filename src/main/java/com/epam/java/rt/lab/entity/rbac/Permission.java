package com.epam.java.rt.lab.entity.rbac;

import com.epam.java.rt.lab.entity.BaseEntity;
import com.epam.java.rt.lab.entity.EntityProperty;

/**
 * service-ms
 */
public class Permission extends BaseEntity {
    private String uri;

    public enum Property implements EntityProperty {
        ID,
        URI
    }

    public Permission() {
    }

    public Permission(Long id, String uri) {
        super(id);
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
