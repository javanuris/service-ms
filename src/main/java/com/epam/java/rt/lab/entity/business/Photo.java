package com.epam.java.rt.lab.entity.business;

import com.epam.java.rt.lab.entity.BaseEntity;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.entity.File;

import java.io.InputStream;
import java.sql.Timestamp;

/**
 * category-ms
 */
public class Photo extends BaseEntity implements File {

    public enum Property implements EntityProperty {
        ID,
        NAME,
        TYPE,
        FILE,
        MODIFIED;

        @Override
        public Class getEntityClass() {
            return Photo.class;
        }
    }

    private String name;
    private String type;
    private InputStream file;
    private Timestamp modified;

    public static final Photo NULL_PHOTO = new Photo();

    public Photo() {
    }

    public Photo(Long id, String name, String type, InputStream file, Timestamp modified) {
        super(id);
        this.name = name;
        this.type = type;
        this.file = file;
        this.modified = modified;
    }

    public Long getId() {
        return super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }
}
