package net.cheltsov.shtoss.command;

import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.ServiceException;
import net.cheltsov.shtoss.resource.BundleManager;
import net.cheltsov.shtoss.service.ConversationService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class InboxCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ATTR_USER = "user";
    private static final String ATTR_CONVERSATIONS = "conversations";
    private static final String ATTR_ERROR = "error";

    @Override
    public String execute(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(ATTR_USER);
        if (request.getSession().getAttribute(ATTR_CONVERSATIONS) == null) {
            try {
                request.getSession().setAttribute(ATTR_CONVERSATIONS, ConversationService.findConversations(user));
            } catch (ServiceException e) {
                LOGGER.catching(e);
                ResourceBundle rb = getCurrentBundle(request);
                request.setAttribute(ATTR_ERROR, rb.getString("mess.error.something-wrong"));
            }
        }
        return BundleManager.PATH_JSP.getString("jsp.inbox");
    }
}
