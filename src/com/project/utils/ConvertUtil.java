package com.project.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

/**常用转换工具类
 * @author LiMin
 */
public class ConvertUtil {

	/** 将List集合转换成字符串，用英文逗号“,”相隔
	 * @param list
	 * @return
	 */
	public static String list2String(List<String> list){
		if(CollectionUtils.isEmpty(list)){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for(String str : list){
			sb.append(str).append(",");
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}
	
	/**
	 * 将集合拼接成字符串 ,隔开
	 */
	public static String listSplltString(List<Map<String, Object>> list, String key) {
		String key_value = "";
		if (list == null)
			return key_value;
		String key_id = "";
		for (Map<String, Object> map : list) {
			key_id = mapObjectToString(map, key);
			if (key_id != "") {
				if (key_value != "") {
					key_value += ",";
				}
				key_value += key_id;
			}
		}
		return key_value;
	}

	/**
	 * 将集合拼接成字符串 ,隔开
	 */
	public static String mapSplltString(Map<String, Object> map) {
		String key_value = "";
		String key_id = "";
		for (String key : map.keySet()) {
			key_id = mapObjectToString(map, key);
			if (key_id != "") {
				if (key_value != "") {
					key_value += ",";
				}
				key_value += key_id;
			}
		}
		return key_value;
	}

	/**
	 * 将map<string,object> 转成 string 类型
	 */
	public static String mapObjectToString(Map<String, Object> map, String key) {
		//modified by kongxm@20150625 这边有一个bug，如果map是空的话，会挂掉
		if(map == null){
			return "";
		}
		
		String value = "";
		if (map.containsKey(key) && map.get(key) != null) {
			value = map.get(key).toString();
		}
		return value;
	}
	
	/**
	 * 将map<string,object> 转成 list 类型
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> mapObjectToList(Map<String, Object> map, String key) {
		List<Map<String, Object>> value = null;
		if (map.containsKey(key) && map.get(key) != null) {
			try {
				value = (List<Map<String, Object>>) map.get(key);
			} catch (Exception e) {
				return null;
			}
		}
		return value;
	}

	/**
	 * 将map<string,object> 转成 map<string,string> 类型
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> mapObjectToMap(Map<String, Object> map, String key) {
		Map<String, String> value = null;
		if (map.containsKey(key) && map.get(key) != null) {
			value = (Map<String, String>) map.get(key);
		}
		return value;
	}

	public static Map<String, String> mapObjectToMap(Map<String, Object> map) {
		Map<String, String> value = new HashMap<String, String>();
		for (String key : map.keySet()) {
			value.put(key, mapObjectToString(map, key));
		}
		return value;
	}
	
}
