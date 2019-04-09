package com.yq.kernel.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/**
 * <p>Date与 java 8 LocalDateTime LocalDate 之间的互相转化 工具方法</p>
 * @author panliyong  2017/9/4 18:47
 */
public class DateUtils {

    /**
     * <p>java.util.Date --> java.time.LocalDateTime</p>
     * @param date  需要转换的 时间
     * @return java.time.LocalDateTime
     * @author panliyong  2018/8/1 10:03
     */
    public static LocalDateTime date2LocalDataTime(Date date) {
        if (date != null) {
            Instant instant = date.toInstant();
            ZoneId zone = ZoneId.systemDefault();
            return LocalDateTime.ofInstant(instant, zone);
        } else {
            return null;
        }
    }

    /**
     * <p>java.time.LocalDateTime --> java.util.Date</p>
     * @param localDateTime 需要转换的 时间
     * @return java.util.Date
     * @author panliyong  2018/8/1 10:02
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = localDateTime.atZone(zone).toInstant();
            return Date.from(instant);
        } else {
            return null;
        }
    }

    /**
     * <p> 比较两个LocalTime的大小,大于等于返回true</p>
     * @param bigTime   大时间
     * @param smallTime 小时间
     * @return boolean
     * @author youq  2018/3/9 20:02
     */
    public static boolean compareTime(LocalTime bigTime, LocalTime smallTime) {
        int compare = bigTime.compareTo(smallTime);
        return compare >= 0;
    }

    /**
     * <p>java.util.Date --> java.time.LocalDate</p>
     * @author panliyong  2018/1/16 10:04
     */
    public static LocalDate date2LocalDate(Date date) {
        if (date != null) {
            Instant instant = date.toInstant();
            ZoneId zone = ZoneId.systemDefault();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
            return localDateTime.toLocalDate();
        } else {
            return null;
        }
    }

    /**
     * <p> java.time.LocalDate --> java.util.Date</p>
     * @author panliyong  2018/1/16 10:08
     */
    public static Date localDate2Date(LocalDate localDate) {
        if (localDate != null) {
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
            return Date.from(instant);
        } else {
            return null;
        }
    }

    /**
     * <p>java.time.LocalTime --> java.util.Date</p>
     * @author panliyong  2018/1/16 10:10
     */
    public static Date localDateTimeAndLocalTime2date(LocalDate localDate, LocalTime localTime) {
        if (localDate != null && localTime != null) {
            LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = localDateTime.atZone(zone).toInstant();
            return Date.from(instant);
        } else {
            return null;
        }
    }

    /**
     * <p> LocalDateTime转换成时间戳（毫秒）</p>
     * @author youq  2018/3/15 10:17
     */
    public static long localDateTimeToMillisecond(LocalDateTime localDateTime) {
        if (localDateTime != null)
            return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        else
            return 0;
    }

    /**
     * <p>long 秒值 --> LocalDateTime</p>
     * @author panliyong  2018/1/16 10:10
     */
    public static LocalDateTime second2LocalDateTime(Long seconds) {
        if (seconds > 0) {
            return LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds), ZoneOffset.of("+8"));
        } else {
            return null;
        }
    }

    /**
     * <p>long 毫秒 --> LocalDateTime</p>
     * @author panliyong  2018/1/16 10:10
     */
    public static LocalDateTime millisecond2LocalDateTime(Long millisecond) {
        if (millisecond > 0) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(millisecond), ZoneOffset.of("+8"));
        } else {
            return null;
        }
    }

    /**
     * <p>LocalDateTime --> long 秒值 </p>
     * @author panliyong  2018/1/16 10:10
     */
    public static long localDateTime2Second(LocalDateTime time) {
        if (time != null) {
            return time.toEpochSecond(ZoneOffset.of("+8"));
        } else {
            return 0;
        }
    }

    /**
     * <p>localDateTime 2 String </p>
     * @param time    需要转化的时间
     * @param pattern 转化的 格式
     * @return java.lang.String
     * @author panliyong  2018/7/4 16:00
     */
    public static String localDateTimeFormat(LocalDateTime time, String pattern) {
        if (Objects.nonNull(time)) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            return df.format(time);
        } else return "";
    }

    /**
     * <p>localDate  2 String </p>
     * @param date    需要转化的日期
     * @param pattern 转化的 格式
     * @return java.lang.String
     * @author panliyong  2018/7/4 16:00
     */
    public static String localDateTimeFormat(LocalDate date, String pattern) {
        if (Objects.nonNull(date)) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            return df.format(date);
        } else return "";
    }

    /**
     * <p>localTime 2 String </p>
     * @param time    需要转化的时间
     * @param pattern 转化的 格式
     * @return java.lang.String
     * @author panliyong  2018/7/4 16:00
     */
    public static String localTimeFormat(LocalTime time, String pattern) {
        if (Objects.nonNull(time)) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            return df.format(time);
        } else return "";
    }

    /**
     * <p>localDateTime 2 yyyy-MM-dd HH:mm:ss String </p>
     * @param time 需要转化的时间
     * @return java.lang.String
     * @author panliyong  2018/7/4 16:00
     */
    public static String localDateTimeDefaultFormat(LocalDateTime time) {
        if (Objects.nonNull(time)) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return df.format(time);
        } else return "";
    }

    /**
     * <p>localDate 2 yyyy-MM-dd  String </p>
     * @param date 需要转化的日期
     * @return java.lang.String
     * @author panliyong  2018/7/4 16:00
     */
    public static String localDateDefaultFormat(LocalDate date) {
        if (Objects.nonNull(date)) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return df.format(date);
        } else return "";
    }

    /**
     * <p>localDate 2  HH:mm:ss String </p>
     * @param time 需要转化的时间
     * @return java.lang.String
     * @author panliyong  2018/7/4 16:00
     */
    public static String localTimeDefaultFormat(LocalTime time) {
        if (Objects.nonNull(time)) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss");
            return df.format(time);
        } else return "";
    }

    /**
     * <p>yyyy-MM-dd HH:mm:ss String to LocalDateTime</p>
     * @param dateTimeStr 需要转化的时间
     * @return java.lang.String
     * @author panliyong  2018/7/4 16:00
     */
    public static LocalDateTime string2LocalDateTime(String dateTimeStr) {
        try {
            if (Objects.nonNull(dateTimeStr)) {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return LocalDateTime.parse(dateTimeStr, df);
            } else return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * <p>yyyy-MM-dd HH:mm:ss String to LocalDateTime</p>
     * @param dateTimeStr 需要转化的时间
     * @return java.lang.String
     * @author panliyong  2018/7/4 16:00
     */
    public static LocalDateTime string2LocalDateTime(String dateTimeStr , String pattern) {
        try {
            if (Objects.nonNull(dateTimeStr)) {
                DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
                return LocalDateTime.parse(dateTimeStr, df);
            } else return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalTime string2LocalTime(String dateTimeStr , String pattern) {
        try {
            if (Objects.nonNull(dateTimeStr)) {
                DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
                return LocalTime.parse(dateTimeStr, df);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDate string2LocalDate(String dateDateStr) {
        try {
            if (Objects.nonNull(dateDateStr)) {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return LocalDate.parse(dateDateStr, df);
            } else return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalTime second2LocalTime(Long seconds) {
        return LocalTime.ofSecondOfDay(seconds);
    }

    /**
     * 获取时间差
     * @param end
     * @param start
     * @return
     */
    public static long getLongTime(LocalDateTime end, LocalDateTime start) {
        return Duration.between(start, end).getSeconds();
    }

}
