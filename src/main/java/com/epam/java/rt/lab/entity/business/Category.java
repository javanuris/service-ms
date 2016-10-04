package com.epam.java.rt.lab.entity.business;

import com.epam.java.rt.lab.entity.BaseEntity;
import com.epam.java.rt.lab.entity.EntityProperty;

import java.sql.Timestamp;

/**
 * service-ms
 */
public class Category extends BaseEntity {

    private Category parent;
    private Timestamp created;
    private String name;

    public enum Property implements EntityProperty {
        ID,
        CATEGORY_ID,
        CREATED,
        NAME;

        @Override
        public Class getEntityClass() {
            return Category.class;
        }
    }

    public Category() {
    }

    public Category(Long id, Category parent, Timestamp created, String name) {
        super(id);
        this.parent = parent;
        this.created = created;
        this.name = name;
    }

    public Long getId() {

        return super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
