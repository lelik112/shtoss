package net.cheltsov.shtoss.dao;

import net.cheltsov.shtoss.exception.DaoException;

public interface Initializer extends AutoCloseable {
    void setAutoCommit(boolean autoCommit) throws DaoException;
    void commit() throws DaoException;
    void rollback();
    boolean isClosed();
    void close();
}
