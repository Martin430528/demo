package com.project.core.proxy;

import java.util.Random;

public class SqlUtil {
	/**
	 * 
	 * <加密字符串>
	 * 
	 * @author bin kao
	 * @date 2010-4-8
	 * @param str
	 * @return
	 */
	public static boolean bo = false;

	public static String encode(String str) {
		if (bo) {
			StringBuffer htext = new StringBuffer();
			String mapTable[] = { "q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j",
					"k", "l", "z", "x", "c", "v", "b", "n", "m", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
			Random ran = new Random();
			String c = mapTable[ran.nextInt(mapTable.length - 1)];
			char m = c.charAt(0);
			htext.append(m);
			for (int i = 0; i < str.length(); i++) {
				htext.append((char) (str.charAt(i) + 10 - 1 * m));
			}
			return htext.toString();
		} else {
			return str;
		}
	}

	/**
	 * 
	 * <解密字符串>
	 * 
	 * @author bin kao
	 * @date 2010-4-8
	 * @param str
	 * @return
	 */
	public static String decode(String str) {
		if (bo) {
			StringBuffer dtext = new StringBuffer();
			if (!"".equals(str)) {
				String c = str.substring(0, 1);
				char m = c.charAt(0);
				str = str.substring(1, str.length());
				for (int i = 0; i < str.length(); i++) {
					dtext.append((char) (str.charAt(i) - 10 + 1 * m));
				}
			}
			return dtext.toString();
		} else {
			return str;
		}
	}
}