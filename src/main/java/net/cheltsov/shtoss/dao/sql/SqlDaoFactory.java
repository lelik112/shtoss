package net.cheltsov.shtoss.dao.sql;

import net.cheltsov.shtoss.dao.*;
import net.cheltsov.shtoss.exception.DaoException;

public class SqlDaoFactory implements AbstractDaoFactory<SqlInitializer> {
    @Override
    public UserDao getUserDao() {
        return SqlUserDao.getMethodLevelUserDao();
    }

    @Override
    public UserDao getUserDao(SqlInitializer initializer) {
        return new SqlUserDao(initializer);
    }

    @Override
    public PaymentDao getPaymentDao() {
        return SqlPaymentDao.getMethodLevelPaymentDao();
    }

    @Override
    public PaymentDao getPaymentDao(SqlInitializer initializer) {
        return new SqlPaymentDao(initializer);
    }

    @Override
    public GameDao getGameDao() {
        return SqlGameDao.getMethodLevelGameDao();
    }

    @Override
    public GameDao getGameDao(SqlInitializer initializer) {
        return new SqlGameDao(initializer);
    }

    @Override
    public ConversationDao getConversationDao() {
        return SqlConversationDao.getMethodLevelConversationDao();
    }

    @Override
    public ConversationDao getConversationDao(SqlInitializer initializer) {
        return new SqlConversationDao(initializer);
    }

    @Override
    public NewsDao getNewsDao() {
        return SqlNewsDao.getMethodLevelNewsDao();
    }

    @Override
    public NewsDao getNewsDao(SqlInitializer initializer) {
        return new SqlNewsDao(initializer);
    }

    @Override
    public SqlInitializer getInitializer() throws DaoException {
        return new SqlInitializer();
    }

}
