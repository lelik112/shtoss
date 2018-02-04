package net.cheltsov.shtoss.command;

import net.cheltsov.shtoss.entity.Conversation;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.ServiceException;
import net.cheltsov.shtoss.service.ConversationService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ResourceBundle;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

public class ShowMessagesCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ATTR_USER = "user";
    private static final String PARAM_CONVERSATIONS_ID = "conversationId";
    private static final String ATTR_MESSAGES = "messages";
    private static final String ATTR_ERROR = "error";
    private static final String ATTR_CONVERSATION = "conversation";
    @Override
    public String execute(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(ATTR_USER);
        int conversationId = Integer.parseInt(request.getParameter(PARAM_CONVERSATIONS_ID));

        try {
            Conversation conversation = ConversationService.findConversationById(conversationId, user);
            request.getSession().setAttribute(ATTR_CONVERSATION, conversation);
            request.getSession().setAttribute(ATTR_MESSAGES, ConversationService.findMessages(conversationId));
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e);
            ResourceBundle rb = getCurrentBundle(request);
            request.setAttribute(ATTR_ERROR, rb.getString("mess.error.something-wrong"));
        }
        return PATH_JSP.getString("jsp.messages");
    }
}
