package com.project.utils;

import java.io.IOException;

/**
 * phantomjs通过工具类
 * 
 * @author GuoZhiLong
 * @date 2015年8月26日 下午3:27:03
 * 
 */
public class PhantomjsUtils {
	private static final String PHANTOMJS_PATH = "D:/phantomjs-2.0.0-windows/bin/";

	/**
	 * 截图
	 * 
	 * @author GuoZhiLong
	 * @param url
	 *            被截图的地址
	 * @return
	 * @return String
	 * @throws
	 */
	public String screenshots(String url) {
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec(PHANTOMJS_PATH
					+ "phantomjs.exe D:/phantomjs-2.0.0-windows/bin/test.js E:/test.png"
					+ url.trim());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[]af){
		PhantomjsUtils phantomjsUtils=new PhantomjsUtils();
		String src=phantomjsUtils.screenshots("http://fanyi.youdao.com/");
		System.out.println(src);
	}
}
