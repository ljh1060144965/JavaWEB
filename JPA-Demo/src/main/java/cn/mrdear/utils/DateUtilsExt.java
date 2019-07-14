package cn.mrdear.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.time.*;
import java.util.Calendar;
import java.util.Date;

/****
 * <pre>类名: DateUtilsExt</pre>
 * <pre>描述: /**
 *   TODO 修改使用LocalDate, LocalTime, LocalDateTime 代替
 *   扩展 {@link org.apache.commons.lang3.time.DateUtils}，以便于程序的开发。
 *  </pre>
 * <pre>版权: lijiahui</pre>
 * <pre>日期: 2019/7/14 20:35</pre>
 * @author lijiahui
 */
public class DateUtilsExt extends DateUtils {

    /**
     * java.util.Date --> java.time.LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime;
    }

    /**
     * java.util.Date --> java.time.LocalDate
     */
    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate;
    }

    /**
     * java.util.Date --> java.time.LocalTime
     */
    public static LocalTime dateToLocalTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalTime localTime = localDateTime.toLocalTime();
        return localTime;
    }


    /**
     * java.time.LocalDateTime --> java.util.Date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }


    /**
     * java.time.LocalDate --> java.util.Date
     */
    public static Date localDateToDate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /**
     * java.time.LocalTime --> java.util.Date
     */
    public static Date localTimeToDate(LocalTime localTime) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /** 是否月初
     * @param date
     * @return
     */
    public static boolean isFirstDayOfMonth(Date date) {
        return toCalendar(date).get(Calendar.DATE) == 1;
    }

    /** 是否季度初
     * @param date
     * @return
     */
    public static boolean isFirstDayOfQuarter (Date date) {
        Calendar cal = toCalendar(date);
        return (cal.get(Calendar.DATE) == 1) && (cal.get(Calendar.MONTH) % 3 == 0);
    }

    /** 季度第一天
     * @param date
     * @return
     */
    public static Date getFirstDayOfQuarter(Date date) {
        Calendar cal = toCalendar(date);
        int mon = cal.get(Calendar.MONTH);

        // 第1季度
        if (mon >= Calendar.JANUARY && mon <= Calendar.MARCH) {
            cal.set(Calendar.MONTH, Calendar.JANUARY);
        }
        // 第2季度
        if (mon >= Calendar.APRIL && mon <= Calendar.JUNE) {
            cal.set(Calendar.MONTH, Calendar.APRIL);
        }
        // 第3季度
        if (mon >= Calendar.JULY && mon <= Calendar.SEPTEMBER) {
            cal.set(Calendar.MONTH, Calendar.JULY);
        }
        // 第4季度
        if (mon >= Calendar.OCTOBER && mon <= Calendar.DECEMBER) {
            cal.set(Calendar.MONTH, Calendar.OCTOBER);
        }
        cal.set(Calendar.DAY_OF_MONTH, 1);

        return cal.getTime();
    }

    /** 季度最后一天
     * @param date
     * @return
     */
    public static Date getLastDayOfQuarter(Date date) {
        Calendar cal = toCalendar(date);
        int mon = cal.get(Calendar.MONTH);

        // 第1季度
        if (mon >= Calendar.JANUARY && mon <= Calendar.MARCH) {
            cal.set(Calendar.MONTH, Calendar.MARCH);
            cal.set(Calendar.DAY_OF_MONTH, 31);
        }
        // 第2季度
        if (mon >= Calendar.APRIL && mon <= Calendar.JUNE) {
            cal.set(Calendar.MONTH, Calendar.JUNE);
            cal.set(Calendar.DAY_OF_MONTH, 30);
        }
        // 第3季度
        if (mon >= Calendar.JULY && mon <= Calendar.SEPTEMBER) {
            cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
            cal.set(Calendar.DAY_OF_MONTH, 30);
        }
        // 第4季度
        if (mon >= Calendar.OCTOBER && mon <= Calendar.DECEMBER) {
            cal.set(Calendar.MONTH, Calendar.DECEMBER);
            cal.set(Calendar.DAY_OF_MONTH, 31);
        }

        return cal.getTime();
    }

    /** 是否年初
     * @param date
     * @return
     */
    public static boolean isFirstDayOfYear(Date date) {
        Calendar cal = toCalendar(date);
        return cal.get(Calendar.MONTH) == Calendar.JANUARY && cal.get(Calendar.DATE) == 1;
    }

    /** 年初第一天
     * @param date
     * @return
     */
    public static Date getFirstDayOfYear(Date date) {
        Calendar cal = toCalendar(date);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        return cal.getTime();
    }
}
