package net.cheltsov.shtoss.dao.sql;

import net.cheltsov.shtoss.database.ConnectionPool;
import net.cheltsov.shtoss.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Abstract root class for classes that provide access to the database
 */
public abstract class SqlAbstractDao {
    protected static final Logger LOGGER = LogManager.getRootLogger();
    private Connection connection;
    private boolean isClassLevel;

    SqlAbstractDao(Connection connection) {
        this.connection = connection;
        isClassLevel = true;
    }

    SqlAbstractDao() {
    }

    Connection getConnection() throws DaoException {
        if (isClassLevel && connection == null) {
            throw new DaoException("Class level DAO is closed");
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
        return isClassLevel && connection == null;
    }

    public boolean isClassLevel() {
        return isClassLevel;
    }

}
