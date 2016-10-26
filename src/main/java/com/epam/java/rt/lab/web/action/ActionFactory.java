package com.epam.java.rt.lab.web.action;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.StringCombiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.epam.java.rt.lab.exception.AppExceptionCode.PROPERTY_EMPTY_OR_CONTENT_ERROR;
import static com.epam.java.rt.lab.exception.AppExceptionCode.PROPERTY_READ_ERROR;
import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.web.action.ActionExceptionCode.*;

public final class ActionFactory {

    static final Logger LOGGER = LoggerFactory.
            getLogger(ActionFactory.class);

    private static final String ACTION_PROPERTY_FILE = "action.properties";
    private static final int JSP_NAME = 0;
    private static final int GET_ACTION = 1;
    private static final int POST_ACTION = 2;

    private static class Holder {

        private static final ActionFactory INSTANCE = new ActionFactory();

    }

    private final Map<String, Action> actionMap = new HashMap<>();

    private ActionFactory() {
    }

    public void initActionMap() throws AppException {
        LOGGER.debug("initActionMap()");
        String actionPackagePath = ActionFactory.class.
                getPackage().getName() + POINT;
        ClassLoader classLoader = ActionFactory.class.getClassLoader();
        InputStream inputStream = classLoader.
                getResourceAsStream(ACTION_PROPERTY_FILE);
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            this.actionMap.clear();
            Enumeration<?> uris = properties.propertyNames();
            while (uris.hasMoreElements()) {
                String uri = (String) uris.nextElement();
                String uriProperties = properties.getProperty(uri);
                String[] uriPropertyArray = StringCombiner.
                        splitSpaceLessNames(uriProperties, COMMA);
                addAction(GET, uri, uriPropertyArray[JSP_NAME],
                        actionPackagePath + uriPropertyArray[GET_ACTION]);
                if (uriPropertyArray.length - 1 == POST_ACTION) {
                    addAction(POST, uri, uriPropertyArray[JSP_NAME],
                            actionPackagePath + uriPropertyArray[POST_ACTION]);
                }
            }
            if (this.actionMap.size() == 0) {
                String[] detailArray = {ACTION_PROPERTY_FILE};
                throw new AppException(PROPERTY_EMPTY_OR_CONTENT_ERROR,
                        detailArray);
            }
        } catch (IOException e) {
            String[] detailArray = {ACTION_PROPERTY_FILE};
            throw new AppException(PROPERTY_READ_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }
    }

    private void addAction(String method, String uri, String jspName,
                           String actionName) throws AppException {
        LOGGER.debug("addAction({}, {}, {}, {})",
                method, uri, jspName, actionName);
        try {
            Class actionClass = Class.forName(actionName);
            Action actionObject = (Action) actionClass.newInstance();
            ((BaseAction) actionObject).setJspName(JSP_BASE_PATH + jspName);
            this.actionMap.put(getActionMapKey(method, uri), actionObject);
        } catch (ClassNotFoundException e) {
            String[] detailArray = {actionName};
            throw new AppException(ACTION_CLASS_NOT_FOUND,
                    e.getMessage(), e.getCause(), detailArray);
        } catch (IllegalAccessException | InstantiationException e) {
            String[] detailArray = {actionName};
            throw new AppException(ACTION_OBJECT_INSTANTIATE_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }
    }

    private static String getActionMapKey(String method, String pathInfo) {
        return method.toUpperCase() + pathInfo.toLowerCase();
    }

    public static ActionFactory getInstance() {
        return Holder.INSTANCE;
    }

    public Action create(String method, String pathInfo) throws AppException {
        LOGGER.debug("create({}, {})", method, pathInfo);
        Action actionObject = this.actionMap.
                get(getActionMapKey(method, pathInfo));
        if (actionObject == null) {
            String[] detailArray = {method, pathInfo};
            throw new AppException(ACTION_BY_URI_NOT_FOUND, detailArray);
        }
        return actionObject;
    }

}
