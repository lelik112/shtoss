package net.cheltsov.shtoss.service;

import net.cheltsov.shtoss.dao.AbstractDaoFactory;
import net.cheltsov.shtoss.dao.DaoManager;
import net.cheltsov.shtoss.dao.sql.SqlDaoFactory;
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

public class NewsService {
    private static final Logger LOGGER = LogManager.getLogger();
    private static AbstractDaoFactory factory = DaoManager.getDaoFactory();

    // TODO: 29.12.2017 Убрать метод
    public static List<News> findLastSomeNews(int countNews) throws ServiceException {
        try {
            return SqlNewsDao.getMethodLevelNewsDao().findSomeLastNews(countNews);
        } catch (DaoException e) {
            throw new ServiceException("Exception while finding last news", e);
        }
    }

    public static List<News> findLAllNews() throws ServiceException {
        try {
            return SqlNewsDao.getMethodLevelNewsDao().findAllNews();
        } catch (DaoException e) {
            throw new ServiceException("Exception while finding last news", e);
        }
    }

    public static Optional<News> createNews(User user, String topic, String text) {
        News news = new News();
        news.setCaption(topic);
        news.setText(text);
        news.setUser(user);
        try {
            news.setNewsID(factory.getNewsDao().crateNews(news));
            news.setDate(new Date());
            return Optional.of(news);
        } catch (DaoException e) {
            LOGGER.log(Level.ERROR, "Can't createUser news", e);
            return Optional.empty();
        }
    }

    public static boolean updateNews(String topic, String text, int newsID) {
        News news = new News();
        news.setNewsID(newsID);
        news.setCaption(topic);
        news.setText(text);
        try {
            return factory.getNewsDao().updateNews(news);
        } catch (DaoException e) {
            LOGGER.log(Level.ERROR, "Can't update news", e);
            return false;
        }
    }

    public static boolean deleteNews(int newsID) {
        try {
            return factory.getNewsDao().deleteNews(newsID);
        } catch (DaoException e) {
            LOGGER.log(Level.ERROR, "Can't delete news", e);
            return false;
        }
    }
}
