package net.cheltsov.shtoss.command;

import net.cheltsov.shtoss.entity.Suit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ResourceBundle;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

public class ChangeSuitCommand implements Command, Responsenable {
    private static final String PARAM_ATTR_SUIT = "suit";
    private static final String ATTR_SUCCESS = "success";
    private static final String ATTR_RESPONSE = "response";

    @Override
    public String execute(HttpServletRequest request) {
        synchronized (request.getSession()) {
            Suit currentSuit = (Suit) request.getSession().getAttribute(PARAM_ATTR_SUIT);
            currentSuit = currentSuit != null ? currentSuit : (Suit) request.getServletContext().getAttribute(PARAM_ATTR_SUIT);
            Suit newSuit = Suit.valueOf(request.getParameter(PARAM_ATTR_SUIT).trim().toUpperCase());
            if (currentSuit == newSuit) return PATH_JSP.getString("jsp.settings");

            request.getSession().setAttribute(PARAM_ATTR_SUIT, newSuit);
            Cookie cookie = new Cookie(PARAM_ATTR_SUIT, newSuit.toString());
            cookie.setMaxAge(60 * 60 * 24 * 365);
            HttpServletResponse response = (HttpServletResponse) request.getAttribute(ATTR_RESPONSE);
            response.addCookie(cookie);
            request.removeAttribute(ATTR_RESPONSE);
            ResourceBundle rb = getCurrentBundle(request);
            request.setAttribute(ATTR_SUCCESS, rb.getString("mess.success.changed-suit"));
        }
        return PATH_JSP.getString("jsp.settings");
    }
}
