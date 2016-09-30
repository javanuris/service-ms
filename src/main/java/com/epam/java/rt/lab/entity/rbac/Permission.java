package com.epam.java.rt.lab.entity.rbac;

import com.epam.java.rt.lab.entity.BaseEntity;
import com.epam.java.rt.lab.entity.EntityProperty;

/**
 * service-ms
 */
public class Permission extends BaseEntity {
    private String uri;
    private String action;

    public enum Property implements EntityProperty {
        ID,
        URI,
        ACTION;

        @Override
        public Class getEntityClass() {
            return Permission.class;
        }
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
