package com.epam.java.rt.lab.web.action;

/**
 * This abstract class used to store jsp path for action
 */
public abstract class BaseAction implements Action {

    private String jspName;

    public String getJspName() {
        return jspName;
    }

    public void setJspName(String jspName) {
        this.jspName = jspName;
    }

}