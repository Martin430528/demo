package com.project.service.common;

import javax.servlet.http.HttpServletRequest;

/**
 * 发送验证码记录service
 * 
 * @author GeRuzhen
 */
public interface ISendValidCodeService {

	/**
	 * 检查验证码是否有效
	 * @param mobilePhone
	 * @param code
	 * @return 0.验证码不存在 1.验证成功 2.验证码失效
	 */
	int checkCode(String mobilePhone, String code);

	/**
	 * 检查验证码是否有效
	 * 
	 * @param request
	 * @param seesionKey
	 * @return 0.验证码不存在 1.验证成功 2.验证码失效
	 */
	int checkCodeSession(HttpServletRequest request, String seesionKey);

	/**
	 * 发送验证码
	 * @param request
	 * @param mobilePhone 接收手机号码
	 * @param sendType 所属模块类别
	 * @param type 验证码类别 1.短信 2.语音
	 * @return
	 */
	boolean sendCode(HttpServletRequest request, String mobilePhone, int sendType, int type);
}
