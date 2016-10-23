package com.epam.java.rt.lab.entity.access;

import com.epam.java.rt.lab.entity.BaseEntity;
import com.epam.java.rt.lab.entity.EntityProperty;

import java.sql.Timestamp;

public class Activate extends BaseEntity {

    public enum Property implements EntityProperty {
        ID,
        EMAIL,
        SALT,
        PASSWORD,
        CODE,
        VALID;

        @Override
        public Class getEntityClass() {
            return Activate.class;
        }
    }

    private String email;
    private String salt;
    private String password;
    private String code;
    private Timestamp valid;

    public Activate() {
    }

    public Activate(Long id, String email, String salt, String password,
                    String code, Timestamp valid) {
        super(id);
        this.email = email;
        this.salt = salt;
        this.password = password;
        this.code = code;
        this.valid = valid;
    }

    public Long getId() {
        return super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Timestamp getValid() {
        return valid;
    }

    public void setValid(Timestamp valid) {
        this.valid = valid;
    }
}
