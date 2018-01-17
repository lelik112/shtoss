package net.cheltsov.shtoss.dao;

import jdk.nashorn.internal.runtime.options.Option;
import net.cheltsov.shtoss.entity.News;
import net.cheltsov.shtoss.exception.DaoException;

import java.util.List;

public interface NewsDao {
    List<News> findSomeLastNews(int countNews) throws DaoException;
    List<News> findAllNews() throws DaoException;
    int crateNews(News news) throws DaoException;
    boolean deleteNews(int newsID) throws DaoException;
    boolean updateNews(News news) throws DaoException;
}
