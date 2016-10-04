package com.epam.java.rt.lab.web.access;

/**
 * service-ms
 */
public class Permission {

    private String uri;
    private String actionName;

    public Permission(String uri, String actionName) {
        this.uri = uri;
        this.actionName = actionName;
    }

    public String getUri() {
        return uri;
    }

    public String getActionName() {
        return actionName;
    }

}
