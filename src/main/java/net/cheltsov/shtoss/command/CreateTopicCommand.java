package net.cheltsov.shtoss.command;

import net.cheltsov.shtoss.entity.Conversation;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.resource.BundleManager;
import net.cheltsov.shtoss.service.ConversationService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CreateTopicCommand implements Command {
    private static final String ATTR_USER = "user";
    private static final String PARAM_TOPIC = "topic";
    private static final String PARAM_TEXT = "text";
    private static final String ATTR_CONVERSATIONS = "conversations";
    private static final String ATTR_ERROR = "error";
    @Override
    public String execute(HttpServletRequest request) {
        synchronized (request.getSession()) {
            if (isRepeat(request)) {
                return BundleManager.PATH_JSP.getString("jsp.inbox");
            }
            User user = (User) request.getSession().getAttribute(ATTR_USER);
            String topic = request.getParameter(PARAM_TOPIC);
            String text = request.getParameter(PARAM_TEXT);
            List<Conversation> conversations = (List<Conversation>) request.getSession().getAttribute(ATTR_CONVERSATIONS);
            if (conversations == null) {
                conversations = new ArrayList<>();
                request.getSession().setAttribute(ATTR_CONVERSATIONS, conversations);
            }
            Optional<Conversation> shredingerConversation = ConversationService.createTopicByUser(topic, text, user);
            shredingerConversation.ifPresent(conversations::add);
            if (shredingerConversation.isPresent()) {
            } else {
                ResourceBundle rb = getCurrentBundle(request);
                request.setAttribute(ATTR_ERROR, rb.getString("mess.error.something-wrong"));
            }
        }
        return BundleManager.PATH_JSP.getString("jsp.inbox");
    }
}
