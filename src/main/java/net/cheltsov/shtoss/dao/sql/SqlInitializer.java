package net.cheltsov.shtoss.dao.sql;

import net.cheltsov.shtoss.dao.Initializer;
import net.cheltsov.shtoss.database.ConnectionPool;
import net.cheltsov.shtoss.exception.DaoException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlInitializer implements AutoCloseable, Initializer {
    private Connection connection;
    private List<SqlAbstractDao> listDao = new ArrayList<>();

    public SqlInitializer() throws DaoException {
        connection = ConnectionPool.getInstance().getConnection();
    }

    Connection getConnection() {
        return connection;
    }

    void addDao (SqlAbstractDao dao) {
        listDao.add(dao);
    }

    public void setAutoCommit(boolean autoCommit) throws DaoException {
        if (connection == null) {
            throw new DaoException("SqlInitializer is closed");
        }
        try {
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            throw new DaoException("Can't change auto commit", e);
        }
    }

    public void commit() throws DaoException {
        if (connection == null) {
            throw new DaoException("SqlInitializer is closed");
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException("Transaction was not committed", e);
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: 14.12.2017 Логгер!! Выкидывать дальше?
//            throw new DaoException("Problem with rollback", e);
        }
    }

    public boolean isClosed() {
        return connection == null;
    }

    @Override
    public void close() {
        listDao.forEach(SqlAbstractDao::closeClassLevelDao);
        ConnectionPool.getInstance().releaseConnection(connection);
        connection = null;
    }
}
