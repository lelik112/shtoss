package net.cheltsov.shtoss.command;

import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.ServiceException;
import net.cheltsov.shtoss.service.UserService;
import net.cheltsov.shtoss.validator.ValidationResult;
import net.cheltsov.shtoss.validator.Validator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ResourceBundle;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

public class RegisterCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String PARAM_ATTR_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_ATTR_EMAIL = "email";
    private static final String PARAM_ATTR_FIRST_NAME = "firstName";
    private static final String PARAM_ATTR_LAST_NAME = "lastName";
    private static final String ATTR_USER = "user";
    private static final String ATTR_ERROR_LOGIN_PASS = "errorLoginPassMessage";


    @Override
    public String execute(HttpServletRequest request) {

        synchronized (request.getSession()) {
            if (isRepeat(request)) {
                return PATH_JSP.getString("jsp.main");
            }

            String login = request.getParameter(PARAM_ATTR_LOGIN);
            String password = request.getParameter(PARAM_PASSWORD);
            String email = request.getParameter(PARAM_ATTR_EMAIL);
            String firstName = request.getParameter(PARAM_ATTR_FIRST_NAME);
            String lastName = request.getParameter(PARAM_ATTR_LAST_NAME);

            if (login == null || password == null || email == null) {
                return PATH_JSP.getString("jsp.register");
            }

            {
                if (!Validator.validateRegisterData(login, password, email)) {
                    LOGGER.log(Level.ERROR, "Registered data are not valid. Registered page is needed to be checked");
                    request.setAttribute(ATTR_ERROR_LOGIN_PASS, getCurrentBundle(request).getString("mess.error.something-wrong"));
                    return PATH_JSP.getString("jsp.register");
                }
            }

            User user = new User();
            ValidationResult result;
            try {
                result = UserService.register(user, login, password, email, firstName, lastName);
            } catch (ServiceException e) {
                LOGGER.catching(e);
                request.setAttribute(ATTR_ERROR_LOGIN_PASS, getCurrentBundle(request).getString("mess.error.something-wrong"));
                return PATH_JSP.getString("jsp.register");
            }

            ResourceBundle rb = getCurrentBundle(request);
            String message = null;
            switch (result) {
                case ALL_RIGHT:
                    request.getSession().setAttribute(ATTR_USER, user);
                    LOGGER.log(Level.INFO, "New user was registered. Login is: " + user.getLogin());
                    return PATH_JSP.getString("jsp.main");
                case LOGIN_NOT_UNIQUE:
                    message = rb.getString("mess.error.login");
                    request.setAttribute(PARAM_ATTR_EMAIL, email);
                    break;
                case EMAIL_NOT_UNIQUE:
                    message = rb.getString("mess.error.email");
                    request.setAttribute(PARAM_ATTR_LOGIN, login);
                    break;
            }
            if (firstName != null) request.setAttribute(PARAM_ATTR_FIRST_NAME, firstName);
            if (lastName != null) request.setAttribute(PARAM_ATTR_LAST_NAME, lastName);
            request.setAttribute(ATTR_ERROR_LOGIN_PASS, message);
        }
        return PATH_JSP.getString("jsp.register");
    }
}
