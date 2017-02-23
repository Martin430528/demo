package com.project.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    private static Logger logger = Logger.getLogger(CommonUtil.class);

    public static String CreateNoncestr(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < length; i++) {
            Random rd = new Random();
            res += chars.indexOf(rd.nextInt(chars.length() - 1));
        }
        return res;
    }

    public static String CreateNoncestr() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < 16; i++) {
            Random rd = new Random();
            res += chars.charAt(rd.nextInt(chars.length() - 1));
        }
        return res;
    }

    public static String FormatQueryParaMap(HashMap<String, String> parameters) {

        String buff = "";
        try {
            List<Entry<String, String>> infoIds = new ArrayList<Entry<String, String>>(
                    parameters.entrySet());

            Collections.sort(infoIds,
                    new Comparator<Entry<String, String>>() {
                        public int compare(Entry<String, String> o1,
                                           Entry<String, String> o2) {
                            return (o1.getKey()).toString().compareTo(
                                    o2.getKey());
                        }
                    });

            for (int i = 0; i < infoIds.size(); i++) {
                Entry<String, String> item = infoIds.get(i);
                if (item.getKey() != "") {
                    buff += item.getKey() + "="
                            + URLEncoder.encode(item.getValue(), "utf-8") + "&";
                }
            }
            if (buff.isEmpty() == false) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return buff;
    }

    public static String FormatBizQueryParaMap(HashMap<String, String> paraMap,
                                               boolean urlencode) {

        String buff = "";
        try {
            List<Entry<String, String>> infoIds = new ArrayList<Entry<String, String>>(
                    paraMap.entrySet());

            Collections.sort(infoIds,
                    new Comparator<Entry<String, String>>() {
                        public int compare(Entry<String, String> o1,
                                           Entry<String, String> o2) {
                            return (o1.getKey()).toString().compareTo(
                                    o2.getKey());
                        }
                    });

            for (int i = 0; i < infoIds.size(); i++) {
                Entry<String, String> item = infoIds.get(i);
                if (item.getKey() != "") {

                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlencode) {
                        val = URLEncoder.encode(val, "utf-8");

                    }
                    buff += key.toLowerCase() + "=" + val + "&";

                }
            }

            if (buff.isEmpty() == false) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buff;
    }

    public static boolean IsNumeric(String str) {
        if (str.matches("\\d *")) {
            return true;
        } else {
            return false;
        }
    }

    public static String ArrayToXml(HashMap<String, String> arr) {
        String xml = "<xml>";

        Iterator<Entry<String, String>> iter = arr.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, String> entry = iter.next();
            String key = entry.getKey();
            String val = entry.getValue();
            if (IsNumeric(val)) {
                xml += "<" + key + ">" + val + "</" + key + ">";

            } else
                xml += "<" + key + "><![CDATA[" + val + "]]></" + key + ">";
        }

        xml += "</xml>";
        return xml;
    }

    /**
     * 把对象转换成字符串
     *
     * @param obj
     * @return String 转换成字符串,若对象为null,则返回空字符串.
     */
    public static String toString(Object obj) {
        if (obj == null)
            return "";

        return obj.toString();
    }

    /**
     * 把对象转换为int数值.
     *
     * @param obj 包含数字的对象.
     * @return int 转换后的数值,对不能转换的对象返回0。
     */
    public static int toInt(Object obj) {
        int a = 0;
        try {
            if (obj != null)
                a = Integer.parseInt(obj.toString());
        } catch (Exception e) {

        }
        return a;
    }

    /**
     * 获取当前时间 yyyyMMddHHmmss
     *
     * @return String
     */
    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }

    /**
     * 获取当前日期 yyyyMMdd
     *
     * @param date
     * @return String
     */
    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String strDate = formatter.format(date);
        return strDate;
    }

    /**
     * 根据格式获取当前日期
     *
     * @param date
     * @return String
     */
    public static String formatDate(Date date,String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String strDate = formatter.format(date);
        return strDate;
    }
    /**
     * 取出一个指定长度大小的随机正整数.
     *
     * @param length int 设定所取出随机数的长度。length小于11
     * @return int 返回生成的随机数。
     */
    public static int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    /**
     * 获取编码字符集
     *
     * @param request
     * @param response
     * @return String
     */

    public static String getCharacterEncoding(HttpServletRequest request,
                                              HttpServletResponse response) {

        if (null == request || null == response) {
            return "gbk";
        }

        String enc = request.getCharacterEncoding();
        if (null == enc || "".equals(enc)) {
            enc = response.getCharacterEncoding();
        }

        if (null == enc || "".equals(enc)) {
            enc = "gbk";
        }

        return enc;
    }

    /**
     * 获取unix时间，从1970-01-01 00:00:00开始的秒数
     *
     * @param date
     * @return long
     */
    public static long getUnixTime(Date date) {
        if (null == date) {
            return 0;
        }

        return date.getTime() / 1000;
    }

    /**
     * 时间转换成字符串
     *
     * @param date       时间
     * @param formatType 格式化类型
     * @return String
     */
    public static String date2String(Date date, String formatType) {
    	if(date == null || StringUtils.isBlank(formatType)){
    		return "";
    	}
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        return sdf.format(date);
    }

    /**
     * 字符串转时间
     *
     * @param date
     * @param formatType
     * @return Date
     * @throws
     * @author GuoZhiLong
     */
    public static Date string2Date(String date, String formatType) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatType);
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成随机数
     *
     * @param @param  numberFlag 是否是数字
     * @param @param  length 生成的长度
     * @param @return
     * @return String
     * @throws
     * @author GuoZhiLong
     */
    public static String createRandomCode(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890"
                : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }

    /**
     * 时间比较
     *
     * @param beginDate
     * @param endDate
     * @return Integer
     * @throws
     * @author GuoZhiLong
     */
    public static Integer dateCompare(String beginDate, String endDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(df.parse(beginDate));
            c2.setTime(df.parse(endDate));
        } catch (ParseException e) {
            System.err.println("格式不正确");
        }
        return c1.compareTo(c2);
    }

    /**
     * 时间比较
     *
     * @param beginDate
     * @param endDate
     * @param fmt       时间格式yyyy-MM-dd HH:mm:ss ......
     * @return Integer
     * @throws
     * @author GuoZhiLong
     */
    public static Integer dateCompare(String beginDate, String endDate,
                                      String fmt) {
        DateFormat df = new SimpleDateFormat(fmt);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(df.parse(beginDate));
            c2.setTime(df.parse(endDate));
        } catch (ParseException e) {
            System.err.println("格式不正确");
        }
        return c1.compareTo(c2);
    }

    /**
     * 时间加减(1年,2月,3日,4时,5分)
     *
     * @param type  类型
     * @param date  时间
     * @param param 加减参数
     * @return Date
     * @throws
     * @author GuoZhiLong
     */
    public static Date getDateBefore(Integer type, Date date, int param) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        switch (type) {
            case 1:// 年
                now.set(Calendar.YEAR, now.get(Calendar.YEAR) + param);
                break;
            case 2:// 月
                now.set(Calendar.MONTH, now.get(Calendar.MONTH) + param);
                break;
            case 3:// 日
                now.set(Calendar.DATE, now.get(Calendar.DATE) + param);
                break;
            case 4:// 时
                now.set(Calendar.HOUR, now.get(Calendar.HOUR) + param);
                break;
            case 5:// 分
                now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + param);
                break;
        }
        return now.getTime();
    }

    /**
     * 根据年月日，计算相差天数
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @param fmt       格式
     * @return int
     * @throws
     * @author GuoZhiLong
     */
    public static int daysBetween(Date beginDate, Date endDate, String fmt) {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        long between_days = 0;
        try {
            beginDate = sdf.parse(sdf.format(beginDate));
            endDate = sdf.parse(sdf.format(endDate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(beginDate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(endDate);
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个时间相差（天、小时、分钟、秒）
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param format    时间格式
     * @param type      类型(1天2小时3分钟4秒)
     * @return Long
     * @throws
     * @author GuoZhiLong
     */
    public static Long dateDiff(String startTime, String endTime,
                                String format, Integer type) {
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long res = 0;
        // 获得两个时间的毫秒时间差异
        try {
            diff = sd.parse(startTime).getTime() - sd.parse(endTime).getTime();
            day = diff / nd;// 计算差多少天
            hour = diff % nd / nh + day * 24;// 计算差多少小时
            min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
            sec = diff % nd % nh % nm / ns;// 计算差多少秒
            if (type == 1) {
                res = day;
            } else if (type == 2) {
                res = hour;
            } else if (type == 3) {
                res = min;
            } else if (type == 4) {
                res = sec;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 判断是否是邮箱
     *
     * @param str
     * @return
     */
    public static boolean judgeEmail(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Matcher mailMatcher = Pattern.compile("^[A-Za-z0-9d]+([-_.][A-Za-z0-9d]+)*@([A-Za-z0-9d]+[-.])+[A-Za-z0-9d]{2,5}$").matcher(str);
        if (mailMatcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是手机号码
     *
     * @param str
     * @return
     */
    public static boolean judgeMobile(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Matcher mobileMatcher = Pattern.compile("^(13|14|15|16|17|18)\\d{9}$").matcher(str);
        if (mobileMatcher.matches()) {
            return true;
        }
        return false;
    }


    /**
     * source非空字段覆盖target中对应的字段
     *
     * @param targetBean 目标对象
     * @param sourceBean 源对象
     * @author GuoZhilong
     */
    public static void copyNotNullField(Object targetBean, Object sourceBean) {
        if (sourceBean == null) {
            logger.info("源对象为空");
            return;
        }
        Class<?> targetClazz = targetBean.getClass();
        Class<?> sourceClazz = sourceBean.getClass();
        Field[] sourceFiles = sourceClazz.getDeclaredFields();
        for (int i = 0; i < sourceFiles.length; i++) {
            String fieldName = sourceFiles[i].getName();
            if (fieldName.equals("serialVersionUID")) {
                continue;
            }
            try {
                Field targetField = targetClazz.getDeclaredField(fieldName);
                // 判断目标对象和源对象同名属性的类型是否相同
                if (targetField.getType() == sourceFiles[i].getType()) {
                    // 由属性名字得到对应get和set方法的名字
                    String setMethodName = "set"
                            + fieldName.substring(0, 1).toUpperCase()
                            + fieldName.substring(1);
                    String getMethodName = "get"
                            + fieldName.substring(0, 1).toUpperCase()
                            + fieldName.substring(1);
                    // 调用source对象的getMethod方法
                    Method getMethod = sourceClazz.getDeclaredMethod(
                            getMethodName, new Class[]{});
                    Object value = getMethod.invoke(sourceBean, new Object[]{});
                    // 判断源对象的属性值是否非空
                    if (null == value || StringUtils.isBlank(value.toString())) {
                        continue;
                    }
                    // 调用目标对象的setMethod方法
                    Method setMethod = targetClazz.getDeclaredMethod(
                            setMethodName, sourceFiles[i].getType());
                    setMethod.invoke(targetBean, value);
                } else {
                    logger.info("同名属性类型不匹配");
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * 图片字段处理
     *
     * @param sourceImg 原图地址
     * @param type      类型1返回小图地址2返回中图地址
     * @return
     * @author GuoZhilong
     */
    public static String procesImgField(String sourceImg, Integer type) {
        if (StringUtils.isNotBlank(sourceImg)) {
            int dot = sourceImg.lastIndexOf('/');
            if ((dot > -1) && (dot < (sourceImg.length() - 1))) {
                if (type == 1) {
                    return sourceImg.substring(0, dot + 1) + "min_" + sourceImg.substring(dot + 1);
                } else if (type == 2) {
                    return sourceImg.substring(0, dot + 1) + "medium_" + sourceImg.substring(dot + 1);
                }
            }
        }
        return "";
    }
    
}
