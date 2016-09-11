package com.epam.java.rt.lab.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * service-ms
 */
public class ConnectionPool implements DataSource {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
    private static String databaseUrl;
    private static String databaseUsername;
    private static String databasePassword;
    private static Integer databaseMaxConnections;
    private Semaphore connectionsSemaphore;
    private BlockingQueue<PooledConnection> availableConnectionsQueue;
    private AtomicInteger grantedConnections = new AtomicInteger();

    private ConnectionPool() {
        connectionsSemaphore = new Semaphore(ConnectionPool.databaseMaxConnections);
        availableConnectionsQueue = new ArrayBlockingQueue<PooledConnection>(ConnectionPool.databaseMaxConnections);
    }

    public static DataSource getInstance() throws ConnectionException {
        return InstanceHolder.INSTANCE;
    }

    public static void initProperties(Properties properties) throws ConnectionException {
        String databaseUrl = properties.getProperty("url");
        String databaseUsername = properties.getProperty("username");
        String databasePassword = properties.getProperty("password");
        String databaseMaxConnections = properties.getProperty("max-connections");
        if (databaseUrl == null || databaseUsername == null || databasePassword == null ||
                databaseMaxConnections == null) throw new ConnectionException("exception.connection.init-properties");
        ConnectionPool.databaseUrl = databaseUrl;
        ConnectionPool.databaseUsername = databaseUsername;
        ConnectionPool.databasePassword = databasePassword;
        ConnectionPool.databaseMaxConnections = Integer.valueOf(databaseMaxConnections);
    }

    public boolean shutdown(long millis) throws ConnectionException {
        logger.debug("Shutdown initiated");
        try {
            connectionsSemaphore = new Semaphore(0);
            long timeoutMillis = System.currentTimeMillis() + millis;
            while (System.currentTimeMillis() < timeoutMillis) {
                if (grantedConnections.get() == 0) break;
                Thread.sleep(100);
            }
            return grantedConnections.get() == 0;
        } catch (InterruptedException e) {
            throw new ConnectionException("Shutdown sleep timed out");
        }
    }

    public void releaseConnection(Connection connection) throws ConnectionException {
        try {
            availableConnectionsQueue.offer((PooledConnection) connection, 100, TimeUnit.MILLISECONDS);
            connectionsSemaphore.release();
        } catch (InterruptedException e) {
            throw new ConnectionException(e.getMessage());
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        try {
            if (connectionsSemaphore.tryAcquire(100, TimeUnit.MILLISECONDS)) {
                PooledConnection pooledConnection;
                if (availableConnectionsQueue.size() == 0) {
                    pooledConnection = new PooledConnection
                            (DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword));
                } else {
                    pooledConnection = availableConnectionsQueue.poll(100, TimeUnit.MILLISECONDS);
                    pooledConnection.clearConnection();
                }
                return pooledConnection;
            }
        } catch (InterruptedException e) {
            throw new SQLException(e.getMessage());
        }
        return null;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new UnsupportedOperationException
                ("Get connection should be requested with predefined in properties username and password");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    private static class InstanceHolder {
        private static final ConnectionPool INSTANCE = new ConnectionPool();
    }

}
