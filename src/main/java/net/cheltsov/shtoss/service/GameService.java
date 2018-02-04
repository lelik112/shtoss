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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A service layer class implementing all the logic concerning games
 */
public class GameService {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final int STEP = 16;
    private static final int PLAYING_CARD_BACK = 0x1F0A0;
    private static AbstractDaoFactory factory = DaoManager.getDaoFactory();

    /**
     * Creates an act of the game.
     *
     * @param user user to play
     * @param bid  the bid of game
     * @param card user's card
     * @param deck user's deck
     * @return the list of the string representation code points of gaming cards
     * @throws ServiceException if any exceptions occurred on the DAO layer
     */
    public static List<String> doGame(User user, BigDecimal bid, int card, Deck deck) throws ServiceException {
        card = card & (STEP - 1);
        List<Integer> moves = giveOutCards(card, deck);
        Game game = new Game();
        game.setUserId(user.getUserId());
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
            game.setGameId(gameDao.findLastGameId(user.getUserId()) + 1);
            gameDao.create(game);
            userDao.updateBalance(game.getBid(), user.getUserId());
            initializer.commit();
            LOGGER.log(Level.INFO, "User: " + user.getLogin() + " did the game");
        } catch (DaoException e) {
            initializer.rollback();
            throw new ServiceException("Can't update database with game info", e);
        } finally {
            initializer.close();
        }
        user.setBalance(user.getBalance().add(game.getBid()));
        return moves.stream().map(Integer::toHexString).map(x -> 'x' + x).collect(Collectors.toList());
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
            current = cards
                    .stream()
                    .skip(r.nextInt(cards.size()))
                    .findFirst()
                    .orElse(-1);
            moves.add(current + PLAYING_CARD_BACK);
            cards.remove(current);
        }
        return moves;
    }

    /**
     * Finds ratings of users
     *
     * @return the list of ratings
     * @throws ServiceException if any exceptions occurred on the DAO layer
     */
    public static List<UserRating> findRating() throws ServiceException {
        try {
            return factory.getGameDao().findRating();
        } catch (DaoException e) {
            throw new ServiceException("Can't find rating", e);
        }
    }
}
