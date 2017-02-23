package com.project.utils;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class XMLUtil {
	
	private static String xmlString = null;
	
	public static void setXmlString(String xml){
		xmlString = xml;
	}
	
	/**通过dom4j读取xml节点值 
	 * @param xmlString
	 * @param nodeName
	 * @return
	 * @author liuding
	 */
	public static String getNodeValue(String nodeName){
		if(StringUtils.isEmpty(xmlString)){
			return null;
		}
		try {
			Document doc = DocumentHelper.parseText(xmlString);
			Element root =  doc.getRootElement();
			Node node = root.selectSingleNode(nodeName);
			if(null == node)
				return null;
			return node.getText();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//避免混乱
	public static String getNodeValue(String xml, String nodeName){
		if(StringUtils.isEmpty(xml)){
			return null;
		}
		try {
			Document doc = DocumentHelper.parseText(xml);
			Element root =  doc.getRootElement();
			Node node = root.selectSingleNode(nodeName);
			if(null == node)
				return null;
			return node.getText();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
