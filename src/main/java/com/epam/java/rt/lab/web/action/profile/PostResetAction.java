package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * category-ms
 */
public class PostResetAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
//        try (LoginService loginService = new LoginService()) {
//            Form form = FormFactory.getInstance().create("reset-password");
//            if (FormValidator.validate(req, form)) {
//                logger.debug("FORM VALID");
//                if (!form.getItem(0).getValue().equals(form.getItem(1).getValue())) {
//                    form.getItem(1).addValidationMessage("message.profile.repeat-not-equal");
//                } else {
//                    User user = (User) req.getSession().getAttribute("user");
//                    if (user != null && user.getLogin().getAttemptLeft() > 0 && user.getLogin().getStatus() >= 0) {
//                        if (!HashGenerator.hashPassword(user.getLogin().getSalt(), form.getItem(2).getValue())
//                                .equals(user.getLogin().getPassword())) {
//                            form.getItem(2).addValidationMessage("message.profile.password-incorrect");
//                        } else {
//                            user.getLogin().setSalt(UUID.randomUUID().toString());
//                            user.getLogin().setPassword(HashGenerator.hashPassword(user.getLogin().getSalt(), form.getItem(0).getValue()));
//                            user.getLogin().setAttemptLeft(Integer.valueOf(PropertyManager.getProperty("login.attempt.max")));
//                            loginService.updateLogin(user.getLogin());
//                            resp.sendRedirect(UrlManager.getContextUri(req, "/profile/view"));
//                            return;
//                        }
//                    }
//                }
//            }
//            req.setAttribute("resetPasswordForm", form);
//            req.getRequestDispatcher("/WEB-INF/jsp/profile/reset-password.jsp").forward(req, resp);
//        } catch (FormException e) {
//            throw new ActionException("exception.action.profile.reset-password.form", e.getCause());
//        } catch (ServletException | IOException e) {
//            throw new ActionException("exception.action.profile.reset-password.jsp", e.getCause());
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.profile.reset-password.hash", e.getCause());
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }
    }
}