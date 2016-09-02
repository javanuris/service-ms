package com.epam.java.rt.lab.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
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
    private static int databaseMaxConnections;
    private Semaphore connectionsSemaphore;
    private BlockingQueue<PooledConnection> availableConnectionsQueue;
    private AtomicInteger grantedConnections = new AtomicInteger();

    private ConnectionPool() {
    }

    public static ConnectionPool getInstance() throws ConnectionException {
        ConnectionPool instance = InstanceHolder.INSTANCE;
        if (databaseUrl == null || databaseUsername == null || databasePassword == null ||
                databaseMaxConnections == 0)
            instance.resetProperties("database.properties");
        return instance;
    }

    public void resetProperties(String fileName) throws ConnectionException {
        String databaseUrl = null;
        String databaseUsername = null;
        String databasePassword = null;
        String databaseMaxConnections = null;
        try {
            InputStream inputStream = ConnectionPool.class.getClassLoader().getResourceAsStream(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String readLine;
            String[] partArray;
            while ((readLine = bufferedReader.readLine()) != null) {
                partArray = readLine.split("=");
                if (partArray[0].equals("database.url")) {
                    databaseUrl = partArray[1];
                } else if (partArray[0].equals("database.username")) {
                    databaseUsername = partArray[1];
                } else if (partArray[0].equals("database.password")) {
                    databasePassword = partArray[1];
                } else if (partArray[0].equals("database.max-connections")) {
                    databaseMaxConnections = partArray[1];
                }
            }
        } catch (IOException e) {
            throw new ConnectionException(e.getMessage());
        }
        if (databaseUrl == null || databaseUsername == null || databasePassword == null ||
                databaseMaxConnections == null)
            throw new ConnectionException("Database properties error");
        while (!shutdown(10000)) ;
        ConnectionPool.databaseUrl = databaseUrl;
        ConnectionPool.databaseUsername = databaseUsername;
        ConnectionPool.databasePassword = databasePassword;
        ConnectionPool.databaseMaxConnections = Integer.valueOf(databaseMaxConnections);
        this.availableConnectionsQueue = new ArrayBlockingQueue<>(ConnectionPool.databaseMaxConnections);
        this.connectionsSemaphore = new Semaphore(ConnectionPool.databaseMaxConnections);
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

    public void releaseConnection(PooledConnection pooledConnection) throws ConnectionException {
        try {
            availableConnectionsQueue.offer(pooledConnection, 100, TimeUnit.MILLISECONDS);
            connectionsSemaphore.release();
        } catch (InterruptedException e) {
            throw new ConnectionException(e.getMessage());
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        try {
            if (connectionsSemaphore.tryAcquire(100, TimeUnit.MILLISECONDS)) {
                PooledConnection pooledConnection = availableConnectionsQueue.poll(100, TimeUnit.MILLISECONDS);
                pooledConnection = new PooledConnection
                        (DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword));
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
