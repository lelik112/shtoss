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
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ShowUsersCommand implements Command, AdminCommand {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ORDER_BY_ID = "user_id";
    private static final String ORDER_BY_LOGIN = "login";
    private static final String ORDER_BY_EMAIL = "email";
    private static final String ORDER_BY_BALANCE = "balance";
    private static final String ORDER_BY_ROLE = "role";
    private static final String ORDER_BY_FIRST_NAME = "fname";
    private static final String ORDER_BY_LAST_NAME = "lname";
    private static final String PARAM_ORDER = "order";
    private static final String ATTR_USERS = "users";
    private static final String ATTR_ERROR = "error";
    private static final String ATTR_PREVIOUS_LIST = "previousList";

    @Override
    public String execute(HttpServletRequest request) {
        String orderBy = request.getParameter(PARAM_ORDER);
        if (orderBy == null) orderBy = ORDER_BY_ID;
        List<User> previousList = (List<User>) request.getSession().getAttribute(ATTR_PREVIOUS_LIST);
        Comparator<User> defaultComparator = Comparator.comparing(User::getLogin);
        Comparator<User> comparator;
        switch (orderBy) {
            case ORDER_BY_ID:
                comparator = Comparator.comparing(User::getID);
                break;
            case ORDER_BY_LOGIN:
                comparator = defaultComparator;
                break;
            case ORDER_BY_EMAIL:
                comparator = Comparator.comparing(User::getEmail);
                break;
            case ORDER_BY_BALANCE:
                comparator = Comparator.comparing(User::getBalance).thenComparing(defaultComparator);
                break;
            case ORDER_BY_ROLE:
                comparator = Comparator.comparing(User::getRole).thenComparing(defaultComparator);
                break;
            case ORDER_BY_FIRST_NAME:
                comparator = Comparator.comparing(User::getFirstName).thenComparing(defaultComparator);
                break;
            case ORDER_BY_LAST_NAME:
                comparator = Comparator.comparing(User::getLastName).thenComparing(defaultComparator);
                break;
            default:
                comparator = defaultComparator;
        }

        try {
            List<User> users = UserService.findAllUsers();
            users.sort(comparator); // TODO: 04.01.2018 Не плохо бы знать предыдущую страницу/комманду
            if (previousList != null && previousList.equals(users)) {
                users.sort(comparator.reversed());
            }
            request.setAttribute(ATTR_USERS, users);
            request.getSession().setAttribute(ATTR_PREVIOUS_LIST, users);
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
            ResourceBundle rb = getCurrentBundle(request);
            request.setAttribute(ATTR_ERROR, rb.getString("mess.error.something-wrong"));
        }
        return BundleManager.PATH_JSP.getString("jsp.admin");
    }
}








