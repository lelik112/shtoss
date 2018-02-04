package net.cheltsov.shtoss.service;

import net.cheltsov.shtoss.dao.AbstractDaoFactory;
import net.cheltsov.shtoss.dao.DaoManager;
import net.cheltsov.shtoss.dao.sql.SqlNewsDao;
import net.cheltsov.shtoss.entity.News;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.DaoException;
import net.cheltsov.shtoss.exception.ServiceException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * A service layer class implementing all the logic concerning news
 */
public class NewsService {
    private static final Logger LOGGER = LogManager.getLogger();
    private static AbstractDaoFactory factory = DaoManager.getDaoFactory();

    /**
     * Finds all news
     *
     * @return the list of news
     * @throws ServiceException if any exceptions occurred on the DAO layer
     */
    public static List<News> findLAllNews() throws ServiceException {
        try {
            return SqlNewsDao.getMethodLevelNewsDao().findAllNews();
        } catch (DaoException e) {
            throw new ServiceException("Exception while finding last news", e);
        }
    }

    /**
     * Creates news
     *
     * @param user  the author of news
     * @param topic the topic of news
     * @param text  the text of news
     * @return news, wrapped in Optional if news was created, <tt>Optional.empty()</tt> otherwise
     */
    public static Optional<News> createNews(User user, String topic, String text) {
        News news = new News();
        news.setCaption(topic);
        news.setText(text);
        news.setUser(user);
        try {
            news.setNewsId(factory.getNewsDao().crateNews(news));
            news.setDate(new Date());
            return Optional.of(news);
        } catch (DaoException e) {
            LOGGER.log(Level.ERROR, "Can't createUser news", e);
            return Optional.empty();
        }
    }

    /**
     * Updates given news
     *
     * @param topic  new topic
     * @param text   new text
     * @param newsId specific id of news
     * @return <tt>true</tt> if changing was successful, <tt>false</tt> otherwise
     */
    public static boolean updateNews(String topic, String text, int newsId) {
        News news = new News();
        news.setNewsId(newsId);
        news.setCaption(topic);
        news.setText(text);
        try {
            return factory.getNewsDao().updateNews(news);
        } catch (DaoException e) {
            LOGGER.log(Level.ERROR, "Can't update news", e);
            return false;
        }
    }

    /**
     * Deletes news
     *
     * @param newsId id of news to delete
     * @return <tt>true</tt> if deleting was successful, <tt>false</tt> otherwise
     */
    public static boolean deleteNews(int newsId) {
        try {
            return factory.getNewsDao().deleteNews(newsId);
        } catch (DaoException e) {
            LOGGER.log(Level.ERROR, "Can't delete news", e);
            return false;
        }
    }
}
