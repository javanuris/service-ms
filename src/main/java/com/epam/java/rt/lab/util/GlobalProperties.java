package com.epam.java.rt.lab.util;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * service-ms
 */
public final class GlobalProperties {

    private static Properties globalProperties = new Properties();
    private static Lock propertiesLock = new ReentrantLock();

    private GlobalProperties() {
    }

    public static void init() {
        if (propertiesLock.tryLock()) {
            try {
                globalProperties.load(GlobalProperties.class.getClassLoader().getResourceAsStream("global.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                propertiesLock.unlock();
            }
        }
    }

    public static String getProperty(String key) {
        if (globalProperties.size() == 0) init();
        return globalProperties.getProperty(key);
    }
}
