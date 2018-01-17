package net.cheltsov.shtoss.command;

import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.ResourceBundle;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

public class ChangeNameCommand implements Command {
    private static final String PARAM_FIRST_NAME = "firstName";
    private static final String PARAM_LAST_NAME = "lastName";
    private static final String ATTR_USER = "user";
    private static final String ATTR_SUCCESS_NAME_CHANGES = "successChangedNames";
    private static final String ATTR_ERROR_NAME_CHANGES = "errorChangedNames";

    @Override
    public String execute(HttpServletRequest request) {
        synchronized (request.getSession()) {
            User user = (User) request.getSession().getAttribute(ATTR_USER);
            String firstName = request.getParameter(PARAM_FIRST_NAME);
            String lastName = request.getParameter(PARAM_LAST_NAME);
            if (Objects.equals(firstName, user.getFirstName()) && Objects.equals(lastName, user.getLastName())) {
                return PATH_JSP.getString("jsp.profile");
            }

            ResourceBundle rb = getCurrentBundle(request);
            if (UserService.updateNames(firstName, lastName, user)) {
                request.setAttribute(ATTR_SUCCESS_NAME_CHANGES, rb.getString("mess.success.changed-name"));
            } else {
                request.setAttribute(ATTR_ERROR_NAME_CHANGES, rb.getString("mess.error.changed-name"));
            }
        }
        return PATH_JSP.getString("jsp.profile");
    }
}
