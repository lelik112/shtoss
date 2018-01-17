package net.cheltsov.shtoss.service;

import net.cheltsov.shtoss.dao.*;
import net.cheltsov.shtoss.entity.Deck;
import net.cheltsov.shtoss.entity.Game;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.entity.UserRating;
import net.cheltsov.shtoss.exception.DaoException;
import net.cheltsov.shtoss.exception.ServiceException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class GameService {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final int STEP = 16;
    private static final int PLAYING_CARD_BACK = 0x1F0A0;
    private static AbstractDaoFactory factory = DaoManager.getDaoFactory();

    public static List<Integer> doGame(User user, BigDecimal bid, int card, Deck deck) throws ServiceException {
        card = card & (STEP - 1);
        List<Integer> moves = giveOutCards(card, deck);
        Game game = new Game();
        game.setUserID(user.getID());
        if (moves.size() % 2 == 0) {
            game.setBid(bid);
        } else {
            game.setBid(bid.negate());
        }
        Initializer initializer = null;
        try {
            initializer = factory.getInitializer();
            initializer.setAutoCommit(false);
            GameDao gameDao = factory.getGameDao(initializer);
            UserDao userDao = factory.getUserDao(initializer);
            game.setGameID(gameDao.findLastGameID(user.getID()) + 1);
            gameDao.create(game);
            userDao.updateBalance(game.getBid(), user.getID());
            initializer.commit();
            LOGGER.log(Level.INFO, "User: " + user.getLogin() + " did the game");
        } catch (DaoException e) {
            initializer.rollback();
            throw new ServiceException("Can't update database with game info", e);
        } finally {
            initializer.close();
        }
        user.setBalance(user.getBalance().add(game.getBid()));
        return moves;
    }

    private static List<Integer> giveOutCards(int card, Deck deck) throws ServiceException {
        List<Integer> moves = new ArrayList<>();
        int current = -1;
        Set<Integer> cards = deck.getNewDeck();
        Random r = new Random();
        if (!cards.contains(card)) {
            throw new ServiceException("The card does not contain in the deck");
        }
        while ((current & (STEP - 1)) != card) {
            current = cards.stream().skip(r.nextInt(cards.size())).findFirst().orElse(-1);
            moves.add(current + PLAYING_CARD_BACK);
            cards.remove(current);
        }
        return moves;
    }

    public static List<UserRating> findRating() throws ServiceException {
        try {
            return factory.getGameDao().findRating();
        } catch (DaoException e) {
            throw new ServiceException("Can't find rating", e);
        }
    }
}
