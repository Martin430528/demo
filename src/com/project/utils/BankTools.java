package com.project.utils;


/**银行信息转换
 * @author ldm 2016年6月7日10:26:17
 */
public class BankTools {
	
	//根据编号获取银行名称
	public static String bankNameConversion(String bankNo){
		switch (bankNo) {
			case "0":return "请选择银行";
			case "1":return "中国工商银行";
			case "2":return "中国建设银行";
			case "3":return "中国农业银行";
			case "4":return "中国交通银行";
			case "5":return "中国邮政银行";
			case "6":return "中国银行";
			case "7":return "支付宝";
			case "8":return "财富通";
			default:return "";
		}
	}
	
}
