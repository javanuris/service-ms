package com.epam.java.rt.lab.util;

import org.joda.time.Days;
import org.joda.time.LocalDateTime;
import org.joda.time.Seconds;

import java.sql.Timestamp;
import java.util.Date;

/**
 * {@code TimestampManager} class is a util class to work with timestamp representation of date
 */
public final class TimestampManager {

    private TimestampManager() {
    }

    public static Timestamp getCurrentTimestamp() {
        Date now = new Date();
        return new Timestamp(now.getTime());
    }

    private static Timestamp jodaToTimestamp(LocalDateTime localDateTime) {
//        if (localDateTime == null) throw new UtilException("");
        return new Timestamp(localDateTime.toDateTime().getMillis());
    }

    private static LocalDateTime timestampToJoda(Timestamp timestamp) {

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

    public static Timestamp secondsToTimestamp(Timestamp timestamp, int seconds) {
        LocalDateTime localDateTime = timestampToJoda(timestamp);
        if (seconds > 0) return jodaToTimestamp(localDateTime.plusSeconds(seconds));
        if (seconds < 0) return jodaToTimestamp(localDateTime.minusSeconds(-seconds));
        return timestamp;
    }

    public static int secondsBetweenTimestamps(Timestamp timestamp1, Timestamp timestamp2) {
        LocalDateTime localDateTime1 = timestampToJoda(timestamp1);
        LocalDateTime localDateTime2 = timestampToJoda(timestamp2);
        return Seconds.secondsBetween(localDateTime1, localDateTime2).getSeconds();
    }

    public static Timestamp valueOf(String stringTimestamp) {
        try {
            return Timestamp.valueOf(stringTimestamp);
        } catch (ClassCastException e) {
            //
            return null;
        }
    }

    public static Date getDate(String stringTimestamp) {
        Timestamp timestamp = valueOf(stringTimestamp);
        return timestamp == null ? null
                                 : new Date(timestamp.getTime());
    }

}
