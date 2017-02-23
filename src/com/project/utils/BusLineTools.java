package com.project.utils;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;


/**公交线路
 * @author liuding
 * @create Apr 26, 2013 12:17:41 PM
 */
public class BusLineTools {
	
	/**获取公交线路
	 * @param cityName
	 * @param busNo
	 * @return
	 * @author liuding
	 */
	public static String getBusLine(String cityName, String busNo, int max){
		try{
			if(StringUtils.isEmpty(cityName)){
				cityName = "深圳";
			}
			cityName = cityName.trim();
			busNo = busNo.trim();
			String key = "162f40a338bb07f755a1bf052254c7a7";
			String url = "http://openapi.aibang.com/bus/lines?app_key="+key+"&city="
				+cityName+"&q=" + busNo;
			String html = HttpClientUtil.createAndGetText(url, "UTF-8");
			if(StringUtils.isEmpty(html)){
				return null;
			}
			Document doc = DocumentHelper.parseText(html);
			Element root =  doc.getRootElement();
			Element lines = root.element("lines");
			@SuppressWarnings("rawtypes")
			List nodes = lines.elements("line");
			if(null == nodes){
				return null;
			}
			StringBuffer content = new StringBuffer();
			Element line;
			int size = nodes.size();
			if(max > 0){
				size = (max > size) ? size : max;
			}
			for(int i=0; i< size; i++){
				line = (Element)nodes.get(i);
				content.append(line.element("name").getText().trim());
				content.append("\n");
				content.append(line.element("info").getText());
				content.append("\n");
				content.append(line.element("stats").getText().trim());
				content.append("\n");
				content.append("\n");
			}
			String str = content.toString();
			str = str.replace(";", ">").replace(" ", "");
			return str;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
