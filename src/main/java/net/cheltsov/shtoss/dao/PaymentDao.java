package net.cheltsov.shtoss.dao;

import net.cheltsov.shtoss.entity.BalanceOperation;
import net.cheltsov.shtoss.entity.Payment;
import net.cheltsov.shtoss.exception.DaoException;

import java.util.List;

public interface PaymentDao {
    boolean  create (Payment payment) throws DaoException;
    List<BalanceOperation> findUserOperations (int userID) throws DaoException;
}
