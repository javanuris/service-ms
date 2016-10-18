package com.epam.java.rt.lab.entity.access;

import com.epam.java.rt.lab.entity.BaseEntity;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.entity.File;

import java.io.InputStream;
import java.sql.Timestamp;

/**
 * category-ms
 */
public class Avatar extends BaseEntity implements File {

    public enum Property implements EntityProperty {
        ID,
        NAME,
        TYPE,
        FILE,
        MODIFIED;

        @Override
        public Class getEntityClass() {
            return Avatar.class;
        }
    }

    public static final Avatar NULL_AVATAR = new Avatar();

    private String name;
    private String type;
    private InputStream file;
    private Timestamp modified;

    public Avatar() {
    }

    public Avatar(Long id, String name, String type, InputStream file, Timestamp modified) {
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
