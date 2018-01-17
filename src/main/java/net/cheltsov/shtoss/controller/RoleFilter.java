package net.cheltsov.shtoss.controller;

import net.cheltsov.shtoss.command.*;
import net.cheltsov.shtoss.command.admin.AdminCommand;
import net.cheltsov.shtoss.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

public class RoleFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String PARAM_OMMAND = "command";
    private static final String ATTR_COMMAND_TYPE = "commandType";
    private static final String ATTR_USER = "user";
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        User user = (User)request.getSession().getAttribute(ATTR_USER);
        CommandType commandType = ActionFactory.defineCommand(request.getParameter(PARAM_OMMAND));
        Command command = commandType.getCommand();
        if (user == null && !(command instanceof LoginCommand || command instanceof RegisterCommand)) {
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + PATH_JSP.getString("jsp.index")));
            return;
        }
        if ((user != null && user.getRole().ordinal() < User.Role.USER.ordinal()) ||
                (user != null && (user.getRole().ordinal() < User.Role.ADMIN.ordinal() && command instanceof AdminCommand))) {
            LOGGER.log(Level.WARN, "Trying to get a not allowed access");
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + PATH_JSP.getString("jsp.guest"));
            return;
        }
        request.setAttribute(ATTR_COMMAND_TYPE, commandType);
        filterChain.doFilter(request, response);
    }



    @Override
    public void destroy() {

    }
}
