package net.cheltsov.shtoss.dao;

import net.cheltsov.shtoss.entity.News;
import net.cheltsov.shtoss.exception.DaoException;

import java.util.List;

/**
 * Provides methods to access data base for <tt>News</tt> class
 */
public interface NewsDao {
    List<News> findAllNews() throws DaoException;

    int crateNews(News news) throws DaoException;

    boolean deleteNews(int newsID) throws DaoException;

    boolean updateNews(News news) throws DaoException;
}
