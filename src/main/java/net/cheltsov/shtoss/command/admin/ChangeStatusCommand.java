package net.cheltsov.shtoss.command.admin;

import net.cheltsov.shtoss.command.Command;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.ServiceException;
import net.cheltsov.shtoss.resource.BundleManager;
import net.cheltsov.shtoss.service.UserService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ResourceBundle;

public class ChangeStatusCommand implements Command, AdminCommand {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ATTR_ERROR = "error";
    private static final String ATTR_USER_TO_CHANGE = "changingUser";
    private static final String PARAM_ROLE = "role";
    private static final String ATTR_SUCCESS = "success";
    @Override
    public String execute(HttpServletRequest request) {
        synchronized (request.getSession()) {
            User userToChange = (User) request.getSession().getAttribute(ATTR_USER_TO_CHANGE);
            User.Role newRole = User.Role.valueOf(request.getParameter(PARAM_ROLE).toUpperCase().trim());
            if (userToChange.getRole().equals(newRole)) {
                return BundleManager.PATH_JSP.getString("jsp.change-status");
            }
            ResourceBundle rb = getCurrentBundle(request);
            try {
                if (UserService.changeRole(userToChange, newRole)) {
                    request.setAttribute(ATTR_SUCCESS, rb.getString("mess.success.changed-role"));
                } else {
                    request.setAttribute(ATTR_ERROR, rb.getString("mess.error.last-admin"));
                }
            } catch (ServiceException e) {
                e.printStackTrace();
                LOGGER.catching(e);
                request.setAttribute(ATTR_ERROR, rb.getString("mess.error.something-wrong"));
            }
        }
        return BundleManager.PATH_JSP.getString("jsp.change-status");
    }
}
