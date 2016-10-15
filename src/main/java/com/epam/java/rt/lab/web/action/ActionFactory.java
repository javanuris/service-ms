package com.epam.java.rt.lab.web.action;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.web.access.Permission;
import com.epam.java.rt.lab.web.access.RoleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.epam.java.rt.lab.web.action.ActionExceptionCode.*;

public final class ActionFactory {

    private static final Logger LOGGER = LoggerFactory.
            getLogger(ActionFactory.class);

    private static final String GET = "Get";
    private static final String POST = "Post";
    private static final String WARN_ACTION_CLASS_NOT_FOUND =
            "Action class not found";

    private static class Holder {

        private static final ActionFactory INSTANCE = new ActionFactory();

    }

    private Map<String, Action> actionMap = new HashMap<>();

    private ActionFactory() {
    }

    public void initActionMap() throws AppException {
        this.actionMap.clear();
        String actionPackagePath = ActionFactory.class.getPackage().getName();
        for (Permission permission : RoleFactory.getInstance().
                getPermissionList()) {
            String actionPath = actionPackagePath + PropertyManager.POINT;
            String actionName = permission.getActionName();
            int lastPointIndex = actionName.lastIndexOf(PropertyManager.POINT)
                    + 1;
            if (lastPointIndex > 0) {
                actionPath = actionPath + actionName.substring(0,
                        lastPointIndex);
                actionName = actionName.substring(lastPointIndex);
            }
            addAction(GET, permission.getUri(), actionPath, actionName);
            addAction(POST, permission.getUri(), actionPath, actionName);
        }
        if (this.actionMap.size() == 0) {
            throw new AppException(ACTION_MAP_EMPTY);
        }
    }

    private void addAction(String method, String uri, String actionPath,
                           String actionName) throws AppException {
        try {
            Class actionClass = Class.forName(actionPath + method + actionName);
            Action actionObject = (Action) actionClass.newInstance();
            this.actionMap.put(getActionMapKey(method, uri), actionObject);
        } catch (ClassNotFoundException e) {
            LOGGER.warn(WARN_ACTION_CLASS_NOT_FOUND
                    + PropertyManager.LEFT_PARENTHESIS
                    + actionPath + method + actionName
                    + PropertyManager.RIGHT_PARENTHESIS);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new AppException(ACTION_OBJECT_INSTANTIATE_ERROR);
        }
    }

    private static String getActionMapKey(String method, String pathInfo) {
        return method.toUpperCase() + pathInfo.toLowerCase();
    }

    public static ActionFactory getInstance() {
        return Holder.INSTANCE;
    }

    public Action create(String method, String pathInfo) throws AppException {
        Action actionObject = this.actionMap.
                get(getActionMapKey(method, pathInfo));
        if (actionObject == null) {
            String[] detailArray = {method, pathInfo};
            throw new AppException(ACTION_BY_URI_NOT_FOUND, detailArray);
        }
        return actionObject;
    }

}
