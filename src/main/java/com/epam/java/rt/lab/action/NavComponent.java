package com.epam.java.rt.lab.action;

/**
 * service-ms
 */
public class NavComponent {
    private String active;
    private String name;
    private String link;

    public NavComponent(String active, String name, String link) {
        this.active = active;
        this.name = name;
        this.link = link;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
