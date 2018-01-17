package net.cheltsov.shtoss.command;


import javax.servlet.http.HttpServletRequest;
import java.util.ResourceBundle;

public interface Command {
    String ATTR_MESSAGE = "message";
    String ATTR_COMMAND_TYPE = "commandType";

    default ResourceBundle getCurrentBundle(HttpServletRequest request) {
        ResourceBundle sessionBundle = (ResourceBundle) request.getSession().getAttribute(ATTR_MESSAGE);
        ResourceBundle appBundle = (ResourceBundle) request.getServletContext().getAttribute(ATTR_MESSAGE);
        return sessionBundle == null? appBundle: sessionBundle;
    }

    static boolean isRepeat(HttpServletRequest request) {
        CommandType previousType = (CommandType) request.getSession().getAttribute(ATTR_COMMAND_TYPE);
        CommandType currentType = (CommandType) request.getAttribute(ATTR_COMMAND_TYPE);
        return previousType == currentType;
    }

    String execute(HttpServletRequest request);
}
