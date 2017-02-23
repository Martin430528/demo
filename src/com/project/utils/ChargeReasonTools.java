package com.project.utils;

/**
 * 
 * @author ldm 2016年5月25日23:51:46
 *
 */
public class ChargeReasonTools {
	
	/**
	 * 转换充值原因类型为中文
	 * @param type 类型   1:"充值",2:"报单",3":"转出",4:"转入",5:"奖金",6:"提款",7:"取消提款"
	 * @return
	 */
	public static String crToString(String type){
		switch (type) {
			case "1": type = "充值"; break;
			case "2": type = "报单"; break;
			case "3": type = "转出"; break;
			case "4": type = "转入"; break;
			case "5": type = "奖金"; break;
			case "6": type = "提款"; break;
			case "7": type = "取消提款"; break;
			case "10": type = "管理端会员充值"; break;
			default: type = ""; break;
		}
		return type;
	}
	
	/**
	 * 提款状态转换为中文
	 * @return
	 */
	public static String drawingsToString(String type){
		switch (type) {
			case "1": type = "提款成功"; break;
			case "2": type = "余额不足"; break;
			case "3": type = "已退现"; break;
			case "0": type = "等待提现"; break;
			default: type = ""; break;
		}
		return type;
	}
}
