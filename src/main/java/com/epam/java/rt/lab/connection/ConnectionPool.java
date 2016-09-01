package com.epam.java.rt.lab.connection;

import com.epam.java.rt.lab.component.NavbarComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * service-ms
 */
public class ConnectionPool implements DataSource {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
    private static String databaseUrl;
    private static String databaseUsername;
    private static String databasePassword;
    private static int databaseMaxConnections;
    private static Semaphore maxConnectionsSemaphore;
    private BlockingQueue<PooledConnection> availableConnectionsQueue;

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
        try {
            databaseUrl = null;
            databaseUsername = null;
            databasePassword = null;
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
                } else if (partArray[0].equals("database.max-connection")) {
                    databaseMaxConnections = Integer.valueOf(partArray[1]);
                    maxConnectionsSemaphore = new Semaphore(databaseMaxConnections);
                    availableConnectionsQueue = new ArrayBlockingQueue<>(databaseMaxConnections);
                }
            }
        } catch (IOException e) {
            throw new ConnectionException(e.getMessage());
        }
        if (databaseUrl == null || databaseUsername == null || databasePassword == null ||
                databaseMaxConnections == 0)
            throw new ConnectionException("Database properties error");
    }

    @Override
    public Connection getConnection() throws SQLException {
        try {
            if (maxConnectionsSemaphore.tryAcquire(100, TimeUnit.MILLISECONDS)) {
                if (availableConnectionsQueue.size() == 0)
                return availableConnectionsQueue.poll(100, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            logger.error("Get connection interrupted" , e);
            throw new SQLException();
        }
        return null;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new UnsupportedOperationException
                ("Get connection should be requested with predefined in properties username and password");
    }

    void releaseConnection(PooledConnection pooledConnection) throws SQLException {
//        try {
//            this.availableConnectionQueue.offer(pooledConnection, 100, TimeUnit.MILLISECONDS);
//            this.grantedConnectionQueue.remove(pooledConnection);
//            logger.info("Granted connection released (total granted: {}, total available: {})",
//                    this.grantedConnectionQueue.size(), this.availableConnectionQueue.size());
//
//        } catch (InterruptedException e) {
//            logger.error("Release connection interrupted" , e);
//            throw new SQLException();
//        }
    }

    public void shutdown() {
//        logger.info("Shutdown initiated");
//        this.shutdownPool.set(true);
//        PooledConnection pooledConnection;
//        do {
//            pooledConnection = this.availableConnectionQueue.poll();
//        }
//        while (this.grantedConnectionQueue.size() > 0);
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
