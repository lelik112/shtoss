package net.cheltsov.shtoss.command.admin;

import net.cheltsov.shtoss.command.Command;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.ServiceException;
import net.cheltsov.shtoss.resource.BundleManager;
import net.cheltsov.shtoss.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ShowUsersCommand implements Command, AdminCommand {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String DEFAULT_ORDER_BY = "userId";
    private static final String ATTR_PARAM_ORDER = "order";
    private static final String ATTR_USERS = "users";
    private static final String ATTR_ERROR = "error";
    private static final String ATTR_REVERSE = "reverse";

    @Override
    public String execute(HttpServletRequest request) {
        String requestOrderBy = request.getParameter(ATTR_PARAM_ORDER);
        String sessionOrderBy = (String) request.getSession().getAttribute(ATTR_PARAM_ORDER);
        String orderBy = requestOrderBy != null ? requestOrderBy : sessionOrderBy != null ? sessionOrderBy : DEFAULT_ORDER_BY;
        Boolean reverse = (Boolean) Optional.ofNullable(request.getSession().getAttribute(ATTR_REVERSE)).orElse(false);

        try {
            List<User> users = UserService.findAllUsers(orderBy);
            if (isRepeat(request) && !reverse && orderBy.equals(sessionOrderBy) || !isRepeat(request) && reverse) {
                Collections.reverse(users);
                request.getSession().setAttribute(ATTR_REVERSE, true);
            } else {
                request.getSession().setAttribute(ATTR_REVERSE, false);
            }
            request.setAttribute(ATTR_USERS, users);
            request.getSession().setAttribute(ATTR_PARAM_ORDER, orderBy);
        } catch (ServiceException e) {
            LOGGER.catching(e);
            ResourceBundle rb = getCurrentBundle(request);
            request.setAttribute(ATTR_ERROR, rb.getString("mess.error.something-wrong"));
        }
        return BundleManager.PATH_JSP.getString("jsp.admin");
    }
}








