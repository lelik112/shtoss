package net.cheltsov.shtoss.command;

import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.ServiceException;
import net.cheltsov.shtoss.service.UserService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import static net.cheltsov.shtoss.resource.BundleManager.*;

public class LoginCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String PARAM_ATTR_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final String ATTR_USER = "user";
    private static final String ATTR_ERROR_LOGIN_PASS = "errorLoginPassMessage";

    @Override
    public String execute(HttpServletRequest request) {
        String loginOrEmail = request.getParameter(PARAM_ATTR_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);

        try {
            Optional<User> shredingerUser = UserService.authorize(loginOrEmail, password);
            if (shredingerUser.isPresent()) {
                request.getSession().setAttribute(ATTR_USER, shredingerUser.get());
                LOGGER.log(Level.INFO, "User " + shredingerUser.get().getLogin() + " has just logged in");
                return PATH_JSP.getString("jsp.main");
            } else {
                request.setAttribute(ATTR_ERROR_LOGIN_PASS, getCurrentBundle(request).getString("mess.error.login.pass"));
            }
        } catch (ServiceException e) {
            LOGGER.catching(e);
            request.setAttribute(ATTR_ERROR_LOGIN_PASS, getCurrentBundle(request).getString("mess.error.something-wrong"));
        }
        return PATH_JSP.getBundle().getString("jsp.login");
    }
}
