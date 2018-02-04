package net.cheltsov.shtoss.command;

import net.cheltsov.shtoss.entity.Deck;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ResourceBundle;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

public class ChangeDeckCommand implements Command, Responsenable {
    private static final String PARAM_ATTR_DECK = "deck";
    private static final String ATTR_SUCCESS = "success";
    private static final String ATTR_RESPONSE = "response";

    @Override
    public String execute(HttpServletRequest request) {
        synchronized (request.getSession()) {
            Deck currentDeck = (Deck) request.getSession().getAttribute(PARAM_ATTR_DECK);
            currentDeck = currentDeck != null ? currentDeck : (Deck) request.getServletContext().getAttribute(PARAM_ATTR_DECK);
            Deck newDeck = Deck.valueOf(request.getParameter(PARAM_ATTR_DECK).trim().toUpperCase());
            if (currentDeck == newDeck) {
                return PATH_JSP.getString("jsp.settings");
            }
            request.getSession().setAttribute(PARAM_ATTR_DECK, newDeck);
            Cookie cookie = new Cookie(PARAM_ATTR_DECK, newDeck.toString());
            cookie.setMaxAge(60 * 60 * 24 * 365);
            HttpServletResponse response = (HttpServletResponse) request.getAttribute(ATTR_RESPONSE);
            response.addCookie(cookie);
            request.removeAttribute(ATTR_RESPONSE);
            ResourceBundle rb = getCurrentBundle(request);
            request.setAttribute(ATTR_SUCCESS, rb.getString("mess.success.changed-deck"));
        }
        return PATH_JSP.getString("jsp.settings");
    }
}
