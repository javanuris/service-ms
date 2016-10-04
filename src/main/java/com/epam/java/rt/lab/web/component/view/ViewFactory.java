package com.epam.java.rt.lab.web.component.view;

import com.epam.java.rt.lab.util.StringArray;

import java.io.IOException;
import java.util.*;

/**
 * category-ms
 */
public class ViewFactory {

    private static class Holder {
        // hacky trick from http://stackoverflow.com/a/2284890
        // there was many variations of singleton (anti-)pattern, so this trick is more fit requirements,
        // because give to user opportunity to catch exceptions during initialization of object
        private static final ViewFactory INSTANCE;

        static {
            try {
                INSTANCE = new ViewFactory();
            } catch (Exception e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    private Map<String, View> viewMap = new HashMap<>();

    private ViewFactory() throws ViewException {
        fillViewMap();
    }

    private void fillViewMap() throws ViewException {
        Properties properties = new Properties();
        String comma = ",";
        String point = ".";
        String views = "views";
        String controls = ".controls";
        String label = ".label";
        String type = ".type";
        String dictionary = ".dictionary";
        try {
            properties.load(ViewFactory.class.getClassLoader().getResourceAsStream("view.properties"));
            this.viewMap.clear();
            for (String viewName : StringArray.splitSpaceLessNames(properties.getProperty(views), comma)) {
                View view = new View();
                List<ViewControl> viewControlList = new ArrayList<>();
                for (String controlName : StringArray.splitSpaceLessNames(properties.getProperty(viewName.concat(controls)), comma)) {
                    String propertyPrefix = viewName.concat(point).concat(controlName);
                    viewControlList.add(
                            new ViewControl(
                                    propertyPrefix,
                                    properties.getProperty(propertyPrefix.concat(label)),
                                    properties.getProperty(propertyPrefix.concat(type)),
                                    properties.getProperty(propertyPrefix.concat(dictionary))
                            )
                    );
                }
                view.setViewControlList(viewControlList);
                this.viewMap.put(viewName, view);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ViewException("exception.component.view.properties", e.getCause());
        }
    }

    public static ViewFactory getInstance() throws ViewException {
        try {
            return Holder.INSTANCE;
        } catch (ExceptionInInitializerError e) {
            throw new ViewException("exception.component.view.init", e.getCause());
        }
    }

    public View create(String viewName) {
        return this.viewMap.get(viewName).copyDef();
    }

}
