package net.cheltsov.shtoss.controller;

import net.cheltsov.shtoss.command.UndefinedCommand;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

/**
 * This filter implementation prevents access to jsp pages directly
 */
public class PageFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ATTR_WRONG_ACTION = "wrongAction";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        request.getSession().invalidate();
        ResourceBundle rb = new UndefinedCommand().getCurrentBundle(request);
        request.getSession().setAttribute(ATTR_WRONG_ACTION, rb.getString("mess.error.wrong-action"));
        LOGGER.log(Level.WARN, "Trying to get a not controlled access");
        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + PATH_JSP.getString("jsp.index")));
    }
}
