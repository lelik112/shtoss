package net.cheltsov.shtoss.command;


import net.cheltsov.shtoss.controller.PreviousRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.ResourceBundle;

public interface Command {
    String ATTR_MESSAGE = "message";
    String ATTR_COMMAND_TYPE = "commandType";
    String ATTR_PARAMETER_MAP = "parameterMap";
    String ATTR_REPEATING = "repeating";

    default ResourceBundle getCurrentBundle(HttpServletRequest request) {
        ResourceBundle sessionBundle = (ResourceBundle) request.getSession().getAttribute(ATTR_MESSAGE);
        ResourceBundle appBundle = (ResourceBundle) request.getServletContext().getAttribute(ATTR_MESSAGE);
        return sessionBundle == null? appBundle: sessionBundle;
    }

    default boolean isRepeat(HttpServletRequest request) {
        CommandType previousType = (CommandType) request.getSession().getAttribute(ATTR_COMMAND_TYPE);
        CommandType currentType = (CommandType) request.getAttribute(ATTR_COMMAND_TYPE);
        return previousType == currentType;
    }

    default String repeatPreviousCommand(HttpServletRequest request) {
        CommandType previousType = (CommandType) request.getSession().getAttribute(ATTR_COMMAND_TYPE);
        Map<String, String[]> previousParameterMap = (Map<String, String[]>) request.getSession().getAttribute(ATTR_PARAMETER_MAP);
        request.setAttribute(ATTR_REPEATING, "true");
        PreviousRequestWrapper requestWrapper = new PreviousRequestWrapper(request, previousParameterMap);
        return previousType.getCommand().execute(requestWrapper);
    }

    String execute(HttpServletRequest request);
}
