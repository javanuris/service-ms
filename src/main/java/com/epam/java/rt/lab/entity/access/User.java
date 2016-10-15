package com.epam.java.rt.lab.entity.access;

import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.entity.BaseEntity;
import com.epam.java.rt.lab.web.access.Role;

/**
 * category-ms
 */
public class User extends BaseEntity {
    private String firstName;
    private String middleName;
    private String lastName;
    private Login login;
    private Role role;
    private Long avatarId;

    public enum Property implements EntityProperty {
        ID,
        FIRST_NAME,
        MIDDLE_NAME,
        LAST_NAME,
        LOGIN_ID,
        ROLE_NAME,
        AVATAR_ID;

        @Override
        public Class getEntityClass() {
            return User.class;
        }
    }

    public User() {
    }

    public User(Long id, String firstName, String middleName, String lastName, Login login, Role role) {
        super(id);
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.login = login;
        this.role = role;
    }

    public Long getId() {
        return super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getAvatarId() { return avatarId; }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }

    public String getName() {
        String name = "";
        if (firstName != null) name = name.concat(" ").concat(firstName);
        if (middleName != null) name = name.concat(" ").concat(middleName);
        if (lastName != null) name = name.concat(" ").concat(lastName);
        return name.trim();
    }

}
