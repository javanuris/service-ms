package com.epam.java.rt.lab.web.action.user;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * category-ms
 */
public class PostEditAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
//        try (UserService userService = new UserService();
//             LoginService loginService = new LoginService()) {
//            logger.debug("/WEB-INF/jsp/user/view.jsp");
//            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
//            String id = parameterMap.get("id");
//            if (ValidatorFactory.create("digits").validate(id) != null) {
//                parameterMap.remove("id");
//                resp.sendRedirect(UrlManager.getContextUri(req, "/user/list", parameterMap));
//                return;
//            } else {
//                User user = (User) req.getSession().getAttribute("user");
//                if (user.getId().equals(Long.valueOf(id))) {
//                    resp.sendRedirect(UrlManager.getContextUri(req, "/profile/edit", parameterMap));
//                    return;
//                }
//                user = userService.getUser(Long.valueOf(id));
//                if (user == null) {
//                    resp.sendRedirect(UrlManager.getContextUri(req, "/user/list", parameterMap));
//                } else {
//                    Form form = FormFactory.getInstance().create("edit-user-profile");
//                    form.setActionParameterString(UrlManager.getRequestParameterString(parameterMap));
//                    List<FormControl.SelectValue> valueList = new ArrayList<>();
//                    for (Map.Entry<String, Role> entry : RoleFactory.getInstance().getRoleMap().entrySet())
//                        valueList.add(new FormControl.SelectValue(
//                                entry.getValue().getName(), entry.getValue().getName()));
//                    form.getItem(4).setAvailableValueList(valueList);
//                    valueList = new ArrayList<>();
//                    valueList.add(new FormControl.SelectValue("0", "0"));
//                    valueList.add(new FormControl.SelectValue("-1", "-1"));
//                    form.getItem(6).setAvailableValueList(valueList);
//                    if (FormValidator.validate(req, form)) {
//                        user.setFirstName(form.getItem(0).getValue());
//                        user.setMiddleName(form.getItem(1).getValue());
//                        user.setLastName(form.getItem(2).getValue());
//                        user.setRole(RoleFactory.getInstance().create(form.getItem(4).getValue()));
//                        user.getLogin().setAttemptLeft(Integer.valueOf(form.getItem(5).getValue()));
//                        user.getLogin().setStatus(Integer.valueOf(form.getItem(6).getValue()));
//                        loginService.updateLogin(user.getLogin());
//                        userService.updateUser(user, form.getItem(3).getValue());
//                        resp.sendRedirect(UrlManager.getContextUri(req, "/user/view", parameterMap));
//                        return;
//                    }
//                    req.setAttribute("editForm", form);
//                    req.getRequestDispatcher("/WEB-INF/jsp/user/edit.jsp").forward(req, resp);
//                }
//            }
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.user.edit.user-category.get-user", e.getCause());
//        } catch (ValidatorException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.user.edit.validator.id", e.getCause());
//        } catch (FormException e) {
//            throw new ActionException("exception.action.user.edit.form", e.getCause());
//        } catch (ServletException | IOException e) {
//            throw new ActionException("exception.action.user.edit.jsp", e.getCause());
//        } catch (AccessException e) {
//            throw new ActionException("exception.action.user.edit.role-factory", e.getCause());
//        }
    }
}