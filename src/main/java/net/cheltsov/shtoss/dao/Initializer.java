package net.cheltsov.shtoss.dao;

import net.cheltsov.shtoss.exception.DaoException;

/**
 * This class is intended for initialisation logic level specific DAO
 * and provides method for transaction management
 */
public interface Initializer extends AutoCloseable {
    /**
     * Sets autoCommit auto-commit mode to the given state.
     * By default, new connections are in auto-commit mode.
     *
     * @param autoCommit <code>true</code> to enable auto-commit mode;
     *                   <code>false</code> to disable it
     * @throws DaoException if any exceptions occurred on the SQL layer
     */
    void setAutoCommit(boolean autoCommit) throws DaoException;

    /**
     * Makes all changes made since the previous
     * commit/rollback permanent
     * This method should be
     * used only when auto-commit mode has been disabled.
     *
     * @throws DaoException if any exceptions occurred on the SQL layer
     */
    void commit() throws DaoException;

    /**
     * Undoes all changes made in the current transaction
     * and releases any database locks currently held
     * This method should be
     * used only when auto-commit mode has been disabled.
     */
    void rollback();

    /**
     * Retrieves whether this <code>Initializer</code> object has been
     * closed.  A connection is closed if the method <code>close</code>
     * has been called on it or if certain fatal errors have occurred.
     * @return <code>true</code> if this <code>Connection</code> object
     *         is closed; <code>false</code> if it is still open
     */
    boolean isClosed();

    /**
     * Closes this object and all DAO which were initialised by it
     */
    void close();
}
