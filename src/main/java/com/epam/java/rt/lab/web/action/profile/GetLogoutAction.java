package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.CookieManager;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.web.action.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.java.rt.lab.web.action.ActionExceptionCode.ACTION_INSTANTIATE_SERVICE_ERROR;

/**
 * This is logout action, which invalidates session and removes remember token
 *
 * @see AppException
 * @see PropertyManager
 * @see CookieManager
 * @see UrlManager
 * @see UserService
 */
public class GetLogoutAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
        try (UserService userService = new UserService()) {
            User user = (User) req.getSession().
                    getAttribute(PropertyManager.USER_ATTR);
//            req.getSession().removeAttribute("user");
//            req.getSession().removeAttribute("navigationList");
            req.getSession().invalidate();
            CookieManager.removeCookie(req, resp,
                    CookieManager.getUserAgentCookieName(req),
                    UrlManager.getContextUri(req, PropertyManager.SLASH));
            userService.removeUserRemember(user);
            resp.sendRedirect(UrlManager.getContextUri(req,
                    PropertyManager.SLASH));
        } catch (ServiceException e) {
            String[] detailArray = {UserService.class.getSimpleName()};
            throw new AppException(ACTION_INSTANTIATE_SERVICE_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        } catch (IOException e) {
            String[] detailArray = {PropertyManager.SLASH};
            throw new AppException(ACTION_INSTANTIATE_SERVICE_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }
    }

}
