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

    /**
     * @param request from client site
     * @return URI of the page to display on client side
     */
    String execute(HttpServletRequest request);

    /**
     * @param request from client site
     * @return ResourceBundle of messages which is associated with client or default one
     */
    default ResourceBundle getCurrentBundle(HttpServletRequest request) {
        ResourceBundle sessionBundle = (ResourceBundle) request.getSession().getAttribute(ATTR_MESSAGE);
        ResourceBundle appBundle = (ResourceBundle) request.getServletContext().getAttribute(ATTR_MESSAGE);
        return sessionBundle == null? appBundle: sessionBundle;
    }

    /**
     * @param request from client site
     * @return <tt>true</tt> if current command is the same command which was in previous request
     */
    default boolean isRepeat(HttpServletRequest request) {
        CommandType previousType = (CommandType) request.getSession().getAttribute(ATTR_COMMAND_TYPE);
        CommandType currentType = (CommandType) request.getAttribute(ATTR_COMMAND_TYPE);
        return previousType == currentType;
    }

    /**
     * Allows to repeat any post command
     *
     * @param request from client site
     */
    default void allowRepeating(HttpServletRequest request) {
        request.setAttribute(ATTR_REPEATING, true);
    }

    /**
     * @param request from client site
     * @return URI of the page to display on client side
     */
    default String repeatPreviousCommand(HttpServletRequest request) {
        CommandType previousType = (CommandType) request.getSession().getAttribute(ATTR_COMMAND_TYPE);
        Map<String, String[]> previousParameterMap = (Map<String, String[]>) request.getSession().getAttribute(ATTR_PARAMETER_MAP);
        request.setAttribute(ATTR_REPEATING, true);
        request.setAttribute(ATTR_COMMAND_TYPE, previousType);
        PreviousRequestWrapper requestWrapper = new PreviousRequestWrapper(request, previousParameterMap);
        return previousType.getCommand().execute(requestWrapper);
    }


}
