package net.cheltsov.shtoss.command.admin;

import net.cheltsov.shtoss.command.Command;
import net.cheltsov.shtoss.command.CommandType;
import net.cheltsov.shtoss.entity.Conversation;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.ServiceException;
import net.cheltsov.shtoss.resource.BundleManager;
import net.cheltsov.shtoss.service.ConversationService;
import net.cheltsov.shtoss.service.UserService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.synth.ColorType;
import java.util.Optional;
import java.util.ResourceBundle;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

public class AdminCreateTopicCommand implements Command, AdminCommand {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ATTR_USER = "user";
    private static final String PARAM_TOPIC = "topic";
    private static final String PARAM_TEXT = "text";
    private static final String PARAM_USER_LOGIN = "userLogin";
    private static final String ATTR_ERROR = "error";
    @Override
    public String execute(HttpServletRequest request) {
        synchronized (request.getSession()) {
            if (Command.isRepeat(request)) {
                return new AdminInboxCommand().execute(request);
            }

            User admin = (User) request.getSession().getAttribute(ATTR_USER);
            String topic = request.getParameter(PARAM_TOPIC);
            String text = request.getParameter(PARAM_TEXT);
            String userLogin = request.getParameter(PARAM_USER_LOGIN);
            ResourceBundle rb = getCurrentBundle(request);
            try {
                if (!UserService.isLoginRegistered(userLogin)) {
                    request.setAttribute(PARAM_TOPIC, topic);
                    request.setAttribute(PARAM_TEXT, text);
                    request.setAttribute(ATTR_ERROR, rb.getString("mess.error.wrong-login"));
                    return PATH_JSP.getString("jsp.create-topic");
                }
            } catch (ServiceException e) {
                LOGGER.log(Level.ERROR, e);
                request.setAttribute(ATTR_ERROR, rb.getString("mess.error.something-wrong"));
                return new AdminInboxCommand().execute(request);
            }
            Optional<Conversation> shredingerConversation = ConversationService.createTopicByAdmin(topic, text, admin, userLogin);
            if (shredingerConversation.isPresent()) {
                request.getSession().setAttribute(ATTR_COMMAND_TYPE, CommandType.ADMIN_CREATE_TOPIC);
            } else {
                request.setAttribute(ATTR_ERROR, rb.getString("mess.error.something-wrong"));
            }
        }
        return new AdminInboxCommand().execute(request);
    }
}
