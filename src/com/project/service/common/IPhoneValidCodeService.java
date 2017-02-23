package com.project.service.common;

public interface IPhoneValidCodeService {
	/**
	 * 发送短信验证码
	 * 
	 * @author GuoZhiLong
	 * @param @param
	 *            phone 手机
	 * @param @param
	 *            templateId 模板ID
	 * @param @param
	 *            params 参数(new String[]{"验证码","自定义"})
	 * @param @return
	 * @return boolean
	 */
	public boolean sendSMSValidCode(String phone, String templateId, String params[]);

	/**
	 * 语音验证码
	 * 
	 * @author GuoZhiLong
	 * @param phone
	 *            接收方的手机号码
	 * @param randomNum
	 *            随机数
	 * @return
	 * @return boolean
	 */
	public boolean voiceValidCode(String phone, String randomNum);
}
