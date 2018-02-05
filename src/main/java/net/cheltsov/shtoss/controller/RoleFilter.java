package net.cheltsov.shtoss.controller;

import net.cheltsov.shtoss.command.Command;
import net.cheltsov.shtoss.command.CommandType;
import net.cheltsov.shtoss.command.GuestCommand;
import net.cheltsov.shtoss.command.RedirectCommand;
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

/**
 * This filter implementation is responsible fot authentication
 */
public class RoleFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ATTR_COMMAND_TYPE = "commandType";
    private static final String ATTR_USER = "user";
    private static final String ATTR_REDIRECT = "redirect";
    private static final String PARAM_NEXT_PAGE = "nextPage";
    private static final String PAGE_LOGIN = "jsp.login";
    private static final String PAGE_REGISTER = "jsp.register";
    private static final String PAGE_GUEST = "jsp.guest";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        User user = (User) request.getSession().getAttribute(ATTR_USER);
        Command command = ((CommandType) request.getAttribute(ATTR_COMMAND_TYPE)).getCommand();
        String nextPage = request.getParameter(PARAM_NEXT_PAGE);
        boolean isAllowedRedirect = command instanceof RedirectCommand
                && (PAGE_LOGIN.equals(nextPage) || PAGE_REGISTER.equals(nextPage) || PAGE_GUEST.equals(nextPage));
        if (user == null && !(command instanceof GuestCommand || isAllowedRedirect)) {
            redirect(request, response, PATH_JSP.getString("jsp.index"));
            return;
        }
        if ((user != null && user.getRole().ordinal() < User.Role.USER.ordinal()) ||
                (user != null && (user.getRole().ordinal() < User.Role.ADMIN.ordinal() && command instanceof AdminCommand))) {
            LOGGER.log(Level.WARN, "Trying to get a not allowed access");
            redirect(request, response, PATH_JSP.getString("jsp.guest"));
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String page) throws IOException {
        request.setAttribute(ATTR_REDIRECT, true);
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + page);
    }
}
