package com.project.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * web常用tools
 *
 * @author GuoZhiLong
 * @date 2014年11月25日 下午4:36:21
 */
public class WebTools {

    /**
     * 添加至cookie
     *
     * @param resp
     * @param name
     * @param value
     * @return void
     * @throws
     * @author GuoZhiLong
     */
    public static void addCookie(HttpServletResponse resp, String name,
                                 String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    /**
     * 添加至cookie
     *
     * @param resp
     * @param name
     * @param value
     * @param day
     * @return void
     * @throws
     * @author GuoZhiLong
     */
    public static void addCookie(HttpServletResponse resp, String name,
                                 String value, int day) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(60 * 60 * 24 * day);// 保留?天
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    /**
     * 获取cookie
     *
     * @param req
     * @param name
     * @return String
     * @throws
     * @author GuoZhiLong
     */
    public static String getCookie(HttpServletRequest req, String name) {
        try {
            for (Cookie ck : req.getCookies()) {
                if (name.equals(ck.getName())) {
                    return ck.getValue();
                }
            }
            return null;
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    /**
     * 清除cookie
     *
     * @param resp
     * @param name
     * @return void
     * @throws
     * @author GuoZhiLong
     */
    public static void removeCookie(HttpServletResponse resp, String name) {
        Cookie ck = new Cookie(name, null);
        ck.setPath("/");
        ck.setMaxAge(0);
        resp.addCookie(ck);
    }

    /**
     * 获取域名
     *
     * @param url
     * @return
     */
    public static String getHostByUrl(String url) {
        if (StringUtils.isEmpty(url))
            return null;
        url = url.replace("https://", "").replace("http://", "");
        if (url.contains("/") && !url.startsWith("/")) {
            return url.substring(0, url.indexOf("/"));
        } else {
            return url;
        }
    }

    private static String baiduAK = "125cbfada11a9c7b8005f168c065045c"; //百度密钥

    /**
     * 通过IP获取城市
     *
     * @param ip
     * @return
     * @author liuding
     */
    public static String getCity(String ip) {
        try {
            String url = "http://api.map.baidu.com/location/ip?ak=" + baiduAK + "&ip=" + ip;
            String html = HttpClientUtil.createAndGetText(url);
            JSONObject obj = JSONObject.parseObject(html);
            JSONObject content = obj.getJSONObject("content");
            return content.getJSONObject("address_detail").getString("city");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过ip获取位置详细信息
     *
     * @param ip
     * @return [0:省1:市2:百度城市代码]
     * @author liuding
     */
    public static String[] getLocationInfo(String ip) {
        try {
            String url = "http://api.map.baidu.com/location/ip?ak=" + baiduAK + "&ip=" + ip;
            String html = HttpClientUtil.getURLText(url);
            JSONObject obj = JSONObject.parseObject(html);
            JSONObject content = obj.getJSONObject("content");
            JSONObject local = content.getJSONObject("address_detail");
            String[] array = new String[]{local.getString("province"),
                    local.getString("city"),
                    local.getString("city_code")};
            return array;
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }


    public static String getRealIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("X-Real-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("REMOTE-HOST");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * 检测当前URL是否可连接或是否有效
     *
     * @param urlStr    url地址
     * @param testCount 测试次数
     * @return
     * @author GuoZhilong
     */
    public synchronized static boolean isConnect(String urlStr, Integer testCount) {
        URL url;
        HttpURLConnection con;
        int state = -1;
        int counts = 0;
        boolean res = false;
        if (StringUtils.isBlank(urlStr)) {
            return false;
        }
        while (counts < testCount) {
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

}
