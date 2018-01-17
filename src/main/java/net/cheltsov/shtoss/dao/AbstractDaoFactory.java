package net.cheltsov.shtoss.dao;

import net.cheltsov.shtoss.entity.Game;
import net.cheltsov.shtoss.entity.News;
import net.cheltsov.shtoss.exception.DaoException;

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
