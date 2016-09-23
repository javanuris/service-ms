package com.epam.java.rt.lab.component;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.service.RoleService;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * service-ms
 */
public class NavigationComponent {
    private static final Logger logger = LoggerFactory.getLogger(NavigationComponent.class);
    private static final Map<String, Object[]> roleNavbarItemMap = new HashMap<>();
    private static final Map<String, Object[]> navItemMap = new HashMap<>();
    private static final Lock updateMapLock = new ReentrantLock();

    public static void updateNavigationItemMap()
            throws InterruptedException, IOException, ConnectionException, DaoException {
        logger.debug("updateNavigationItemMap");
        try {
            if (updateMapLock.tryLock(10, TimeUnit.MILLISECONDS)) {
                Properties navigationProperties = new Properties();
                navigationProperties.load(NavigationComponent.class.getClassLoader().getResourceAsStream("navigation.properties"));
                List<NavigationItem> navbarItemList = new ArrayList<>();
                List<NavigationItem> navItemList = new ArrayList<>();
                Enumeration enumeration = navigationProperties.propertyNames();
                while (enumeration.hasMoreElements()) {
                    String key = (String) enumeration.nextElement();
                    if (key.startsWith("navbar.")) {
                        navbarItemList.add(new NavigationItem(key, navigationProperties.getProperty(key)));
                    } else if (key.startsWith("nav.")) {
                        navItemList.add(new NavigationItem(key, navigationProperties.getProperty(key)));
                    }
                }
                Collections.sort(navbarItemList);
                Collections.sort(navItemList);
                for (Role role : new RoleService().getRoleList()) {
                    List<NavigationItem> roleNavbarItemList = new ArrayList<>();
                    for (NavigationItem navbarItem : navbarItemList) {
                        if (role.getUriList().contains(navbarItem.getLink()))
                            roleNavbarItemList.add(navbarItem);
                    }
                    roleNavbarItemMap.put(role.getName(), roleNavbarItemList.toArray());
                }
                for (NavigationItem navbarItem : navbarItemList) {
                    String prefix = "nav.".concat(navbarItem.getName().substring(7)).concat(".");
                    List<NavigationItem> navbarNavItemList = new ArrayList<>();
                    for (NavigationItem navItem : navItemList) {
                        if (navItem.getName().startsWith(prefix))
                            navbarNavItemList.add(navItem);
                    }
                    if (navbarNavItemList.size() > 0)
                        navItemMap.put(prefix.substring(0, prefix.length() - 1), navbarNavItemList.toArray());
                }
            }
        } finally {
            updateMapLock.unlock();
        }
    }

    public static Object[] getNavbarItemArray(Role role) {
        logger.debug("getNavbarItemArray({})", role.getName());
        try {
            if (roleNavbarItemMap.size() == 0) updateNavigationItemMap();
            return roleNavbarItemMap.get(role.getName());
        } catch (InterruptedException | ConnectionException | IOException | DaoException e) {
            // TODO unhandled
            e.printStackTrace();
        }
        return null;
    }

    public static Object[] getNavItemArray(String navPrefix) {
        try {
            if (navItemMap.size() == 0) updateNavigationItemMap();
            return navItemMap.get(navPrefix);
        } catch (InterruptedException | ConnectionException | IOException | DaoException e) {
            // TODO unhandled
            e.printStackTrace();
        }
        return null;
    }

    public static class NavigationItem implements Comparable<NavigationItem> {
        private int index;
        private String name;
        private String link;

        public NavigationItem(String name, String link) {
            String index = name.substring(name.lastIndexOf(".") + 1);
            if (ValidatorFactory.isOnlyDigits(index)) {
                this.index = Integer.valueOf(index);
                this.name = name.substring(0, name.length() - index.length() - 1);
            } else {
                this.index = 0;
                this.name = name;
            }
            this.link = link;
        }

        public String getName() {
            return name;
        }

        public String getLink() {
            return link;
        }

        @Override
        public int compareTo(NavigationItem o) {
            return this.index - o.index;
        }
    }
}