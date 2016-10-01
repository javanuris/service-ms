package com.epam.java.rt.lab.web.action;

import com.epam.java.rt.lab.entity.rbac.Permission;
import com.epam.java.rt.lab.service.PermissionService;
import com.epam.java.rt.lab.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Service Management System
 */
public final class ActionFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionFactory.class);

    private static class Holder {

        private static final ActionFactory INSTANCE;

        static {
            try {
                INSTANCE = new ActionFactory();
            } catch (Exception e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    private Map<String, Action> actionMap = new HashMap<>();

    private ActionFactory() throws ActionException {
        fillActionMap();
    }

    private void fillActionMap() throws ActionException {
        String point = ".";
        String get = "Get";
        String post = "Post";
        String action = "Action";
        this.actionMap.clear();
        String actionPackagePath = ActionFactory.class.getPackage().getName().concat(point);
        try (PermissionService permissionService = new PermissionService()) {
            List<Permission> permissionList = permissionService.getPermissionList();
            for (Permission permission : permissionList) {
                String actionName = permission.getAction();
                String actionPath = actionPackagePath;
                int pointIndex = actionName.lastIndexOf(point) + 1;
                if (pointIndex > 0) {
                    actionPath = actionPackagePath.concat(actionName.substring(0, pointIndex));
                    actionName = actionName.substring(pointIndex).concat(action);
                } else {
                    actionName = actionName.concat(action);
                }
                addAction(
                        get,
                        permission.getUri(),
                        actionPath,
                        actionName
                );
                addAction(
                        post,
                        permission.getUri(),
                        actionPath,
                        actionName
                );
            }
        } catch (ServiceException e) {
            throw new ActionException("exception.action.factory.permission", e.getCause());
        }
    }

    private void addAction(String method, String uri, String actionPath, String actionName) throws ActionException {
        try {
            actionName = actionPath.concat(method).concat(actionName);
            Class actionClass = Class.forName(actionName);
            Action actionObject = (Action) actionClass.newInstance();
            this.actionMap.put(getActionMapKey(method.toUpperCase(), uri), actionObject);
        } catch (IllegalAccessException | ClassNotFoundException e) {
            LOGGER.debug("exception.action.factory.not-found: {}", actionName);
        } catch (InstantiationException e) {
            throw new ActionException("exception.action.factory.action-object", e.getCause());
        }
    }

    private static String getActionMapKey(String method, String pathInfo) {
        return method.concat(pathInfo);
    }

    public static ActionFactory getInstance() throws ActionException {
        try {
            return Holder.INSTANCE;
        } catch (ExceptionInInitializerError e) {
            throw new ActionException("exception.action.factory.init", e.getCause());
        }
    }

    public Action create(String method, String pathInfo) {
        return this.actionMap.get(getActionMapKey(method, pathInfo));
    }

}
