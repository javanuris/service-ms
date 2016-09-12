package com.epam.java.rt.lab.component;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * service-ms
 */
public class NavComponent {
    private static final Logger logger = LoggerFactory.getLogger(NavComponent.class);
    private static final Map<String, Object[]> roleNavbarItemMap = new HashMap<>();
    private static final Lock updateMapLock = new ReentrantLock();

    private static List<NavbarItem> getNavbarItemList(String fileName) throws IOException {
        logger.debug("getNavbarItemList");
        List<NavbarItem> navbarItemList = new ArrayList<>();
        InputStream inputStream = NavComponent.class.getClassLoader().getResourceAsStream(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String readLine;
        String[] partArray;
        while ((readLine = bufferedReader.readLine()) != null) {
            partArray = readLine.split("=");
            navbarItemList.add(new NavbarItem(partArray[0], partArray[1]));
        }
        logger.debug("getNavbarItemList.size() = {}", navbarItemList.size());
        return navbarItemList;
    }

    public static void updateRoleNavbarItemMap()
            throws InterruptedException, IOException, ConnectionException, DaoException {
        logger.debug("updateRoleNavbarItemMap");
        try {
            if (updateMapLock.tryLock(10, TimeUnit.MILLISECONDS)) {
                List<NavbarItem> navbarItemList = getNavbarItemList("navbar.properties");
                List<NavbarItem> roleNavbarItemList;
                List<String> uriList;
                List<Role> roleList = (new RoleService()).getRoleList();
                for (Role role : roleList) {
                    logger.debug("role.name = {}", role.getName());
                    uriList = role.getUriList();
                    roleNavbarItemList = new ArrayList<>();
                    for (NavbarItem navbarItem : navbarItemList) {
                        if (uriList.contains(navbarItem.getLink()))
                            roleNavbarItemList.add(navbarItem);
                    }
                    logger.debug("roleNavbarItemList.size() = {}", roleNavbarItemList.size());
                    roleNavbarItemMap.put(role.getName(), roleNavbarItemList.toArray());
                }
            }
        } finally {
            updateMapLock.unlock();
        }
    }

    public static Object[] getNavbarItemArray(Role role) {
        try {
            if (roleNavbarItemMap.size() == 0) updateRoleNavbarItemMap();
            return roleNavbarItemMap.get(role.getName());
        } catch (InterruptedException | ConnectionException | IOException | DaoException e) {
            // TODO unhandled
            e.printStackTrace();
        }
        return null;
    }

    public static class NavbarItem {
        private String name;
        private String link;

        public NavbarItem(String name, String link) {
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