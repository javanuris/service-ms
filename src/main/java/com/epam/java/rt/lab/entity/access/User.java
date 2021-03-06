package com.epam.java.rt.lab.entity.access;

import com.epam.java.rt.lab.entity.BaseEntity;
import com.epam.java.rt.lab.web.access.Role;

import static com.epam.java.rt.lab.util.PropertyManager.SPACE;

public class User extends BaseEntity {
    private String firstName;
    private String middleName;
    private String lastName;
    private Login login;
    private Role role;
    private Long avatarId;

    public static final User NULL_USER = new User();

    public User() {
    }

    public User(Long id, String firstName, String middleName,
                String lastName, Login login, Role role) {
        super(id);
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.login = login;
        this.role = role;
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
        if (firstName != null) name = name + SPACE + firstName;
        if (middleName != null) name = name + SPACE + middleName;
        if (lastName != null) name = name + SPACE + lastName;
        return name.trim();
    }

}
