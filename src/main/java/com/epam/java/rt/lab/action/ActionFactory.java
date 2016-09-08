package com.epam.java.rt.lab.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Service Management System
 */
public class ActionFactory {
    private static final Logger logger = LoggerFactory.getLogger(ActionFactory.class);
    private static final Map<String, Action> actionMap = new HashMap<>();
    private static final Lock actionMapLock = new ReentrantLock();

    public static Action getAction(String pathInfo) throws ActionException {
        int i = pathInfo.indexOf("-");
        while (i > 0 && i < pathInfo.length()) {
            pathInfo = pathInfo.substring(0, i)
                    .concat(pathInfo.substring(i + 1, i + 2).toUpperCase())
                    .concat(pathInfo.substring(i + 2));
            i = pathInfo.indexOf("-", i);
        }
        String actionPath = pathInfo.substring(1).replace("/", ".");
        int actionNamePoint = actionPath.lastIndexOf(".") + 1;
        String actionPathAndName = actionPath.substring(0, actionNamePoint)
                .concat(actionPath.substring(actionNamePoint, actionNamePoint + 1).toUpperCase())
                .concat(actionPath.substring(actionNamePoint + 1)).concat("Action");
        logger.debug(actionPathAndName);
        Action action = ActionFactory.actionMap.get(actionPathAndName);
        if (action != null) return action;
        if (ActionFactory.actionMapLock.tryLock()) {
            action = ActionFactory.actionMap.get(actionPathAndName);
            if (action != null) {
                ActionFactory.actionMapLock.unlock();
                return action;
            }
            try {
                Class<?> actionClass = Class.forName(ActionFactory.class.getPackage()
                        .getName().concat(".").concat(actionPathAndName));
                if (actionClass.getAnnotation(WebAction.class) == null)
                    throw new ActionException("Action '" + actionPathAndName + "' not found");;
                Action actionObject = (Action) actionClass.newInstance();
                ActionFactory.actionMap.put(actionPathAndName, actionObject);
                return actionObject;
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                throw new ActionException(e.getMessage());
            } finally {
                ActionFactory.actionMapLock.unlock();
            }
        }
        throw new ActionException("Action '" + actionPathAndName + "' not found");
    }

}
