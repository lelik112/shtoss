package net.cheltsov.shtoss.controller;

import net.cheltsov.shtoss.database.ConnectionPool;
import net.cheltsov.shtoss.entity.Deck;
import net.cheltsov.shtoss.entity.Suit;
import net.cheltsov.shtoss.exception.ServiceException;
import net.cheltsov.shtoss.resource.BundleManager;
import net.cheltsov.shtoss.service.NewsService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ShtossContextListener implements ServletContextListener {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ENCODING = "UTF-8";
    private static final String ATTR_MESSAGE = "message";
    private static final String PARAM_APP_ENCODING = "encoding";
    private static final String PARAM_APP_LANGUAGE = "language";
    private static final String ATTR_NEWS = "news";
    private static final String ATTR_DECK = "deck";
    private static final String ATTR_SUIT = "suit";

    // TODO: 08.01.2018 Нормально все в одном методе?

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        /**
         * Setting default language
         */
        BundleManager bundleManager = BundleManager.getMessageBundleManager(servletContext.getInitParameter(PARAM_APP_LANGUAGE));
        servletContext.setAttribute(ATTR_MESSAGE, bundleManager.getBundle());
        LOGGER.log(Level.INFO, "Attribute 'message' was added");
        /**
         * Adding cards attribute to context scope
         */
        servletContext.setAttribute(ATTR_DECK, Deck.SMALL);
        servletContext.setAttribute(ATTR_SUIT, Suit.SPADES);
        /**
         * Setting encoding of requests and response
         */
        String encoding = servletContext.getInitParameter(PARAM_APP_ENCODING);
        if (encoding == null) {
            encoding = ENCODING;
        }
        servletContext.setRequestCharacterEncoding(encoding);
        LOGGER.log(Level.INFO, "Request's encoding was set");
        servletContext.setResponseCharacterEncoding(encoding);
        LOGGER.log(Level.INFO, "Response's encoding was set");
        /**
         * Insentient ConnectionPool
         */
        ConnectionPool.getInstance();
        LOGGER.log(Level.INFO, "Connection pool was created");
        /**
         * Adding news attribute to context scope
         */
        try {
            servletContext.setAttribute(ATTR_NEWS, NewsService.findLAllNews());
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, "Can't find news", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPool.getInstance().closePool();
    }
}
