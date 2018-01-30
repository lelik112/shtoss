package net.cheltsov.shtoss.dao.sql;

import net.cheltsov.shtoss.database.ConnectionPool;
import net.cheltsov.shtoss.exception.DaoException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class SqlInitializerTest {

    @BeforeTest
    public void init() {
        ConnectionPool.getInstance();
    }

    @Test
    public void testGetAutoCommitTrue() throws DaoException {
        SqlInitializer initializer = new SqlInitializer();
        assertTrue(initializer.getAutoCommit());
        initializer.close();
    }

    @Test
    public void testSetAutoCommitTrue() throws DaoException {
        SqlInitializer initializer = new SqlInitializer();
        initializer.setAutoCommit(false);
        initializer.setAutoCommit(true);
        assertTrue(initializer.getAutoCommit());
        initializer.close();
    }

    @Test
    public void testSetAutoCommitFalse() throws DaoException {
        SqlInitializer initializer = new SqlInitializer();
        initializer.setAutoCommit(false);
        assertFalse(initializer.getAutoCommit());
        initializer.close();
    }

    @Test
    public void testIsClosedFalse() throws DaoException {
        SqlInitializer initializer = new SqlInitializer();
        assertFalse(initializer.isClosed());
        initializer.close();
    }

    @Test
    public void testIsClosedTrue() throws DaoException {
        SqlInitializer initializer = new SqlInitializer();
        initializer.close();
        assertTrue(initializer.isClosed());
    }
}