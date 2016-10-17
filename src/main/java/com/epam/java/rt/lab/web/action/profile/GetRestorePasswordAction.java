package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.action.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetRestorePasswordAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
//        try (LoginService loginService = new LoginService()) {
//            req.getSession().removeAttribute("restoreEmail");
//            req.getSession().removeAttribute("restoreRef");
//            String email = req.getParameter("email");
//            String code = req.getParameter("code");
//            if (email != null && code != null) {
//                String cookieName = CookieManager.getUserAgentCookieName(req);
//                if (cookieName != null) {
//                    String cookieValue = CookieManager.getCookieValue(req, cookieName);
//                    CookieManager.removeCookie(req, resp, cookieName, UrlManager.getContextUri(req, ""));
//                    Login login = loginService.getRestoreLogin(email, code, cookieName, cookieValue);
//                    if (login != null) {
//                        req.getSession().setAttribute("login", login);
//                        Form form = FormFactory.getInstance().create("restore-password");
//                        form.setActionParameterString(UrlManager.getRequestParameterString(req));
//                        req.setAttribute("restoreForm", form);
//                        req.getRequestDispatcher("/WEB-INF/jsp/profile/restore-password.jsp").forward(req, resp);
//                        return;
//                    }
//                }
//            }
//            resp.sendRedirect(UrlManager.getContextUri(req, "/home"));
//        } catch (FormException e) {
//            throw new ActionException("exception.action.profile.restore.form", e.getCause());
//        } catch (ServletException | IOException e) {
//            throw new ActionException("exception.action.profile.restore-password.jsp", e.getCause());
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.restore-password.category", e.getCause());
//        }
    }
}