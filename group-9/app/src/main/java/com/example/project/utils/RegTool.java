/** author: lice Liu and Nicole Ni
 * date: 2019.10.30
 * This class used to set RegTool to use interact with  other activities
 */
package com.example.project.utils;


import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 */

public class RegTool {

    public static boolean isMatch(String regex, String string) {
        return !isNullString(string) && Pattern.matches(regex, string);
    }

    public static boolean isNullString(@Nullable String str) {
        return str == null || str.length() == 0 || "null".equals(str);
    }

    /**
     * 邮箱校验
     *
     * @param emailContent
     * @return
     */
    public static boolean isEmail(String emailContent){
        String regEx = "^.+@.+\\..+$";
        return isMatch(regEx, emailContent);
    }


    /**
     * 比较两个日期之间的大小
     *
     * @param d1
     * @param d2
     * @return 前者大于后者返回true 反之false
     */
    public static boolean compareDate(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);

        int result = c1.compareTo(c2);
        if (result > 0)
            return true;
        else
            return false;
    }

    /**
     * 字符串类型的日期转化为日期
     *
     * @param dateStr 要转换的时间
     * @param format  要转换的时间格式
     * @return
     */
    public static Date strToDate(String dateStr, String format) {
        DateFormat df = new SimpleDateFormat(format);
        Date dateFront = null;
        try {
            dateFront = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFront;
    }

    /**
     * date
     *
     * @param date
     * @param format
     * @return
     */
    public static String getDateStr(Date date, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * long型时间转换时间格式
     *
     * @param longTime
     * @param format
     * @return
     */
    public static String longToDate(long longTime, String format) {
        Date date = new Date(longTime);
        SimpleDateFormat sd = new SimpleDateFormat(format);
        return sd.format(date);
    }

}
