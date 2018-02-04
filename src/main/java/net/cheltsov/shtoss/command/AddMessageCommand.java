package net.cheltsov.shtoss.command;

import net.cheltsov.shtoss.entity.Conversation;
import net.cheltsov.shtoss.entity.Message;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.service.ConversationService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

public class AddMessageCommand implements Command {
    private static final String ATTR_USER = "user";
    private static final String PARAM_TEXT = "text";
    private static final String ATTR_ERROR = "error";
    private static final String ATTR_CONVERSATION = "conversation";
    private static final String ATTR_MESSAGES = "messages";
    private static final String ATTR_PARAMETER_MAP = "parameterMap";


    @Override
    public String execute(HttpServletRequest request) {
        synchronized (request.getSession()) {
            String text = request.getParameter(PARAM_TEXT);
            if (isRepeat(request)) {
                String previousText = ((Map<String, String[]>) request.getSession().getAttribute(ATTR_PARAMETER_MAP)).get(PARAM_TEXT)[0];
                if (Objects.equals(previousText, text)) {
                    return PATH_JSP.getString("jsp.messages");
                }
            }
            User user = (User) request.getSession().getAttribute(ATTR_USER);
            Conversation conversation = (Conversation) request.getSession().getAttribute(ATTR_CONVERSATION);
            List<Message> messages = (List<Message>) request.getSession().getAttribute(ATTR_MESSAGES);
            Optional<Message> shredingerMessage = ConversationService.addMessage(user, conversation, text);
            shredingerMessage.ifPresent(messages::add);
            if (!shredingerMessage.isPresent()) {
                ResourceBundle rb = getCurrentBundle(request);
                request.setAttribute(ATTR_ERROR, rb.getString("mess.error.something-wrong"));
            }
        }
        return PATH_JSP.getString("jsp.messages");
    }
}
