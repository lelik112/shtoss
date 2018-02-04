package net.cheltsov.shtoss.dao.sql;

import net.cheltsov.shtoss.dao.NewsDao;
import net.cheltsov.shtoss.entity.News;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.DaoException;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class SqlNewsDao extends SqlAbstractDao implements NewsDao {
    private static final String DAO_NEWS_ID = "news_id";
    private static final String DAO_U_USER_ID = "u.user_id";
    private static final String DAO_USER_LOGIN = "login";
    private static final String DAO_NEWS_CAPTION = "caption";
    private static final String DAO_NEWS_TEXT = "news_text";
    private static final String DAO_NEWS_TIME = "news_time";

    private static final String SQL_FIND_SOME_LAST_NEWS = "SELECT news_id, caption, news_text, news_time, u.user_id, login  FROM news n " +
            "JOIN user u ON n.user_id = u.user_id ORDER BY news_time DESC LIMIT ?";
    private static final String SQL_FIND_ALL_NEWS = "SELECT news_id, caption, news_text, news_time, u.user_id, login  FROM news n " +
            "JOIN user u ON n.user_id = u.user_id ORDER BY news_time DESC";
    private static final String SQL_CREATE_NEWS = "INSERT INTO news (user_id, caption, news_text) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE_NEWS = "UPDATE news SET caption = ?, news_text = ? WHERE news_id = ?";
    private static final String SQL_DELETE_NEWS = "DELETE FROM news WHERE news_id = ?";

    private static SqlNewsDao methodLevelNewsDao = new SqlNewsDao();

    private SqlNewsDao(){}

    SqlNewsDao(SqlInitializer initializer) {
        super(initializer.getConnection());
        initializer.addDao(this);
    }

    public static SqlNewsDao getMethodLevelNewsDao() {
        return methodLevelNewsDao;
    }

    @Override
    public List<News> findAllNews() throws DaoException {
        Connection cn = getConnection();
        try (Statement s = cn.createStatement()) {
            ResultSet rs = s.executeQuery(SQL_FIND_ALL_NEWS);
            return buildNews(rs);
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    private List<News> buildNews(ResultSet rs) throws SQLException {
        List<News> news = new LinkedList<>();
        while (rs.next()) {
            User user = new User();
            user.setUserId(rs.getInt(DAO_U_USER_ID));
            user.setLogin(rs.getString(DAO_USER_LOGIN));
            News newsOne = new News();
            newsOne.setNewsId(rs.getInt(DAO_NEWS_ID));
            newsOne.setCaption(rs.getString(DAO_NEWS_CAPTION));
            newsOne.setText(rs.getString(DAO_NEWS_TEXT));
            newsOne.setDate(rs.getTimestamp(DAO_NEWS_TIME));
            newsOne.setUser(user);
            news.add(newsOne);
        }
        return news;
    }

    @Override
    public int crateNews(News news) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_CREATE_NEWS, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, news.getUser().getUserId());
            ps.setString(2, news.getCaption());
            ps.setString(3, news.getText());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    @Override
    public boolean deleteNews(int newsID) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_DELETE_NEWS)){
            ps.setInt(1, newsID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    @Override
    public boolean updateNews(News news) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_UPDATE_NEWS)){
            ps.setString(1, news.getCaption());
            ps.setString(2, news.getText());
            ps.setInt(3, news.getNewsId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }
}
