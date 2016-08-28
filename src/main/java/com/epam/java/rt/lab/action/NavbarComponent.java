package com.epam.java.rt.lab.action;

/**
 * service-ms
 */
public class NavbarComponent {
    private static final NavItem[] navArray = {
            new NavItem("navbar.application.list", "/application"),
            new NavItem("navbar.execution.list", "/execution"),
            new NavItem("navbar.service.list", "/service"),
            new NavItem("navbar.employee.list", "/employee"),
    };

    public static NavItem[] getNavArray() {
        return navArray;
    }

    public static class NavItem {
        private String name;
        private String link;

        public NavItem(String name, String link) {
            this.name = name;
            this.link = link;
        }

        public String getName() {
            return name;
        }

        public String getLink() {
            return link;
        }
    }
}