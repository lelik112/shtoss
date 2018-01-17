package net.cheltsov.shtoss.command.admin;

import net.cheltsov.shtoss.command.Command;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.ServiceException;
import net.cheltsov.shtoss.resource.BundleManager;
import net.cheltsov.shtoss.service.ConversationService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ResourceBundle;

public class AdminInboxCommand implements Command, AdminCommand {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ATTR_USER = "user";
    private static final String ATTR_CONVERSATIONS = "conversations";
    private static final String ATTR_ERROR = "error";
    @Override
    public String execute(HttpServletRequest request) {
        // TODO: 29.12.2017 Поверка администратора
        User user = (User) request.getSession().getAttribute(ATTR_USER);
        try {
            request.setAttribute(ATTR_CONVERSATIONS, ConversationService.findConversations());
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
            ResourceBundle rb = getCurrentBundle(request);
            request.setAttribute(ATTR_ERROR, rb.getString("mess.error.something-wrong"));
        }
        return BundleManager.PATH_JSP.getString("jsp.inbox");
    }
}
