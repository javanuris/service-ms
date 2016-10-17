package com.epam.java.rt.lab.web.action.user;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.action.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetViewAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
//        try (UserService userService = new UserService()) {
//            logger.debug("/WEB-INF/jsp/user/view.jsp");
//            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
//            String id = parameterMap.remove("id");
//            if (ValidatorFactory.create("digits").validate(id) != null) {
//                resp.sendRedirect(UrlManager.getContextUri(req, "/user/list", parameterMap));
//            } else {
//                User user = (User) req.getSession().getAttribute("user");
//                if (user.getId() == Long.valueOf(id)) {
//                    resp.sendRedirect(UrlManager.getContextUri(req, "/profile/view", parameterMap));
//                    return;
//                }
//                user = userService.getUser(Long.valueOf(id));
//                if (user == null) {
//                    resp.sendRedirect(UrlManager.getContextUri(req, "/user/list", parameterMap));
//                } else {
//                    View view = ViewFactory.getInstance().create("view-user-profile");
//                    view.getControl(0).setValue(user.getFirstName());
//                    view.getControl(1).setValue(user.getMiddleName());
//                    view.getControl(2).setValue(user.getLastName());
//                    view.getControl(3).setValue(
//                            UrlManager.getContextRef(req, "/file/download/avatar", "id", user.getAvatarId())
//                    );
//                    view.getControl(4).setValue(user.getLogin().getEmail());
//                    view.getControl(5).setValue(user.getRole().getName());
//                    view.getControl(6).setValue(String.valueOf(user.getLogin().getAttemptLeft()));
//                    view.getControl(7).setValue(String.valueOf(user.getLogin().getStatus()));
//                    view.getControl(8).setAction(UrlManager.getContextUri(req, "/user/edit",
//                            UrlManager.getRequestParameterString(parameterMap), "id=".concat(id)));
//                    view.getControl(9).setAction(UrlManager.getContextUri(req, "/user/list", parameterMap));
//                    req.setAttribute("viewProfile", view);
//                    req.getRequestDispatcher("/WEB-INF/jsp/user/view.jsp").forward(req, resp);
//                }
//            }
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.user.view.user-category.get-user", e.getCause());
//        } catch (ValidatorException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.user.view.validator.id", e.getCause());
//        } catch (ViewException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.user.view.view-factory.get-instance", e.getCause());
//        } catch (ServletException | IOException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.user.view.request", e.getCause());
//        }
    }

}
