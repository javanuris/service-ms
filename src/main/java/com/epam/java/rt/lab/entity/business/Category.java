package com.epam.java.rt.lab.entity.business;

import com.epam.java.rt.lab.entity.BaseEntity;
import com.epam.java.rt.lab.entity.EntityProperty;

import java.sql.Timestamp;

/**
 * category-ms
 */
public class Category extends BaseEntity {

    private Long parentId;
    private Timestamp created;
    private String name;

    public enum Property implements EntityProperty {
        ID,
        PARENT_ID,
        CREATED,
        NAME;

        @Override
        public Class getEntityClass() {
            return Category.class;
        }
    }

    public Category() {
    }

    public Category(Long id, Long parentId, Timestamp created, String name) {
        super(id);
        this.parentId = parentId;
        this.created = created;
        this.name = name;
    }

    public Long getId() {

        return super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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
