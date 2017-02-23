package com.project.utils;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.time.DateFormatUtils;

/**编码生成器
 * @author LiMin
 * 2016年7月5日
 */
public class CodeGenerator {
	
	/**生成商品编号
	 * @return
	 */
	public static String getGoodsNo() {
		return getCode("00");
	}

	/**生成订单编号
	 * @return
	 */
	public static String getOrderCode() {
		return getCode("01");
	}

	private static String getCode(String type) {
		StringBuffer result = new StringBuffer();
		result.append(DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS")).append(type);
		Random rm = new Random(UUID.randomUUID().hashCode());
		// 获得随机数
		double pross = (1 + rm.nextDouble()) * Math.pow(10, 7);
		// 将获得的获得随机数转化为字符串
		String fixLenthString = String.valueOf(pross);
		result.append(fixLenthString.substring(2, 7 + 2));
		// 返回固定的长度的随机数
		return result.toString();
	}
}
