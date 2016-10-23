package com.epam.java.rt.lab.util;

import com.epam.java.rt.lab.exception.AppException;
import org.joda.time.Days;
import org.joda.time.LocalDateTime;
import org.joda.time.Seconds;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.util.UtilExceptionCode.VALUE_OUT_OF_RANGE;

/**
 * {@code TimestampManager} class is a util class to work with
 * timestamp representation of date
 */
public final class TimestampManager {

    private static final String TIMESTAMP_REGEX =
            "^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})(\\.\\d+)?$";

    private static List<String> dateList = new ArrayList<>();
    private static List<String> timeList = new ArrayList<>();

    private TimestampManager() {
    }

    private static int daysInMonth(int year, int month) throws AppException {
        switch (month) {
            case 1:
                return 31;
            case 2:
                if (year % 4 == 0) {
                    return 29;
                } else {
                    return 28;
                }
            case 3:
                return 31;
            case 4:
                return 30;
            case 5:
                return 31;
            case 6:
                return 30;
            case 7:
                return 31;
            case 8:
                return 31;
            case 9:
                return 30;
            case 10:
                return 31;
            case 11:
                return 30;
            case 12:
                return 31;
        }
        String[] detailArray = {String.valueOf(year), String.valueOf(month)};
        throw new AppException(VALUE_OUT_OF_RANGE, detailArray);
    }

    public static void initDateList() throws AppException {
        if (TimestampManager.dateList.size() == 0) {
            for (int year = 1900; year <= 2100; year++) {
                for (int month = 1; month <= 12; month++) {
                    for (int day = 1; day <= daysInMonth(year, month); day++) {
                        StringBuilder stringDate = new StringBuilder();
                        stringDate.append(String.format("%04d", year));
                        stringDate.append(String.format("%02d", month));
                        stringDate.append(String.format("%02d", day));
                        TimestampManager.dateList.add(stringDate.toString());
                    }
                }
            }
        }
    }

    public static void initTimeList() {
        if (TimestampManager.timeList.size() == 0) {
            for (int hour = 0; hour <= 23; hour++) {
                for (int minute = 0; minute <= 59; minute++) {
                    for (int second = 0; second <= 59; second++) {
                        StringBuilder stringTime = new StringBuilder();
                        stringTime.append(String.format("%02d", hour));
                        stringTime.append(String.format("%02d", minute));
                        stringTime.append(String.format("%02d", second));
                        TimestampManager.timeList.add(stringTime.toString());
                    }
                }
            }
        }
    }

    public static boolean isTimestamp(String stringTimestamp) {
        if (stringTimestamp == null
                || !stringTimestamp.matches(TIMESTAMP_REGEX)) return false;
        String[] partArray = stringTimestamp.split(PropertyManager.SPACE);
        if (partArray.length != 2) return false;
        String date = partArray[0].replaceAll(HYPHEN, "");
        if (!TimestampManager.dateList.contains(date)) return false;
        String time = partArray[1].replaceAll(COLON, "");
        partArray = time.split(ESCAPED_POINT);
        time = partArray[0];
        return TimestampManager.timeList.contains(time);
    }

    public static Timestamp getCurrentTimestamp() {
        Date now = new Date();
        return new Timestamp(now.getTime());
    }

    private static Timestamp jodaToTimestamp(LocalDateTime localDateTime) {
        return new Timestamp(localDateTime.toDateTime().getMillis());
    }

    private static LocalDateTime timestampToJoda(Timestamp timestamp) {
        return LocalDateTime.fromDateFields(timestamp);
    }

    public static Timestamp daysToTimestamp(Timestamp timestamp, int days)
            throws AppException {
        if (timestamp == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        LocalDateTime localDateTime = timestampToJoda(timestamp);
        if (days > 0) return jodaToTimestamp(localDateTime.plusDays(days));
        if (days < 0) return jodaToTimestamp(localDateTime.minusDays(-days));
        return timestamp;
    }

    public static int daysBetweenTimestamps(Timestamp timestamp1,
                                            Timestamp timestamp2)
            throws AppException {
        if (timestamp1 == null || timestamp2 == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        LocalDateTime localDateTime1 = timestampToJoda(timestamp1);
        LocalDateTime localDateTime2 = timestampToJoda(timestamp2);
        return Days.daysBetween(localDateTime1, localDateTime2).getDays();
    }

    public static Timestamp secondsToTimestamp(Timestamp timestamp,
                                               int seconds)
            throws AppException {
        if (timestamp == null) throw new AppException(NULL_NOT_ALLOWED);
        LocalDateTime localDateTime = timestampToJoda(timestamp);
        if (seconds > 0) {
            return jodaToTimestamp(localDateTime.plusSeconds(seconds));
        } else if (seconds < 0) {
            return jodaToTimestamp(localDateTime.minusSeconds(-seconds));
        }
        return timestamp;
    }

    public static int secondsBetweenTimestamps(Timestamp timestamp1,
                                               Timestamp timestamp2)
            throws AppException {
        if (timestamp1 == null || timestamp2 == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        LocalDateTime localDateTime1 = timestampToJoda(timestamp1);
        LocalDateTime localDateTime2 = timestampToJoda(timestamp2);
        return Seconds.secondsBetween(localDateTime1, localDateTime2).
                getSeconds();
    }

}