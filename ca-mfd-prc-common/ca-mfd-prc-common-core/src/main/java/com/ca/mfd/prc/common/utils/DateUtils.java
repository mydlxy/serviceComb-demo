/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.utils;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日期处理
 *
 * @author inkelink
 */
public class DateUtils {

    private DateUtils() {
    }

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
    /**
     * 时间格式(yyyy-MM)
     */
    public static final String DATE_YEAR_MONTH = "yyyy-MM";
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyyMMdd)
     */
    public static final String DATE_PATTERN_C = "yyyyMMdd";
    /**
     * 时间格式(yyyyMMddHHmmss)
     */
    public static final String DATE_TIME_PATTERN_C = "yyyyMMddHHmmss";
    /**
     * 时间格式(yyyyMMddHHmmssSSS)
     */
    public static final String DATE_TIME_PATTERN_L = "yyyyMMddHHmmssSSS";
    /**
     * 时间格式(yyyy-MM-dd HH:mm)
     */
    public static final String DATE_TIME_HOUR_SEC = "yyyy-MM-dd HH:mm";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss.fff)
     */
    public static final String DATE_TIME_PATTERN_M = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * 时间格式(yyyy-MM-dd'T'HH:mm:ss)
     */
    public static final String DATE_TIME_PATTERN_T = "yyyy-MM-dd'T'HH:mm:ss";
    /**
     * 时间格式(yyyy-MM-dd'T'HH:mm:ss.SSSZ)
     */
    public static final String DATE_TIME_PATTERN_T_M = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    /**
     * 时间格式集合
     */
    public static final ImmutableList<String> FORMAT_LIST = ImmutableList.of(DATE_YEAR_MONTH, DATE_PATTERN, DATE_TIME_HOUR_SEC, DATE_TIME_PATTERN, DATE_TIME_PATTERN_T, DATE_TIME_PATTERN_T_M);
    private static final String DEL_TIME_STR = "1970-01";
    private static final Integer DATE_FULL_LEN = 10;
    private static final Integer DATE_YEAR_MONTH_LEN = 7;

//    static {
//        FORMAT_LIST.add(DATE_YEAR_MONTH);
//        FORMAT_LIST.add(DATE_PATTERN);
//        FORMAT_LIST.add(DATE_TIME_HOUR_SEC);
//        FORMAT_LIST.add(DATE_TIME_PATTERN);
//        FORMAT_LIST.add(DATE_TIME_PATTERN_T);
//        FORMAT_LIST.add(DATE_TIME_PATTERN_T_M);
//    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date 日期
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date    日期
     * @param pattern 格式，如：DateUtils.DATE_TIME_PATTERN
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date, String pattern) {
        if (date != null) {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern(pattern);
            return formatter.format(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
        }
        return null;
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String now() {
        return format(new Date(), DATE_TIME_PATTERN_M);
    }

    /**
     * 过滤日期中中1970-01的时间
     *
     * @return 返回格式日期
     */
    public static String replaceDefTime(String val) {
        if (StringUtils.isBlank(val)) {
            return val;
        }
        val = val.trim();
        if (val.length() >= DATE_FULL_LEN && DEL_TIME_STR.equals(val.substring(0, DATE_YEAR_MONTH_LEN))) {
            val = DateUtils.format(new Date(), DateUtils.DATE_PATTERN) + " " + val.substring(DATE_FULL_LEN).trim();
        }
        return val;
    }

    /**
     * 日期解析
     *
     * @param date    日期
     * @param pattern 格式，如：DateUtils.DATE_TIME_PATTERN
     * @return 返回Date
     */
    public static Date parse(String date, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 日期解析
     *
     * @param val 日期
     * @return 返回Date
     */
    public static Date parse(String val) {
        Date parsedDate = null;

        try {
            if (StringUtils.isNotBlank(val)) {
                if (val.matches("^\\d{4}-\\d{1,2}$")) {
                    parsedDate = parse(val, DATE_YEAR_MONTH);
                } else if (val.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
                    parsedDate = parse(val, DATE_PATTERN);
                } else if (val.matches("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}$")) {
                    parsedDate = parse(val, DATE_TIME_HOUR_SEC);
                } else if (val.matches("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                    parsedDate = parse(val, DATE_TIME_PATTERN);
                } else if (val.matches("^\\d{4}-\\d{1,2}-\\d{1,2}.*T.*\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                    parsedDate = parse(val, DATE_TIME_PATTERN_T);
                } else if (val.matches("^\\d{4}-\\d{1,2}-\\d{1,2}.*T.*\\d{1,2}:\\d{1,2}:\\d{1,2}.*.+$")) {
                    parsedDate = parse(val, DATE_TIME_PATTERN_T_M);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return parsedDate;
    }

    /**
     * 字符串转换成日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期的格式，如：DateUtils.DATE_TIME_PATTERN
     */
    public static Date stringToDate(String strDate, String pattern) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        }

        DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
        return fmt.parseLocalDateTime(strDate).toDate();
    }

    /**
     * 根据周数，获取开始日期、结束日期
     *
     * @param week 周期  0本周，-1上周，-2上上周，1下周，2下下周
     * @return 返回date[0]开始日期、date[1]结束日期
     */
    public static Date[] getWeekStartAndEnd(int week) {
        DateTime dateTime = new DateTime();
        LocalDate date = new LocalDate(dateTime.plusWeeks(week));

        date = date.dayOfWeek().withMinimumValue();
        Date beginDate = date.toDate();
        Date endDate = date.plusDays(6).toDate();
        return new Date[]{beginDate, endDate};
    }

    /**
     * 获取日期是当年第几天
     *
     * @param date 日期
     */
    public static Integer getDayOfYear(Date date) {
        DateTime dateTime = new DateTime(date);
        LocalDate lcdate = new LocalDate(dateTime);
        return lcdate.dayOfYear().get();
    }


    /**
     * 对日期的【秒】进行加/减
     *
     * @param date    日期
     * @param seconds 秒数，负数为减
     * @return 加/减几秒后的日期
     */
    public static Date addDateSeconds(Date date, int seconds) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusSeconds(seconds).toDate();
    }

    /**
     * 对日期的【分钟】进行加/减
     *
     * @param date    日期
     * @param minutes 分钟数，负数为减
     * @return 加/减几分钟后的日期
     */
    public static Date addDateMinutes(Date date, int minutes) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(minutes).toDate();
    }

    /**
     * 对日期的【小时】进行加/减
     *
     * @param date  日期
     * @param hours 小时数，负数为减
     * @return 加/减几小时后的日期
     */
    public static Date addDateHours(Date date, int hours) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusHours(hours).toDate();
    }

    /**
     * 对日期的【天】进行加/减
     *
     * @param date 日期
     * @param days 天数，负数为减
     * @return 加/减几天后的日期
     */
    public static Date addDateDays(Date date, int days) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusDays(days).toDate();
    }

    /**
     * 对日期的【周】进行加/减
     *
     * @param date  日期
     * @param weeks 周数，负数为减
     * @return 加/减几周后的日期
     */
    public static Date addDateWeeks(Date date, int weeks) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusWeeks(weeks).toDate();
    }

    /**
     * 对日期的【月】进行加/减
     *
     * @param date   日期
     * @param months 月数，负数为减
     * @return 加/减几月后的日期
     */
    public static Date addDateMonths(Date date, int months) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMonths(months).toDate();
    }

    /**
     * 对日期的【年】进行加/减
     *
     * @param date  日期
     * @param years 年数，负数为减
     * @return 加/减几年后的日期
     */
    public static Date addDateYears(Date date, int years) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusYears(years).toDate();
    }

    /**
     * 计算两个时间相差的秒数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 相差的总秒数
     * @throws ParseException 异常
     */
    public static long getTimeTotalSeconds(String startTime, String endTime) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN);
        long eTime = df.parse(endTime).getTime();
        long sTime = df.parse(startTime).getTime();
        return (eTime - sTime) / 1000;
    }

    /**
     * 计算两个时间之间的天数差
     *
     * @param beforeDay 开始时间
     * @param afterDay  结束时间
     * @return 相差天数
     */
    public static long getTwoDateDays(Date beforeDay, Date afterDay) {
        SimpleDateFormat sm = new SimpleDateFormat(DATE_TIME_PATTERN);
        long days = -1;
        try {
            days =
                    (sm.parse(sm.format(afterDay)).getTime() - sm.parse(sm.format(beforeDay)).getTime())
                            / (1000 * 3600 * 24);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return days;
    }

    /**
     * 特殊类型时间转换 无法确定源是否合法
     *
     * @param source 时间源
     * @param format 格式
     * @return 返回结果(转换失败返回空)
     */
    public static Date tryParseExact(String source, String format) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(source);
        } catch (ParseException e) {
            return null;
        }
    }
}
