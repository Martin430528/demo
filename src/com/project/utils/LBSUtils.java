package com.project.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.net.URLEncoder;


/**地图常用方法
 * @author liuding
 * @create Apr 12, 2013 4:58:26 PM
 */
public class LBSUtils {
	
	private static String baidukey = "6482a909fe337de45180ea627edfed89";
	
	/**根据地理编码获取详细位置
	 * @param lat 纬度
	 * @param lng 经度
	 * @return
	 * @author liuding
	 */
	public static JSONObject getGeocodingInfo(String lat, String lng){
		String url = "http://api.map.baidu.com/geocoder/v2/?ak=" + baidukey +
				"&location="+lat+","+lng+"&output=json&pois=1";
		String html = HttpClientUtil.createAndGetText(url);
		try {
			return JSONObject.parseObject(html);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param cityName
	 * @param keyWord
	 * @return [0:经度，1：纬度]
	 * @author liuding
	 */
	public static String[] getGeocoding(String cityName, String keyWord){
		try {
			String url = "http://api.map.baidu.com/telematics/v2/geocoding?output=json&keyWord="
				+ URLEncoder.encode(keyWord, "utf-8") + "&cityName="+URLEncoder.encode(cityName, "utf-8")+"&ak=" + baidukey;
			String html = HttpClientUtil.createAndGetText(url);
			JSONObject obj =  JSONObject.parseObject(html);
			if("success".equals(obj.getString("status").toLowerCase())){
				JSONObject location = obj.getJSONObject("results").getJSONObject("location");
				return new String[]{location.getString("lng"), location.getString("lat")};
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
