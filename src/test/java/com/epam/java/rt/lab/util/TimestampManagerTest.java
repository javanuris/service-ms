package com.epam.java.rt.lab.util;

import com.epam.java.rt.lab.exception.AppException;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class TimestampManagerTest {

    private static final String TIMESTAMP = "2010-10-20 00:35:08";
    private static final String TIMESTAMP_ERR = "2010-13-20 00:35:23";
    private static final String TIMESTAMP_PLUS_ONE_DAY =
            "2010-10-21 00:35:08.0";
    private static final String TIMESTAMP_MINUS_ONE_DAY =
            "2010-10-19 00:35:08.0";
    private static final String TIMESTAMP_PLUS_TEN_SECONDS =
            "2010-10-20 00:35:18.0";
    private static final String TIMESTAMP_MINUS_TEN_SECONDS =
            "2010-10-20 00:34:58.0";

    @Before
    public void setUp() throws Exception {
        PropertyManager.initGlobalProperties();
        AppException.initExceptionBundle();
        TimestampManager.initDateList();
        TimestampManager.initTimeList();
    }

    @Test
    public void isTimestamp() throws Exception {
        assertTrue("isTimestamp() failed", TimestampManager.
                isTimestamp(TIMESTAMP));
        assertFalse("isTimestamp() failed", TimestampManager.
                isTimestamp(TIMESTAMP_ERR));
    }

    @Test
    public void getCurrentTimestamp() throws Exception {
        assertTrue("getCurrentTimestamp() failed",
                TimestampManager.isTimestamp(TimestampManager.
                        getCurrentTimestamp().toString()));
    }

    @Test
    public void daysToTimestamp() throws Exception {
        Timestamp timestamp = Timestamp.valueOf(TIMESTAMP);
        assertEquals("daysToTimestamp() failed", TIMESTAMP_PLUS_ONE_DAY,
                TimestampManager.daysToTimestamp(timestamp, 1).toString());
        assertEquals("daysToTimestamp() failed", TIMESTAMP_MINUS_ONE_DAY,
                TimestampManager.daysToTimestamp(timestamp, -1).toString());
    }

    @Test
    public void daysBetweenTimestamps() throws Exception {
        Timestamp timestamp1 = Timestamp.valueOf(TIMESTAMP_MINUS_ONE_DAY);
        Timestamp timestamp2 = Timestamp.valueOf(TIMESTAMP_PLUS_ONE_DAY);
        assertEquals("daysBetweenTimestamps() failed", 2,
                TimestampManager.daysBetweenTimestamps(timestamp1, timestamp2));
    }

    @Test
    public void secondsToTimestamp() throws Exception {
        Timestamp timestamp = Timestamp.valueOf(TIMESTAMP);
        assertEquals("secondsToTimestamp() failed", TIMESTAMP_PLUS_TEN_SECONDS,
                TimestampManager.secondsToTimestamp(timestamp, 10).toString());
        assertEquals("secondsToTimestamp() failed", TIMESTAMP_MINUS_TEN_SECONDS,
                TimestampManager.secondsToTimestamp(timestamp, -10).toString());
    }

    @Test
    public void secondsBetweenTimestamps() throws Exception {
        Timestamp timestamp1 = Timestamp.valueOf(TIMESTAMP_MINUS_TEN_SECONDS);
        Timestamp timestamp2 = Timestamp.valueOf(TIMESTAMP_PLUS_TEN_SECONDS);
        assertEquals("secondsBetweenTimestamps() failed", 20,
                TimestampManager.secondsBetweenTimestamps(timestamp1, timestamp2));
    }

}