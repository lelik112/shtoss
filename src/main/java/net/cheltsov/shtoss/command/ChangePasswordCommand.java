package net.cheltsov.shtoss.command;

import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.service.UserService;
import net.cheltsov.shtoss.validator.ValidationResult;
import net.cheltsov.shtoss.validator.Validator;

import javax.servlet.http.HttpServletRequest;

import java.util.ResourceBundle;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

public class ChangePasswordCommand implements Command {
    private static final String PARAM_OLD_PASSWORD = "oldPassword";
    private static final String PARAM_NEW_PASSWORD = "password";
    private static final String ATTR_USER = "user";
    private static final String ATTR_ACTIVE_WINDOW = "activeWindow";
    private static final String ATTR_ACTIVE_WINDOW_VALUE = "change-password";
    private static final String ATTR_SUCCESS_PASSWORD_CHANGE = "successChangedPassword";
    private static final String ATTR_ERROR_PASSWORD_CHANGE = "errorChangedPassword";

    @Override
    public String execute(HttpServletRequest request) {
        synchronized (request.getSession()) {
            User user = (User) request.getSession().getAttribute(ATTR_USER);
            request.setAttribute(ATTR_ACTIVE_WINDOW, ATTR_ACTIVE_WINDOW_VALUE);
            String oldPassword = request.getParameter(PARAM_OLD_PASSWORD);
            String newPassword = request.getParameter(PARAM_NEW_PASSWORD);
            ResourceBundle rb = getCurrentBundle(request);
            if (oldPassword.equals(newPassword) && !Validator.validatePassword(newPassword)) {
                request.setAttribute(ATTR_ERROR_PASSWORD_CHANGE, getCurrentBundle(request).getString("mess.error.something-wrong"));
                return PATH_JSP.getString("jsp.profile");
            }
            ValidationResult result = UserService.changePassword(user, oldPassword, newPassword);
            switch (result) {
                case ALL_RIGHT:
                    request.setAttribute(ATTR_SUCCESS_PASSWORD_CHANGE, rb.getString("mess.success.changed-password"));
                    break;
                case PASSWORD_NOT_CORRECT:
                    request.setAttribute(ATTR_ERROR_PASSWORD_CHANGE, rb.getString("mess.error.pass"));
                    break;
                case SERVICE_ERROR:
                    request.setAttribute(ATTR_ERROR_PASSWORD_CHANGE, rb.getString("mess.error.something-wrong"));
                    break;
            }
        }

        return PATH_JSP.getString("jsp.profile");
    }
}
