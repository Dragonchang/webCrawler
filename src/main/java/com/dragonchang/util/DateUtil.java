package com.dragonchang.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * date util
 *
 * @author xuxueli 2018-08-19 01:24:11
 */
public class DateUtil {

    // ---------------------- format parse ----------------------
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_TIME_FORMAT_SECOND = "yyyyMMddHHmmss";
    private static final ThreadLocal<Map<String, DateFormat>> dateFormatThreadLocal = new ThreadLocal<Map<String, DateFormat>>();

    public static int getCurrentQuarter() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.MONTH) / 3 + 1;
    }

    public static String localDateTimeFormat(LocalDateTime localDateTime,String pattern){
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
    private static DateFormat getDateFormat(String pattern) {
        if (pattern==null || pattern.trim().length()==0) {
            throw new IllegalArgumentException("pattern cannot be empty.");
        }

        Map<String, DateFormat> dateFormatMap = dateFormatThreadLocal.get();
        if(dateFormatMap!=null && dateFormatMap.containsKey(pattern)){
            return dateFormatMap.get(pattern);
        }

        synchronized (dateFormatThreadLocal) {
            if (dateFormatMap == null) {
                dateFormatMap = new HashMap<String, DateFormat>();
            }
            dateFormatMap.put(pattern, new SimpleDateFormat(pattern));
            dateFormatThreadLocal.set(dateFormatMap);
        }

        return dateFormatMap.get(pattern);
    }

    /**
     * format datetime. like "yyyy-MM-dd"
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String formatDate(Date date) {
        return format(date, DATE_FORMAT);
    }

    /**
     * format date. like "yyyy-MM-dd HH:mm:ss"
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String formatDateTime(Date date) {
        return format(date, DATETIME_FORMAT);
    }

    public static String formatLocalDateTime(LocalDateTime date) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        return df.format(date);
    }

    /**
     * format date
     *
     * @param date
     * @param patten
     * @return
     * @throws ParseException
     */
    public static String format(Date date, String patten) {
        return getDateFormat(patten).format(date);
    }

    /**
     * parse date string, like "yyyy-MM-dd HH:mm:s"
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String dateString){
        return parse(dateString, DATE_FORMAT);
    }

    /**
     * parse datetime string, like "yyyy-MM-dd HH:mm:ss"
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date parseDateTime(String dateString) {
        return parse(dateString, DATETIME_FORMAT);
    }

    /**
     * parse date
     *
     * @param dateString
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateString, String pattern) {
        try {
            Date date = getDateFormat(pattern).parse(dateString);
            return date;
        } catch (Exception e) {
            logger.warn("parse date error, dateString = {}, pattern={}; errorMsg = {}", dateString, pattern, e.getMessage());
            return null;
        }
    }


    // ---------------------- add date ----------------------

    public static Date addYears(final Date date, final int amount) {
        return add(date, Calendar.YEAR, amount);
    }

    public static Date addMonths(final Date date, final int amount) {
        return add(date, Calendar.MONTH, amount);
    }

    public static Date addDays(final Date date, final int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }

    public static Date addHours(final Date date, final int amount) {
        return add(date, Calendar.HOUR_OF_DAY, amount);
    }

    public static Date addMinutes(final Date date, final int amount) {
        return add(date, Calendar.MINUTE, amount);
    }

    public static LocalDateTime getCurrentStartTime() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Date cur = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH,0);

        //一天的开始时间 yyyy:MM:dd 00:00:00
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return LocalDateTime.parse(formatDateTime(calendar.getTime()),df);
    }

    public static LocalDateTime getCurrentEndTime() {
        Date cur = new Date();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH,0);

        //一天的结束时间 yyyy:MM:dd 23:59:59
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        return LocalDateTime.parse(formatDateTime(calendar.getTime()),df);
    }

    public  static  LocalDateTime dateTimeToLocal(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    private static Date add(final Date date, final int calendarField, final int amount) {
        if (date == null) {
            return null;
        }
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    /**
     * 20201203 -->2020-12-03
     * @param time
     * @return
     */
    public static LocalDateTime strToLocalDateTime(String time) {
        if(time.length()<8) {
            return null;
        }
        StringBuilder builder = new StringBuilder(time);
        builder.insert(4,"-");
        builder.insert(7,"-");
        builder.append(" 00:00:01");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(builder.toString(), df);
    }
    public static Date getDateBefore(Date d,int day){
        Calendar now =Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE,now.get(Calendar.DATE)-day);
        return now.getTime();
    }

    public static boolean isMonday(Date todayDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(todayDate);
        int w = cal.get(Calendar.DAY_OF_WEEK)-1;
        if(w==0) w=7;
        if(w == 1) {
            return true;
        }
        return false;
    }


    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static float int2float(int value) {
        // 将int类型转换为float类型并除以10
        float result = (float) value / 10;
        // 使用Math.round()方法对结果进行四舍五入，并保留一位小数
        result = (float) Math.round(result * 10) / 10;
        return result;
    }

    public static int float2int(float voltage) {
        if (voltage < 0 || voltage > 25.5) {
            return -1;
        }
        // 转换为字符串并处理尾部0和小数点
        String numStr = String.format("%.10f", voltage)
                .replaceAll("0+$", "") // 去除小数部分尾部的0
                .replaceAll("\\.$", ""); // 若只剩小数点则去除

        // 分割整数和小数部分
        String[] parts = numStr.split("\\.");

        // 无小数部分（整数）或小数部分长度 <=1，直接返回原数
        if (parts.length < 2 || parts[1].length() <= 1) {
            return (int) voltage;
        } else {
            // 小数部分长度 >1，保留1位小数（四舍五入）
            float convertValue = (float) Math.round(voltage * 10) / 10;
            int setValue = (int) (convertValue * 10);
            return setValue ;
        }
    }

    private static final String TAG = "StringToIntConverter";
    // 匹配x数字y数字格式的正则表达式（如x1y2、x10y20）
    private static final Pattern PATTERN = Pattern.compile("x(\\d+)y(\\d+)");

    /**
     * 将x1y2格式的字符串列表转换为整数列表（x后的数字乘以y后的数字）
     * @param stringList 输入的字符串列表，格式如["x1y2", "x3y4"]
     * @return 转换后的整数列表，如[2, 12]
     */
    public static List<Integer> convert(List<String> stringList) {
        List<Integer> resultList = new ArrayList<>();
        if (stringList == null || stringList.isEmpty()) {
            //Log.w(TAG, "输入列表为空");
            return resultList;
        }

        for (String str : stringList) {
            try {
                Matcher matcher = PATTERN.matcher(str);
                if (matcher.matches()) {
                    // 提取x后面的数字和y后面的数字
                    int x = Integer.parseInt(matcher.group(1));
                    int y = Integer.parseInt(matcher.group(2));
                    // 计算乘积并添加到结果列表
                    resultList.add(x * y);
                } else {
                    //Log.e(TAG, "字符串格式不正确: " + str);
                    // 可根据需求选择是否添加默认值，如-1表示格式错误
                    // resultList.add(-1);
                }
            } catch (NumberFormatException e) {
                //Log.e(TAG, "数字解析失败: " + str, e);
                // 可根据需求选择是否添加默认值
                // resultList.add(-1);
            }
        }

        return resultList;
    }
    public static void main(String[] args) {

        List<String> stringList = new ArrayList<>();
        stringList.add("x3y4");
        System.out.println(">>>"+float2int(25.4f));
        System.out.println(">>>"+float2int(25.4111f));
        System.out.println(">>>"+float2int(0.4f));
        System.out.println(">>>"+float2int(0.46666f));
        System.out.println(">>>"+float2int(0.433333f));
        System.out.println(">>>"+float2int(10.2f));


        System.out.println(">>>"+int2float(255));
        System.out.println(">>>"+int2float(255));
        System.out.println(">>>"+int2float(110));
        System.out.println(">>>"+int2float(11));
        System.out.println(">>>"+int2float(5));

        int tet = getCurrentQuarter();
        LocalDateTime str = DateUtil.strToLocalDateTime("20201203");
        System.out.println(">>>"+str.toString());

        hexStringToByteArray("55aa0000ffff");
        isMonday(new Date());
    }
}