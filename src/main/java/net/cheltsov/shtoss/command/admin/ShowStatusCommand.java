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

import java.util.Optional;
import java.util.ResourceBundle;

public class ShowStatusCommand implements Command, AdminCommand {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String PARAM_USER_ID = "user-id";
    private static final String ATTR_ERROR = "error";
    private static final String ATTR_USER_TO_CHANGE = "changingUser";

    @Override
    public String execute(HttpServletRequest request) {
        int userID = Integer.parseInt(request.getParameter(PARAM_USER_ID));
        ResourceBundle rb = getCurrentBundle(request);
        try {
            Optional<User> userToChange = UserService.findUserById(userID);
            if (userToChange.isPresent()) {
                request.getSession().setAttribute(ATTR_USER_TO_CHANGE, userToChange.get());
                return BundleManager.PATH_JSP.getString("jsp.change-status");
            } else {
                request.setAttribute(ATTR_ERROR, rb.getString("mess.error.wrong-id"));
            }
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
            request.setAttribute(ATTR_ERROR, rb.getString("mess.error.something-wrong"));
        }
        return BundleManager.PATH_JSP.getString("jsp.admin");
    }
}
