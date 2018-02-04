package net.cheltsov.shtoss.dao;

import net.cheltsov.shtoss.entity.BalanceOperation;
import net.cheltsov.shtoss.entity.Payment;
import net.cheltsov.shtoss.exception.DaoException;

import java.util.List;

/**
 * Provides methods to access data base for <tt>Payment</tt> class
 */
public interface PaymentDao {
    boolean  create (Payment payment) throws DaoException;

    List<BalanceOperation> findUserOperations (int userID) throws DaoException;
}
