package net.cheltsov.shtoss.controller;

import net.cheltsov.shtoss.command.Command;
import net.cheltsov.shtoss.command.CommandType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RepeatFilter implements Filter {
    private static final String ATTR_COMMAND_TYPE = "commandType";
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (!Command.isRepeat(request)) {
            request.getSession().setAttribute(ATTR_COMMAND_TYPE, CommandType.UNDEFINED);
        }
        filterChain.doFilter(request, servletResponse);
    }
}
