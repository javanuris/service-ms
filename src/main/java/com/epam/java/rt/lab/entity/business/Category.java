package com.epam.java.rt.lab.entity.business;

import com.epam.java.rt.lab.entity.BaseEntity;

import java.sql.Timestamp;

public class Category extends BaseEntity {

    private Long parentId;
    private Timestamp created;
    private String name;

    public static final Category NULL_CATEGORY = new Category();

    public Category() {
    }

    public Category(Long id, Long parentId, Timestamp created, String name) {
        super(id);
        this.parentId = parentId;
        this.created = created;
        this.name = name;
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
