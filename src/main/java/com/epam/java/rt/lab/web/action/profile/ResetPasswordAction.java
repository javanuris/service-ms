package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import com.epam.java.rt.lab.web.action.WebAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * service-ms
 */
@WebAction
public class ResetPasswordAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetLoginAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
//        try (LoginService loginService = new LoginService()) {
//            Form form = FormFactory.getInstance().create("profile-reset-password");
////            switch (Form.getStatus("profile.reset-password", UrlManager.getContextPathInfo(req), 100)) {
////                case 1:
////                    form = Form.create("profile.reset-password");
////                    break;
////                case 0:
////                    form = Form.set("profile.reset-password",
////                            new Form.Item("profile.reset-password.password.label", "password", "profile.reset-password.password.label",
////                                    ValidatorFactory.Validator.getRegex(ValidatorFactory.getRegex("password.regex"), "validation.password")),
////                            new Form.Item("profile.reset-password.confirm.label", "password", "profile.reset-password.confirm.label",
////                                    ValidatorFactory.Validator.getRegex(ValidatorFactory.getRegex("password.regex"), "validation.password")),
////                            new Form.Item("profile.reset-password.current.label", "password", "profile.reset-password.current.label",
////                                    ValidatorFactory.Validator.getRegex(ValidatorFactory.getRegex("password.regex"), "validation.password")),
////                            new Form.Item("profile.reset-password.submit.label", "submit", "", null),
////                            new Form.Item("profile.reset-password.view-profile.label", "button",
////                                    UrlManager.getContextUri(req, "/profile/view"), null)
////                    );
////                    break;
////                case -1:
////                    throw new ActionException("exception.action.reset-password.form-status");
////            }
//            req.setAttribute("resetPasswordForm", form);
//            if ("GET".equals(req.getMethod())) {
//                logger.debug("GET");
//                //
//            } else if ("POST".equals(req.getMethod())) {
//                logger.debug("POST");
//                if (ValidatorFactory.validate(req, form)) {
//                    logger.debug("FORM VALID");
//                    if (form.getItem(0).getValue().equals(form.getItem(1).getValue())) {
//                        logger.debug("PASSWORD AND CONFIRM EQUAL");
//                        UserService userService = new UserService();
//                        User user = userService.getUser((Long) req.getSession().getAttribute("userId"));
//                        if (user.getLogin().getPassword().equals(form.getItem(2).getValue())) {
//                            logger.debug("GRANTED");
//                            user.getLogin().setPassword(form.getItem(0).getValue());
//                            if (loginService.updatePassword(user.getLogin()) != 1) {
//                                logger.debug("UPDATE ERROR");
//                                String[] validationMessageArray = {"profile.reset-password.submit.error-reset-password"};
//                                form.getItem(3).setValidationMessageArray(validationMessageArray);
//                            } else {
//                                logger.debug("UPDATE SUCCESS");
//                                resp.sendRedirect(UrlManager.getContextUri(req, "/profile/view"));
//                                return;
//                            }
//                        } else {
//                            logger.debug("DENIED");
//                            String[] validationMessageArray = {"profile.reset-password.current.error-incorrect"};
//                            form.getItem(2).setValidationMessageArray(validationMessageArray);
//                        }
//                    } else {
//                        logger.debug("PASSWORD AND CONFIRM NOT EQUAL");
//                        String[] validationMessageArray = {"profile.reset-password.confirm.error-not-equal"};
//                        form.getItem(1).setValidationMessageArray(validationMessageArray);
//                    }
//                }
//            }
//            req.getRequestDispatcher("/WEB-INF/jsp/profile/reset-password.jsp").forward(req, resp);
//        } catch (ConnectionException | DaoException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.reset-password.login-service", e.getCause());
//        } catch (ServletException | IOException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.reset-password.forward", e.getCause());
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.reset-password.update-login", e.getCause());
//        } catch (ComponentException e) {
//            e.printStackTrace();
//        }
    }
}