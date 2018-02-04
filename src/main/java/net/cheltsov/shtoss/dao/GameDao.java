package net.cheltsov.shtoss.dao;

import net.cheltsov.shtoss.entity.Game;
import net.cheltsov.shtoss.entity.UserRating;
import net.cheltsov.shtoss.exception.DaoException;

import java.util.List;

/**
 * Provides methods to access data base for <tt>Game</tt> class
 */
public interface GameDao {
    int findLastGameId(int userId) throws DaoException;

    boolean create(Game game) throws DaoException;

    List<UserRating> findRating() throws DaoException;
}
