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

    private static final String SIGN_POINT = ".";
    private static final String GET = "Get";
    private static final String POST = "Post";
    private static final String ACTION = "Action";

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionFactory.class);
    private static final Map<String, Action> actionMap = new HashMap<>();
    private static final Lock mapLock = new ReentrantLock();

    /**
     * @param method
     * @param pathInfo
     * @return
     * @throws ActionException
     */
    public static Action getAction(String method, String pathInfo) throws ActionException {
        if (ActionFactory.actionMap.size() == 0) init();
        return ActionFactory.actionMap.get(getActionMapKey(method, pathInfo));
    }

    /**
     *
     * @throws ActionException
     */
    public static void init() throws ActionException {
        if (ActionFactory.mapLock.tryLock()) {
            ActionFactory.actionMap.clear();
            String actionPackagePath = ActionFactory.class.getPackage().getName().concat(SIGN_POINT);
            try (PermissionService permissionService = new PermissionService()) {
                List<Permission> permissionList = permissionService.getPermissionList();
                for (Permission permission : permissionList) {
                    ActionFactory.addAction(
                            GET,
                            permission.getUri(),
                            actionPackagePath,
                            permission.getAction()
                    );
                    ActionFactory.addAction(
                            POST,
                            permission.getUri(),
                            actionPackagePath,
                            permission.getAction()
                    );
                }
            } catch (ServiceException e) {
                throw new ActionException("exception.action.factory.permission", e.getCause());
            } finally {
                ActionFactory.mapLock.unlock();
            }
        }
    }

    private static void addAction(String method, String uri, String actionPackage, String action) throws ActionException {
        try {
            int pointIndex = action.lastIndexOf(SIGN_POINT) + 1;
            if (pointIndex > 0) {
                action = actionPackage.concat(action.substring(0, pointIndex))
                        .concat(method).concat(action.substring(pointIndex)).concat(ACTION);
            } else {
                action = actionPackage.concat(method).concat(action).concat(ACTION);
            }
            Class actionClass = Class.forName(action);
            Action actionObject = (Action) actionClass.newInstance();
            ActionFactory.actionMap.put(getActionMapKey(method.toUpperCase(), uri), actionObject);
        } catch (IllegalAccessException | ClassNotFoundException e) {
            LOGGER.debug("exception.action.factory.not-found: {}", action);
        } catch (InstantiationException e) {
            throw new ActionException("exception.action.factory.action-object", e.getCause());
        }
    }

    private static String getActionMapKey(String method, String pathInfo) {
        return method.concat(pathInfo);
    }

}
