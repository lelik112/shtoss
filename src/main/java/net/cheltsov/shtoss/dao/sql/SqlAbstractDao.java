package net.cheltsov.shtoss.dao.sql;

import net.cheltsov.shtoss.database.ConnectionPool;
import net.cheltsov.shtoss.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SqlAbstractDao {
    protected static final Logger LOGGER = LogManager.getRootLogger();
    private Connection connection;
    private boolean isClassLevel;

       SqlAbstractDao(Connection connection) {
        this.connection = connection;
        isClassLevel = true;
    }

    public SqlAbstractDao() {
    }

    Connection getConnection() throws DaoException {
        if (isClassLevel && connection == null) {
            throw new DaoException("Class level DAO is closed");  // TODO: 13.12.2017 Нормально так бросать?
        }
        return connection != null? connection: ConnectionPool.getInstance().getConnection();
    }

    void releaseConnectionIfLocal (Connection connection) {
        if (!isClassLevel) {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    void closeClassLevelDao() {
        connection = null;
    }

    int findIntByInt(String query, int id) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next()? rs.getInt(1): 0;
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    public boolean isClosed() {
        return isClassLevel? connection == null: false; // TODO: 13.12.2017 Что лучше вернуть если уровень метода?
    }

    public boolean isClassLevel() {
        return isClassLevel;
    }

}
