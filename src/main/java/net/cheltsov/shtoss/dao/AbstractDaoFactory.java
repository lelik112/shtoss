package net.cheltsov.shtoss.dao;

import net.cheltsov.shtoss.exception.DaoException;

/**
 * This interface provides two types of specific DAO. Methods without parameters
 * provide method level specific DAO. Methods with parameter <T extends Initializer>
 * provides logic level specific DAO.
 *
 * @param <T> specific <tt>Initializer</tt> for initialisation logic level specific DAO
 */
public interface AbstractDaoFactory <T extends Initializer> {
    UserDao getUserDao();

    UserDao getUserDao(T initializer);

    PaymentDao getPaymentDao();

    PaymentDao getPaymentDao(T initializer);

    GameDao getGameDao();

    GameDao getGameDao(T initializer);

    ConversationDao getConversationDao();

    ConversationDao getConversationDao(T initializer);

    NewsDao getNewsDao();

    NewsDao getNewsDao(T initializer);

    T getInitializer() throws DaoException;
}
