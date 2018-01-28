package net.cheltsov.shtoss.database;

import net.cheltsov.shtoss.exception.DaoException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static net.cheltsov.shtoss.resource.BundleManager.DATABASE;

@Test(singleThreaded = true)
public class ConnectionPoolTest {
    private ConnectionPool instance;

    @BeforeClass
    public void init() throws Exception {
        instance = ConnectionPool.getInstance();
    }

    @Test
    public void testGetInstanceNotNull() throws Exception {
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetInstanceSame() throws Exception {
        ConnectionPool actualInstance = ConnectionPool.getInstance();
        Assert.assertSame(actualInstance, instance);
    }

    @Test
    public void testReleaseConnectionNormal() throws Exception {
        Set<Connection> connections = new HashSet<>(instance.MAX_SIZE);
        Connection expectedConnection = instance.getConnection();
        for (int i = 0; i < instance.MAX_SIZE - 1; i++) {
            connections.add(instance.getConnection());
        }
        instance.releaseConnection(expectedConnection);
        Connection actualConnection = instance.getConnection();
        instance.releaseConnection(actualConnection);
        connections.forEach(instance::releaseConnection);
        connections.clear();

        Assert.assertSame(actualConnection, expectedConnection);
    }

    @Test
    public void testReleaseNotAutoCommittedConnectionChange() throws Exception {
        Connection connection = instance.getConnection();
        connection.setAutoCommit(false);
        instance.releaseConnection(connection);
        Assert.assertTrue(connection.getAutoCommit());
    }

    @Test
    public void testReleaseForeignConnectionIgnore() throws Exception {
        final String URL = DATABASE.getString("url") + "?serverTimezone=" + DATABASE.getString("serverTimezone");
        final String USER = DATABASE.getString("user");
        final String PASSWORD = DATABASE.getString("password");

        Set<Connection> expectedConnections = new HashSet<>(instance.MAX_SIZE);
        for (int i = 0; i < instance.MAX_SIZE; i++) {
            expectedConnections.add(instance.getConnection());
        }

        Set<Connection> foreignConnection = new HashSet<>();
        for (int i = 0; i < instance.MAX_SIZE; i++) {
            foreignConnection.add(DriverManager.getConnection(URL, USER, PASSWORD));
        }

        Stream.of(expectedConnections, foreignConnection).flatMap(Set::parallelStream).forEach(instance::releaseConnection);

        Set<Connection> actualConnections = new HashSet<>(instance.MAX_SIZE);
        for (int i = 0; i < instance.MAX_SIZE; i++) {
            actualConnections.add(instance.getConnection());
        }

        actualConnections.forEach(instance::releaseConnection);
        foreignConnection.forEach(x -> {
            try {
                x.close();
            } catch (SQLException e) {
                /*NOP*/
            }
        });

        Assert.assertEquals(actualConnections, expectedConnections);
    }

    @Test
    public void testReleaseClosedConnectionReductionSize() throws Exception {
        Connection connection = instance.getConnection();
        connection.close();
        int expectedSize = instance.getCurrentSize() - 1;
        instance.releaseConnection(connection);
        int actualSize = instance.getCurrentSize();
        Assert.assertEquals(actualSize, expectedSize);
    }

    @Test(priority = -10)
    public void testGetConnectionIncrementPoolSize() throws Exception {
        LinkedHashSet<Integer> expectedSizes = new LinkedHashSet<>();
        LinkedHashSet<Integer> actualSizes = new LinkedHashSet<>();
        IntStream
                .iterate(instance.MIN_SIZE, x -> x + instance.EXTENSION_SIZE)
                .limit(instance.MAX_SIZE / instance.EXTENSION_SIZE)
                .forEach(expectedSizes::add);
        Set<Connection> connections = new HashSet<>(instance.MAX_SIZE);
        expectedSizes.add(instance.getCurrentSize());
        for (int i = 0; i < instance.MAX_SIZE; i++) {
            connections.add(instance.getConnection());
            actualSizes.add(instance.getCurrentSize());
        }
        connections.forEach(instance::releaseConnection);
        Assert.assertEquals(actualSizes, expectedSizes);
    }

    @Test
    public void testGetConnectionNotNull() throws Exception {
        Connection connection = instance.getConnection();
        instance.releaseConnection(connection);
        Assert.assertNotNull(connection);
    }

    @Test(expectedExceptions = DaoException.class, timeOut = 15 * 1000, priority = 9)
    public void testGetConnectionMoreThenMaxException() throws Exception {
        Set<Connection> connections = new HashSet<>(instance.MAX_SIZE);
        for (int i = 0; i < instance.MAX_SIZE; i++) {
            connections.add(instance.getConnection());
        }
        try {
            instance.getConnection();
        } finally {
            connections.forEach(instance::releaseConnection);
        }
    }

    @Test(expectedExceptions = CloneNotSupportedException.class)
    public void testCloneException() throws Exception {
        instance.clone();
    }

    @Test(priority = 10)
    public void testClosePoolSizeIsZero() throws Exception {
        instance.closePool();
        int expectedSize = 0;
        int actualSize = instance.getCurrentSize();
        Assert.assertEquals(actualSize, expectedSize);
    }


    @Test(dependsOnMethods = "testClosePoolSizeIsZero",
            expectedExceptions = DaoException.class,
            expectedExceptionsMessageRegExp = "Error while creating connection")
    public void testGetConnectionClosedPoolException() throws Exception {
        instance.getConnection();
    }


}