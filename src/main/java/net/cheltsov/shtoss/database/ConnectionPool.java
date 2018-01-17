package net.cheltsov.shtoss.database;

import net.cheltsov.shtoss.exception.DaoException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileReader;
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

    private static final Logger LOGGER = LogManager.getLogger();
    private static final AtomicBoolean instanceCreated = new AtomicBoolean(false);
    private static final Lock lock = new ReentrantLock();
    private static ConnectionPool instance;

    private final String DB_URI_VALUE;
    private final Properties properties;

    private final int MIN_SIZE = 10;
    private final int EXTENSION_SIZE = 10;
    private final int MAX_SIZE = 50;
    private final int WAIT_CONNECTION_SECONDS = 10;
    private final int LOOSING_CONNECTION_INTERVAL_MINUTES = 10;
    private final int CHECKING_INTERVAL_MINUTES = 30;

    private final AtomicInteger currentNumber;
    private final BlockingQueue<Connection> waitingConnections;
    private final ConcurrentHashMap<Connection, Long> occupiedConnections;
    private final ScheduledExecutorService es = Executors.newScheduledThreadPool(1);

    private ConnectionPool() {
        if (instance != null) {
            LOGGER.log(Level.FATAL,"Attempt to createUser the second instance of pool");
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
            LOGGER.log(Level.FATAL,"Can't createUser connection pool", e);
            throw new RuntimeException("Can't createUser connection pool", e);
        }

        es.scheduleAtFixedRate(new PoolSizeChecker(), CHECKING_INTERVAL_MINUTES, CHECKING_INTERVAL_MINUTES, TimeUnit.MINUTES);
    }

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

   private class PoolSizeChecker implements Runnable {
        @Override
        public void run() {
            while (waitingConnections.size() > MIN_SIZE) {
                closeConnection(waitingConnections.poll());
            }

            for (Connection c : occupiedConnections.keySet()) {
                Long delta = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - occupiedConnections.get(c)); // TODO: 29.11.2017 change toMinutes
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

    public Connection getConnection() throws DaoException {
        Connection connection = waitingConnections.poll();
        if (connection != null) {
            return addToMapAndReturn(connection);
        }

        try {
            while (currentNumber.get() <= MAX_SIZE && connection == null) {
                createConnections(EXTENSION_SIZE);
                connection = waitingConnections.poll();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Error while creating connection", e);
        }

        if (connection != null) {
            return addToMapAndReturn(connection);
        }
        try {
            connection = waitingConnections.poll(WAIT_CONNECTION_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException ignore) {
                /*NOP*/
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
        try {
            for (int i = 0; i < Math.min(quantity, MAX_SIZE - currentNumber.get()); i++) {
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

    public void closePool() {
        waitingConnections.stream().forEach(this::closeConnection);
        waitingConnections.clear();
        occupiedConnections.keySet().stream().forEach(this::closeConnection);
        occupiedConnections.clear();
        deregisterDriver();
        es.shutdown();
        LOGGER.log(Level.INFO,"Pool closed");
    }

    private void deregisterDriver() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                LOGGER.log(Level.INFO,"Driver was deregistered", driver);
            } catch (SQLException e) {
                LOGGER.log(Level.ERROR, "Error while deregistering driver.", e);
            }
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        LOGGER.log(Level.FATAL,"Attempt to createUser the second instance of pool");
        throw new CloneNotSupportedException("Don't try to do it");
    }

}
