package com.epam.java.rt.lab.connection;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.exception.AppExceptionCode;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;

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

import static com.epam.java.rt.lab.connection.ConnectionExceptionCode.*;
import static com.epam.java.rt.lab.exception.AppExceptionCode.PROPERTY_READ_ERROR;
import static com.epam.java.rt.lab.web.validator.ValidatorFactory.DIGITS;

public class ConnectionPool implements DataSource {

    private static final String URL = "url";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MAX_CONNECTIONS = "max-connections";

    private static class InstanceHolder {

        private static final ConnectionPool INSTANCE = new ConnectionPool();

    }

    private String databaseUrl;
    private String databaseUsername;
    private String databasePassword;
    private Semaphore connectionsSemaphore;
    private BlockingQueue<PooledConnection> availableConnectionsQueue;
    private AtomicInteger grantedConnections = new AtomicInteger();

    private ConnectionPool() {
    }

    public static ConnectionPool getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void initDatabaseProperties(Properties properties)
            throws AppException {
        String databaseUrl = properties.getProperty(URL);
        String databaseUsername = properties.getProperty(USERNAME);
        String databasePassword = properties.getProperty(PASSWORD);
        String databaseMaxConnections = properties.getProperty(MAX_CONNECTIONS);
        if (databaseUrl == null || databaseMaxConnections == null
                || ValidatorFactory.getInstance().create(DIGITS).
                validate(databaseMaxConnections).length > 0) {
            throw new AppException(PROPERTY_READ_ERROR);
        }
        this.databaseUrl = databaseUrl;
        this.databaseUsername = databaseUsername;
        this.databasePassword = databasePassword;
        Integer databaseMaxConnectionsValue =
                Integer.valueOf(databaseMaxConnections);
        this.connectionsSemaphore =
                new Semaphore(databaseMaxConnectionsValue);
        this.availableConnectionsQueue =
                new ArrayBlockingQueue<>(databaseMaxConnectionsValue);
    }

    public boolean shutdown(long millis) throws AppException {
        try {
            connectionsSemaphore = new Semaphore(0);
            long timeoutMillis = System.currentTimeMillis() + millis;
            while (System.currentTimeMillis() < timeoutMillis) {
                if (grantedConnections.get() == 0) break;
                Thread.sleep(100);
            }
            return grantedConnections.get() == 0;
        } catch (InterruptedException e) {
            throw new AppException(SHUTDOWN_TIMEOUT_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

    public void releaseConnection(Connection connection)
            throws AppException {
        try {
            availableConnectionsQueue.offer((PooledConnection) connection,
                    100, TimeUnit.MILLISECONDS);
            connectionsSemaphore.release();
        } catch (InterruptedException e) {
            throw new AppException(RELEASE_CONNECTION_TIMEOUT_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        try {
            if (connectionsSemaphore.tryAcquire(100, TimeUnit.MILLISECONDS)) {
                PooledConnection pooledConnection;
                if (availableConnectionsQueue.size() == 0) {
                    pooledConnection = new PooledConnection(DriverManager.
                            getConnection(databaseUrl, databaseUsername,
                                    databasePassword));
                } else {
                    pooledConnection = availableConnectionsQueue.
                            poll(100, TimeUnit.MILLISECONDS);
                    pooledConnection.clearConnection();
                }
                return pooledConnection;
            }
        } catch (InterruptedException e) {
            throw new SQLException(e.getMessage());
        }
        throw new SQLException();
    }

    @Override
    public Connection getConnection(String username, String password)
            throws SQLException {
        throw new UnsupportedOperationException
                ("Get connection should be requested with predefined"
                        + " in properties username and password");
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
    public java.util.logging.Logger getParentLogger()
            throws SQLFeatureNotSupportedException {
        return null;
    }

}