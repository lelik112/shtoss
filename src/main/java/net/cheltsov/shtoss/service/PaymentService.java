package net.cheltsov.shtoss.service;

import net.cheltsov.shtoss.dao.*;
import net.cheltsov.shtoss.dao.sql.SqlDaoFactory;
import net.cheltsov.shtoss.dao.sql.SqlUserDao;
import net.cheltsov.shtoss.entity.BalanceOperation;
import net.cheltsov.shtoss.entity.Payment;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.DaoException;
import net.cheltsov.shtoss.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;

public class PaymentService {
    private static final Logger LOGGER = LogManager.getLogger();
    private static AbstractDaoFactory factory = DaoManager.getDaoFactory();

    public static boolean addFunds(User user, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setUserID(user.getID());
        Initializer initializer = null;
        try {
            initializer = factory.getInitializer();
            UserDao userDao = factory.getUserDao(initializer);
            PaymentDao paymentDao = factory.getPaymentDao(initializer);
            initializer.setAutoCommit(false);
            paymentDao.create(payment);
            userDao.updateBalance(payment.getAmount(), user.getID());
            initializer.commit();
        } catch (DaoException e) {
            initializer.rollback();
            LOGGER.catching(e);
            return false;
        } finally {
            initializer.close();
        }
        try {
            user.setBalance(SqlUserDao.getMethodLevelUserDao().findUserById(user.getID()).getBalance());
        } catch (DaoException e) {
            LOGGER.catching(e);
        }
        return true;
    }

    public static List<BalanceOperation> findUserOperations(User user) throws ServiceException {
        try {
            return factory.getPaymentDao().findUserOperations(user.getID());
        } catch (DaoException e) {
            throw new ServiceException("Problem with finding user operations", e);
        }
    }
}
