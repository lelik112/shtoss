package net.cheltsov.shtoss.controller;

import net.cheltsov.shtoss.command.ActionFactory;
import net.cheltsov.shtoss.command.CommandType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public class RepeatFilter implements Filter {
    private static final String PARAM_COMMAND = "command";
    private static final String ATTR_COMMAND_TYPE = "commandType";
    private static final String ATTR_PARAMETER_MAP = "parameterMap";
    private static final String ATTR_REPEATING = "repeating";


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        CommandType commandType = ActionFactory.defineCommand(request.getParameter(PARAM_COMMAND));
        request.setAttribute(ATTR_COMMAND_TYPE, commandType);

        filterChain.doFilter(request, servletResponse);

        if (Boolean.parseBoolean((String) request.getAttribute(ATTR_REPEATING))) {
            return;
        }
        request.getSession().setAttribute(ATTR_COMMAND_TYPE, request.getAttribute(ATTR_COMMAND_TYPE));
        Map<String, String[]> previousParameterMap = (Map<String, String[]>) request.getSession().getAttribute(ATTR_PARAMETER_MAP);
        previousParameterMap.clear();
        previousParameterMap.putAll(request.getParameterMap());
    }
}
