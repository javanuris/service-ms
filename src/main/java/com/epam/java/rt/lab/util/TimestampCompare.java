package com.epam.java.rt.lab.util;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDateTime;
import org.joda.time.Seconds;

import java.sql.Timestamp;
import java.util.Date;

/**
 * service-ms
 */
public class TimestampCompare {

    public static Timestamp getCurrentTimestamp() {
        Date now = new Date();
        return new Timestamp(now.getTime());
    }

    public static Timestamp jodaToTimestamp(LocalDateTime localDateTime) {
        return new Timestamp(localDateTime.toDateTime().getMillis());
    }

    public static LocalDateTime timestampToJoda(Timestamp timestamp) {
        return LocalDateTime.fromDateFields(timestamp);
    }

    public static Timestamp daysToTimestamp(Timestamp timestamp, int days) {
        LocalDateTime localDateTime = timestampToJoda(timestamp);
        if (days > 0) return jodaToTimestamp(localDateTime.plusDays(days));
        if (days < 0) return jodaToTimestamp(localDateTime.minusDays(-days));
        return timestamp;
    }

    public static int daysBetweenTimestamps(Timestamp timestamp1, Timestamp timestamp2) {
        LocalDateTime localDateTime1 = timestampToJoda(timestamp1);
        LocalDateTime localDateTime2 = timestampToJoda(timestamp2);
        return Days.daysBetween(localDateTime1, localDateTime2).getDays();
    }

    public static int secondsBetweenTimestamps(Timestamp timestamp1, Timestamp timestamp2) {
        LocalDateTime localDateTime1 = timestampToJoda(timestamp1);
        LocalDateTime localDateTime2 = timestampToJoda(timestamp2);
        return Seconds.secondsBetween(localDateTime1, localDateTime2).getSeconds();
    }

}
