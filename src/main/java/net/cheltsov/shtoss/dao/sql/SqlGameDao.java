package net.cheltsov.shtoss.dao.sql;

import net.cheltsov.shtoss.dao.GameDao;
import net.cheltsov.shtoss.entity.Game;
import net.cheltsov.shtoss.entity.UserRating;
import net.cheltsov.shtoss.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlGameDao extends SqlAbstractDao implements GameDao {
    private static final String DAO_U_LOGIN = "u.login";
    private static final String DAO_ALL_GAMES = "all_games";
    private static final String DAO_WIN_GAMES = "win_games";
    private static final String DAO_TOTAL_BID = "total_bid";
    private static final String SQL_FIND_LAST_GAME_ID = "SELECT max(game_id) FROM game WHERE user_id = ?";
    private static final String SQL_ADD_GAME = "INSERT INTO game (game_id, user_id, bid) VALUES (?, ?, ?)";
    private static final String SQL_FIND_RATING = "SELECT u.login, count(*) all_games, " +
            "   (SELECT count(*) FROM game WHERE user_id = g.user_id AND bid > 0) win_games, " +
            "   sum(abs(g.bid)) total_bid " +
            "FROM game g JOIN user u ON g.user_id = u.user_id GROUP BY g.user_id ORDER BY win_games DESC";

    private static SqlGameDao methodLevelGameDao = new SqlGameDao();

    private SqlGameDao() {
    }

    SqlGameDao(SqlInitializer initializer) {
        super(initializer.getConnection());
        initializer.addDao(this);
    }

    public static SqlGameDao getMethodLevelGameDao() {
        return methodLevelGameDao;
    }

    @Override
    public int findLastGameId(int userID) throws DaoException {
        return findIntByInt(SQL_FIND_LAST_GAME_ID, userID);
    }

    @Override
    public boolean create(Game game) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_ADD_GAME)) {
            ps.setInt(1, game.getGameId());
            ps.setInt(2, game.getUserId());
            ps.setBigDecimal(3, game.getBid());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    @Override
    public List<UserRating> findRating() throws DaoException {
        Connection cn = getConnection();
        try (Statement s = cn.createStatement()) {
            ResultSet rs = s.executeQuery(SQL_FIND_RATING);
            return buildRating(rs);
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    private List<UserRating> buildRating(ResultSet rs) throws SQLException {
        List<UserRating> ratings = new ArrayList<>();
        while (rs.next()) {
            UserRating rating = new UserRating();
            rating.setUserLogin(rs.getString(DAO_U_LOGIN));
            rating.setAllGames(rs.getInt(DAO_ALL_GAMES));
            rating.setWinGames(rs.getInt(DAO_WIN_GAMES));
            rating.setTotalBid(rs.getBigDecimal(DAO_TOTAL_BID));
            ratings.add(rating);
        }
        return ratings;
    }

}
