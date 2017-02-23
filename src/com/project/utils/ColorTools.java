package com.project.utils;

import java.awt.*;

/**
 * 颜色处理工具
 * 
 * @author GuoZhiLong
 * @date 2015年5月29日 下午11:07:32
 * 
 */
public class ColorTools {
	/**
	 * 字符串颜色(#333333)转换为color
	 * 
	 * @author GuoZhiLong
	 * @param str
	 * @return
	 * @return Color
	 * @throws
	 */
	public static Color String2Color(String str) {
		int i = Integer.parseInt(str.substring(1), 16);
		return new Color(i);
	}

	/**
	 * color转换为字符串
	 * 
	 * @author GuoZhiLong
	 * @param color
	 * @return
	 * @return String
	 * @throws
	 */
	public static String Color2String(Color color) {
		String R = Integer.toHexString(color.getRed());
		R = R.length() < 2 ? ('0' + R) : R;
		String B = Integer.toHexString(color.getBlue());
		B = B.length() < 2 ? ('0' + B) : B;
		String G = Integer.toHexString(color.getGreen());
		G = G.length() < 2 ? ('0' + G) : G;
		return '#' + R + B + G;
	}
}
