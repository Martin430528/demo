package com.project.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TenpayUtil {

    private static Object Server;

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

    public static String URLencode(String content) {

        String URLencode;

        URLencode = replace(Server.equals(content), "+", "%20");

        return URLencode;
    }

    private static String replace(boolean equals, String string, String string2) {

        return null;
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

    public static String QRfromGoogle(String chl) {
        int widhtHeight = 300;
        String EC_level = "L";
        int margin = 0;
        String QRfromGoogle;
        chl = URLencode(chl);

        QRfromGoogle = "http://chart.apis.google.com/chart?chs=" + widhtHeight
                + "x" + widhtHeight + "&cht=qr&chld=" + EC_level + "|" + margin
                + "&chl=" + chl;

        return QRfromGoogle;
    }

    /**
     * 时间转换成字符串
     *
     * @param date       时间
     * @param formatType 格式化类型
     * @return String
     */
    public static String date2String(Date date, String formatType) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        return sdf.format(date);
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
        } catch (java.text.ParseException e) {
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
        } catch (java.text.ParseException e) {
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
     * 检测当前URL是否可连接或是否有效
     *
     * @param urlStr
     * @return
     * @author GuoZhilong
     */
    public synchronized static boolean isConnect(String urlStr) {
        URL url;
        HttpURLConnection con;
        int state = -1;
        int counts = 0;
        boolean res = false;
        if (StringUtils.isBlank(urlStr)) {
            return false;
        }
        while (counts < 2) {
            try {
                url = new URL(urlStr);
                con = (HttpURLConnection) url.openConnection();
                state = con.getResponseCode();
                if (state == 200) {
                    res = true;
                }
                break;
            } catch (Exception ex) {
                counts++;
                continue;
            }
        }
        return res;
    }
    
	public static boolean isNum(String str){
		if(StringUtils.isBlank(str)){
			return false;
		}
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
	
	public static boolean isDigits(String str){
		return str.matches("^[-+]?([0-9]+)$");
	}
    
	public static final String getIp(final HttpServletRequest request)
	        throws Exception {
		return request.getRemoteAddr();
	}
	
	public static Double getPrice(long price){
		return price / 100.0;
	}
	
}