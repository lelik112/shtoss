package net.cheltsov.shtoss.service;

import net.cheltsov.shtoss.dao.*;
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

/**
 * A service layer class implementing all the logic concerning payments
 */
public class PaymentService {
    private static final Logger LOGGER = LogManager.getLogger();
    private static AbstractDaoFactory factory = DaoManager.getDaoFactory();

    /**
     * Adds money to the user balance
     *
     * @param user   the user which balance are updated
     * @param amount the amount of payment
     * @return <tt>true</tt> if updating was successful, <tt>false</tt> otherwise
     */
    public static boolean addFunds(User user, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setUserId(user.getUserId());
        Initializer initializer = null;
        try {
            initializer = factory.getInitializer();
            UserDao userDao = factory.getUserDao(initializer);
            PaymentDao paymentDao = factory.getPaymentDao(initializer);
            initializer.setAutoCommit(false);
            paymentDao.create(payment);
            userDao.updateBalance(payment.getAmount(), user.getUserId());
            initializer.commit();
        } catch (DaoException e) {
            initializer.rollback();
            LOGGER.catching(e);
            return false;
        } finally {
            initializer.close();
        }
        try {
            user.setBalance(SqlUserDao.getMethodLevelUserDao().findUserById(user.getUserId()).getBalance());
        } catch (DaoException e) {
            LOGGER.catching(e);
        }
        return true;
    }

    /**
     * Finds all user's operations
     *
     * @param user the user whose operations are needed to be found
     * @return the list of operations of the user
     * @throws ServiceException if any exceptions occurred on the DAO layer
     */
    public static List<BalanceOperation> findUserOperations(User user) throws ServiceException {
        try {
            return factory.getPaymentDao().findUserOperations(user.getUserId());
        } catch (DaoException e) {
            throw new ServiceException("Problem with finding user operations", e);
        }
    }
}
