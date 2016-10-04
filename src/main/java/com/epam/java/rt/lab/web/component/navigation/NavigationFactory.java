package com.epam.java.rt.lab.web.component.navigation;

import com.epam.java.rt.lab.util.StringArray;
import com.epam.java.rt.lab.web.access.Role;
import com.epam.java.rt.lab.web.access.RoleException;
import com.epam.java.rt.lab.web.access.RoleFactory;

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
        String point = ".";
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
                String subNavsProperty = properties.getProperty(navigationName.concat(subNavs));
                if (subNavsProperty != null) {
                    String subNamPrefix = navigationName.concat(point);
                    for (String subNavigationName : StringArray.splitSpaceLessNames(subNavsProperty, comma)) {
                        navigation.addNavigation(new Navigation(
                                properties.getProperty(subNamPrefix.concat(subNavigationName).concat(uri)),
                                properties.getProperty(subNamPrefix.concat(subNavigationName).concat(label))
                        ));
                    }
                }
                navigationList.add(navigation);

            }
            for (Map.Entry<String, Role> entry : RoleFactory.getInstance().getRoleMap().entrySet()) {
                List<Navigation> roleNavigationList = new ArrayList<>();
                for (Navigation navigation : navigationList) {
                    if (entry.getValue().verifyPermission(navigation.getUri()))
                        roleNavigationList.add(navigation);
                }
                this.roleNavigationMap.put(entry.getValue().getName(), roleNavigationList);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new NavigationException("exception.component.nav.properties", e.getCause());
        } catch (RoleException e) {
            e.printStackTrace();
            throw new NavigationException("exception.component.navigation.role-factory", e.getCause());
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
