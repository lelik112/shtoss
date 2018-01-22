package net.cheltsov.shtoss.command;

import net.cheltsov.shtoss.resource.BundleManager;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ResourceBundle;

public class ChangeLanguageCommand implements Command, Responsenable {
    private static final String PARAM_LANGUAGE = "language";
    private static final String ATTR_MESSAGE = "message";
    private static final String ATTR_RESPONSE = "response";


    @Override
    public String execute(HttpServletRequest request) {
        String language = request.getParameter(PARAM_LANGUAGE);
        ResourceBundle bundle = BundleManager.getMessageBundleManager(language).getBundle();
        if (bundle != null) {
            request.getSession().setAttribute(ATTR_MESSAGE, bundle);
            Cookie cookie = new Cookie(PARAM_LANGUAGE, language);
            cookie.setMaxAge(60 * 60 * 24 * 365);
            HttpServletResponse response = (HttpServletResponse) request.getAttribute(ATTR_RESPONSE);
            response.addCookie(cookie);
        }
        request.removeAttribute(ATTR_RESPONSE);
        return repeatPreviousCommand(request);
    }
}
