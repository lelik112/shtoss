package net.cheltsov.shtoss.database;

import net.cheltsov.shtoss.exception.DaoException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static net.cheltsov.shtoss.resource.BundleManager.DATABASE;

public final class ConnectionPool {

    /**
     * Minimum number of connection which can exist
     */
    public final int MIN_SIZE = 10;
    /**
     * Maximum number of connection which can exist
     */
    public final int MAX_SIZE = 50;
    /**
     * The number of connection which pool expands of
     */
    public final int EXTENSION_SIZE = 10;


    private static final Logger LOGGER = LogManager.getLogger();
    private static final AtomicBoolean instanceCreated = new AtomicBoolean(false);
    private static final Lock lock = new ReentrantLock();
    /**
     * The only instance of pool of connections
     */
    private static ConnectionPool instance;

    private final String DB_URI_VALUE;
    private final Properties properties;

    /**
     * The time of waiting a connection in seconds before an Exception be thrown
     */
    private final int WAIT_CONNECTION_SECONDS = 10;
    /**
     * The period of time in minutes after expiring which the connection is lost
     */
    private final int LOOSING_CONNECTION_INTERVAL_MINUTES = 10;
    /**
     * The period of time in minutes of checking losing connections
     */
    private final int CHECKING_INTERVAL_MINUTES = 30;

    /**
     * Current size of pool
     */
    private final AtomicInteger currentNumber;
    /**
     * The queue of available connections
     */
    private final BlockingQueue<Connection> waitingConnections;
    /**
     * The map of connections which are currently used. The values of map are time stamps of getting
     * connection from pool
     */
    private final ConcurrentHashMap<Connection, Long> occupiedConnections;
    /**
     * The executor service to periodically check of losing connection
     */
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private ConnectionPool() {
        if (instance != null) {
            LOGGER.log(Level.FATAL, "Attempt to createUser the second instance of pool");
            throw new RuntimeException("Don't try to do it");
        }

        DB_URI_VALUE = DATABASE.getString("url");
        currentNumber = new AtomicInteger(0);
        waitingConnections = new ArrayBlockingQueue<>(MAX_SIZE);
        occupiedConnections = new ConcurrentHashMap<>();
        properties = new Properties();
        try {
            properties.load(ConnectionPool.class.getClassLoader().getResourceAsStream("properties/connection.properties"));
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            createConnections(MIN_SIZE);
        } catch (SQLException | IOException e) {
            LOGGER.log(Level.FATAL, "Can't createUser connection pool", e);
            throw new RuntimeException("Can't createUser connection pool", e);
        }

        executorService.scheduleAtFixedRate(new PoolSizeChecker(), CHECKING_INTERVAL_MINUTES, CHECKING_INTERVAL_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * @return the only instance this class
     */
    public static ConnectionPool getInstance() {
        if (!instanceCreated.get()) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new ConnectionPool();
                    instanceCreated.set(true);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    /**
     * The Runnable implementation for checker of losing connection thread
     */
    private class PoolSizeChecker implements Runnable {
        @Override
        public void run() {
            while (waitingConnections.size() > MIN_SIZE) {
                closeConnection(waitingConnections.poll());
            }

            for (Connection c : occupiedConnections.keySet()) {
                Long delta = TimeUnit.MINUTES.toMinutes(System.currentTimeMillis() - occupiedConnections.get(c));
                if (delta > LOOSING_CONNECTION_INTERVAL_MINUTES) {
                    occupiedConnections.remove(c);
                    closeConnection(c);
                }
            }
            LOGGER.log(Level.INFO, "MapSize = " + occupiedConnections.size() + "; " + "QueueSize = " +
                    waitingConnections.size() + " currentNumber = " + currentNumber);
        }
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.ERROR, "Error while closing connection", e);
            } finally {
                currentNumber.decrementAndGet();
            }
        }
    }

    /**
     * Releases a connection to pool for reuse
     *
     * @param connection to be reuse
     */
    public void releaseConnection(Connection connection) {
        if (connection == null || occupiedConnections.remove(connection) == null) {
            return;
        }
        try {
            if (connection.isClosed()) {
                currentNumber.decrementAndGet();
                return;
            }
            if (!connection.getAutoCommit()) {
                connection.setAutoCommit(true);
            }
            waitingConnections.put(connection);
        } catch (InterruptedException | SQLException e) {
            closeConnection(connection);
            LOGGER.log(Level.ERROR, "Error while returning connection", e);
        }
    }

    /**
     * Gets connection from queue. If queue is empty and current size of pool less then
     * maximum size the method extends the pool by EXTENSION_SIZE but not more than
     * (MAX_SIZE - currentNumber). If (MAX_SIZE = currentNumber) the method waits
     * for releasing connections but not more than WAIT_CONNECTION_SECONDS seconds.
     * The DaoException will be thrown after that
     *
     * @return connection to data base
     * @throws DaoException if period of waiting connection is expired
     */
    public Connection getConnection() throws DaoException {
        Connection connection = waitingConnections.poll();
        if (connection != null) {
            return addToMapAndReturn(connection);
        }

        try {
            while (currentNumber.get() < MAX_SIZE && connection == null) {
                createConnections(EXTENSION_SIZE);
                connection = waitingConnections.poll();
            }
        } catch (SQLException e) {
            throw new DaoException("Error while creating connection", e);
        }

        if (connection != null) {
            return addToMapAndReturn(connection);
        }

        try {
            connection = waitingConnections.poll(WAIT_CONNECTION_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.catching(e);
        }
        if (connection != null) {
            return addToMapAndReturn(connection);
        }
        throw new DaoException("Specified waiting time elapsed");
    }

    private void createConnections(int quantity) throws SQLException {
        if (!lock.tryLock()) {
            return;
        }
        int limit = MAX_SIZE - currentNumber.get();
        try {
            for (int i = 0; i < Math.min(quantity, limit); i++) {
                waitingConnections.add(DriverManager.getConnection(DB_URI_VALUE, properties));
                currentNumber.incrementAndGet();
            }
        } finally {
            lock.unlock();
        }
    }

    private Connection addToMapAndReturn(Connection connection) {
        if (connection != null) {
            occupiedConnections.put(connection, System.currentTimeMillis());
        }
        return connection;
    }

    /**
     *
     * @return current size of connection pool
     */
    public int getCurrentSize() {
        return currentNumber.get();
    }

    /**
     * Closes connection pool
     */
    public void closePool() {
        Connection connection;
        while (currentNumber.get() > 0) {
            try {
                connection = waitingConnections.poll(60, TimeUnit.SECONDS);
                if (connection != null) {
                    closeConnection(connection);
                } else {
                    occupiedConnections.keySet()
                            .stream()
                            .filter(x -> occupiedConnections.remove(x) != null)
                            .forEach(this::closeConnection);
                }
            } catch (InterruptedException e) {
                LOGGER.catching(e);
            }
        }

        deregisterDriver();
        executorService.shutdown();
        LOGGER.log(Level.INFO, "Pool closed");
    }

    private void deregisterDriver() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                LOGGER.log(Level.INFO, "Driver was deregistered", driver);
            } catch (SQLException e) {
                LOGGER.catching(e);
            }
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        LOGGER.log(Level.FATAL, "Attempt to create the second instance of pool");
        throw new CloneNotSupportedException("Don't try to do it");
    }

}
