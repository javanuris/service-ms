package com.epam.java.rt.lab.web.component.navigation;

import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.service.RoleService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.util.StringArray;

import java.io.IOException;
import java.util.*;

/**
 * service-ms
 */
public class NavigationFactory {

    private static class Holder {

        private static final NavigationFactory INSTANCE;

        static {
            try {
                INSTANCE = new NavigationFactory();
            } catch (Exception e) {
                e.printStackTrace();
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    private Map<String, List<Navigation>> roleNavigationMap = new HashMap<>();

    private NavigationFactory() throws NavigationException {
        fillNavigationMap();
    }

    private void fillNavigationMap() throws NavigationException {
        Properties properties = new Properties();
        String comma = ",";
        String navs = "navs";
        String uri = ".uri";
        String label = ".label";
        String subNavs = ".navs";
        try {
            properties.load(NavigationFactory.class.getClassLoader().getResourceAsStream("nav.properties"));
            List<Navigation> navigationList = new ArrayList<>();
            for (String navigationName : StringArray.splitSpaceLessNames(properties.getProperty(navs), comma)) {
                Navigation navigation = new Navigation(
                        properties.getProperty(navigationName.concat(uri)),
                        properties.getProperty(navigationName.concat(label))
                );
                String subNavsProperty = properties.getProperty((navigationName).concat(subNavs));
                if (subNavsProperty != null) {
                    for (String subNavigationName : StringArray.splitSpaceLessNames(subNavsProperty, comma)) {
                        navigation.addNavigation(new Navigation(
                                properties.getProperty(navigationName.concat(uri)),
                                properties.getProperty(navigationName.concat(label))
                        ));
                    }
                }
                navigationList.add(navigation);

            }
            RoleService roleService = new RoleService();
            for (Role role : roleService.getRoleList()) {
                List<Navigation> roleNavigationList = new ArrayList<>();
                for (Navigation navigation : navigationList) {
                    if (role.getUriList().contains(navigation.getUri()))
                        roleNavigationList.add(navigation);
                }
                this.roleNavigationMap.put(role.getName(), roleNavigationList);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new NavigationException("exception.component.navigation.properties", e.getCause());
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new NavigationException("exception.component.navigation.service", e.getCause());
        }
    }

    public static NavigationFactory getInstance() throws NavigationException {
        try {
            return Holder.INSTANCE;
        } catch (ExceptionInInitializerError e) {
            e.printStackTrace();
            throw new NavigationException("exception.component.navigation.instance", e.getCause());
        }
    }

    public List<Navigation> create(String roleName) {
        return this.roleNavigationMap.get(roleName);
    }

}
