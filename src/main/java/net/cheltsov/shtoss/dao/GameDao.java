package net.cheltsov.shtoss.dao;

import net.cheltsov.shtoss.entity.Game;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.entity.UserRating;
import net.cheltsov.shtoss.exception.DaoException;

import java.util.List;

public interface GameDao {
    int findLastGameID(int userID) throws DaoException;
    boolean create(Game game) throws DaoException;
    List<UserRating> findRating() throws DaoException;
}
