package net.cheltsov.shtoss.command;

import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.service.UserService;
import net.cheltsov.shtoss.validator.ValidationResult;

import javax.servlet.http.HttpServletRequest;

import java.util.ResourceBundle;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

public class ChangeEmailCommand implements Command {
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_EMAIL = "email";
    private static final String ATTR_USER = "user";
    private static final String ATTR_EMAIL = "email";
    private static final String ATTR_SUCCESS_EMAIL_CHANGE = "successChangedEmail";
    private static final String ATTR_ERROR_EMAIL_CHANGE = "errorChangedEmail";
    private static final String ATTR_ACTIVE_WINDOW = "activeWindow";
    private static final String ATTR_ACTIVE_WINDOW_VALUE = "change-email";


    @Override
    public String execute(HttpServletRequest request) {
        synchronized (request.getSession()) {
            User user = (User) request.getSession().getAttribute(ATTR_USER);
            String email = request.getParameter(PARAM_EMAIL);
            String password = request.getParameter(PARAM_PASSWORD);
            request.setAttribute(ATTR_ACTIVE_WINDOW, ATTR_ACTIVE_WINDOW_VALUE);
            if (user.getEmail().equals(email)) {
                return PATH_JSP.getString("jsp.profile");
            }
            ResourceBundle rb = getCurrentBundle(request);
            ValidationResult result = UserService.changeEmail(user, password, email);
            switch (result) {
                case ALL_RIGHT:
                    request.setAttribute(ATTR_SUCCESS_EMAIL_CHANGE, rb.getString("mess.success.changed-email"));
                    break;
                case PASSWORD_NOT_CORRECT:
                    request.setAttribute(ATTR_ERROR_EMAIL_CHANGE, rb.getString("mess.error.pass"));
                    request.setAttribute(ATTR_EMAIL, email);
                    break;
                case EMAIL_NOT_UNIQUE:
                    request.setAttribute(ATTR_ERROR_EMAIL_CHANGE, rb.getString("mess.error.email"));
                    break;
                case SERVICE_ERROR:
                    request.setAttribute(ATTR_ERROR_EMAIL_CHANGE, rb.getString("mess.error.something-wrong"));
                    break;
            }
        }
        return PATH_JSP.getString("jsp.profile");

    }
}
