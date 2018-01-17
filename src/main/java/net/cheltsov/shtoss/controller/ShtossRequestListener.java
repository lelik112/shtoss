package net.cheltsov.shtoss.controller;



import net.cheltsov.shtoss.entity.Deck;
import net.cheltsov.shtoss.entity.Suit;
import net.cheltsov.shtoss.resource.BundleManager;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class ShtossRequestListener implements ServletRequestListener {
    private static final String ATTR_MESSAGE = "message";
    private static final String COOKIE_LANGUAGE = "language";
    private static final String COOKIE_DECK = "deck";
    private static final String COOKIE_SUIT = "suit";

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        HttpSession session = request.getSession();
        if (session.isNew()) {
            Cookie[] cookies = request.getCookies();
            String cookieLanguage = null;
            if (cookies != null) {
                for (Cookie c: cookies) {
                    switch (c.getName()) {
                        case COOKIE_LANGUAGE:
                            cookieLanguage = c.getValue();
                            break;
                        case COOKIE_DECK:
                            session.setAttribute(COOKIE_DECK, Deck.valueOf(c.getValue().toUpperCase().trim()));
                            break;
                        case COOKIE_SUIT:
                            session.setAttribute(COOKIE_SUIT, Suit.valueOf(c.getValue().toUpperCase().trim()));
                            break;
                    }
                }
            }
            String requestLanguage = request.getLocale() != null? request.getLocale().getLanguage(): null;
            String language = cookieLanguage != null? cookieLanguage: requestLanguage;
            ResourceBundle bundle = BundleManager.getMessageBundleManager(language).getBundle();

            if (bundle != null || !bundle.equals(request.getServletContext().getAttribute(ATTR_MESSAGE))) {
                session.setAttribute(ATTR_MESSAGE, bundle);
            }
        }

    }
}
