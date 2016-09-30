package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * service-ms
 */
public class GetForgotPasswordAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetForgotPasswordAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
//        try (LoginService loginService = new LoginService()) {
//            Form form = FormFactory.getInstance().create("profile-forgot-password");
////            switch (Form.getStatus("profile.forgot-password", UrlManager.getContextPathInfo(req), 100)) {
////                case 1:
////                    form = Form.create("profile.forgot-password");
////                    break;
////                case 0:
////                    form = Form.set("profile.forgot-password",
////                            new Form.Item("profile.forgot-password.password.label", "password", "profile.forgot-password.password.label",
////                                    ValidatorFactory.Validator.getRegex(ValidatorFactory.getRegex("password.regex"), "validation.password")),
////                            new Form.Item("profile.forgot-password.confirm.label", "password", "profile.forgot-password.confirm.label",
////                                    ValidatorFactory.Validator.getRegex(ValidatorFactory.getRegex("password.regex"), "validation.password")),
////                            new Form.Item("profile.forgot-password.submit.label", "submit", "", null),
////                            new Form.Item("profile.forgot-password.login.label", "button",
////                                    UrlManager.getContextUri(req, "/profile/login"), null)
////                    );
////                    break;
////                case -1:
////                    throw new ActionException("exception.action.forgot-password.form-status");
////            }
//            req.setAttribute("forgotPasswordForm", form);
//            String forgotEmail = req.getParameter("email.regex");
//            String forgotCode = req.getParameter("code");
//            logger.debug("email: {}, code: {}", forgotEmail, forgotCode);
//            req.getSession().removeAttribute("forgotEmail");
//            req.getSession().removeAttribute("forgotCode");
//            req.getSession().removeAttribute("forgotRef");
//            String forgotCookieName = "_".concat(CookieManager.getDependantCookieName(req));
//            String forgotCookieValue = CookieManager.getDependantCookieValue(req, forgotCookieName);
//            logger.debug("forgotCookieValue = {}", forgotCookieValue);
//            Login login = null;
//            if (forgotCookieValue != null && forgotEmail != null && forgotCode != null &&
//                    forgotCookieValue.equals(forgotCode)) {
//                login = loginService.confirmForgotCode(forgotEmail, forgotCode);
//            } else {
//                CookieManager.removeDependantCookieValue(req, resp, forgotCookieName);
//                resp.sendRedirect(UrlManager.getContextUri(req, "/home"));
//                return;
//            }
//            if ("GET".equals(req.getMethod())) {
//                logger.debug("GET");
//                if (login == null) {
//                    req.getSession().setAttribute("message", "message.forgot.error-confirm");
//                    resp.sendRedirect(UrlManager.getContextUri(req, "/home"));
//                    return;
//                }
//                form.setActionParameterString(UrlManager.getRequestParameterString(req));
//            } else if ("POST".equals(req.getMethod())) {
//                logger.debug("POST");
//                String[] parameterArray = {
//                        "email=".concat(req.getParameter("email.regex")),
//                        "code=".concat(req.getParameter("code"))
//                };
//                form.setActionParameterString(UrlManager.getRequestParameterString(parameterArray));
//                if (ValidatorFactory.validate(req, form)) {
//                    logger.debug("FORM VALID");
//                    if (form.getItem(0).getValue().equals(form.getItem(1).getValue())) {
//                        logger.debug("PASSWORD AND CONFIRM EQUAL");
//                        login.setPassword(form.getItem(0).getValue());
//                        if (loginService.updatePassword(login) != 1) {
//                            logger.debug("UPDATE ERROR");
//                            String[] validationMessageArray = {"profile.reset-password.submit.error-reset-password"};
//                            form.getItem(2).setValidationMessageArray(validationMessageArray);
//                        } else {
//                            logger.debug("UPDATE SUCCESS");
//                            loginService.removeForgotCode(login.getEmail());
//                            CookieManager.removeDependantCookieValue(req, resp, forgotCookieName);
//                            resp.sendRedirect(UrlManager.getContextUri(req, "/profile/login"));
//                            return;
//                        }
//                    } else {
//                        logger.debug("PASSWORD AND CONFIRM NOT EQUAL");
//                        String[] validationMessageArray = {"profile.reset-password.confirm.error-not-equal"};
//                        form.getItem(1).setValidationMessageArray(validationMessageArray);
//                    }
//                }
//            }
//            req.getRequestDispatcher("/WEB-INF/jsp/profile/forgot-password.jsp").forward(req, resp);
//        } catch (ConnectionException | DaoException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.forgot-password.login-service", e.getCause());
//        } catch (ServletException | IOException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.forgot-password.forward", e.getCause());
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.forgot-password.update-login", e.getCause());
//        } catch (ComponentException e) {
//            e.printStackTrace();
//        }
    }
}