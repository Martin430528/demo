package com.project.service.common.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.core.proxy.ProxyFactory;
import com.project.entity.common.SendValidCode;
import com.project.service.common.IPhoneValidCodeService;
import com.project.service.common.ISendValidCodeService;
import com.project.utils.CommonUtil;
import com.project.utils.MD5;
import com.project.utils.WebTools;

@Service("sendValidCodeService")
public class SendValidCodeServiceImpl implements ISendValidCodeService {
	
	@Autowired
	private IPhoneValidCodeService phoneValidCodeService;
	
	/**
	 * 检查验证码是否有效
	 * @param mobilePhone
	 * @param code
	 * @return
	 */
	public int checkCode(String mobilePhone, String code) {
		String sql = "select time_out from send_valid_code where mobile_phone = ? and valid_code = ? order by id desc";
		Object obj = ProxyFactory.baseService.findOneReturn(sql, mobilePhone, code);
		if(obj == null){//验证码不存在
			return 0;
		}
		long endTime = NumberUtils.toLong(obj.toString());
		if (System.currentTimeMillis() > endTime) {//验证码失效
			return 2;
		}
		return 1;
	}

	/**
	 * 检查验证码是否有效
	 * 
	 * @param request
	 * @param seesionKey
	 * @return
	 */
	public int checkCodeSession(HttpServletRequest request, String sessionKey) {
		Object obj = request.getSession().getAttribute(sessionKey);
		if(obj == null){//验证码错误
			return 0;
		}
		long endTime = NumberUtils.toLong(obj.toString());
		if (System.currentTimeMillis() > endTime) {//验证码失效
			return 2;
		}
		return 1;
	}

	/**
	 * 发送验证码
	 * @param request
	 * @param mobilePhone 接收手机号码
	 * @param sendType 所属模块类别
	 * @param type 验证码类别 1.短信 2.语音
	 * @return
	 */
	public boolean sendCode(HttpServletRequest request, String mobilePhone, int sendType, int type) {
		try{
			if(!checkPhoneSendTime(mobilePhone)){
				return false;
			}
			String code = CommonUtil.createRandomCode(true, 6);//验证码
			int time = 2;
			String modelId = "";
			switch(sendType){//不同模块 验证码失效时间及模板不同
			case 1://注册用户
				time = 10;
				modelId = "";//模板ID
				break;
			case 2: //找回密码
				time = 10;
				modelId = "";//模板ID
				break;
			/*
			default:
				break;*/
			}
			if(StringUtils.isBlank(modelId)){
				return false;
			}
			boolean res = false;
			if(type == 1){//短信验证码
				String params[] = new String[] { code, time + "" };
				res = phoneValidCodeService.sendSMSValidCode(mobilePhone, modelId, params);
			}else if(type == 2){//语音验证码
				res = phoneValidCodeService.voiceValidCode(mobilePhone, code);
			}
			if(res){
				long endTime = System.currentTimeMillis() + time * 60 * 1000;
				String ip = "";
				if(request != null){//session记录验证码
					request.getSession().setAttribute(MD5.encode32(mobilePhone + code), endTime);
					ip = WebTools.getRealIpAddr(request);
				}
				//数据记录存库
				saveValidCode(mobilePhone, code, type, ip, endTime);
			}
			return res;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}
	

	/**
	 * 发送记录
	 * @param mobilePhone 接收验证码手机号
	 * @param code 验证码
	 * @param validType 验证类别1.短信2.语音
	 * @param ip 
	 * @param time 有效时间(毫秒)
	 */
	private void saveValidCode(String mobilePhone, String code, int validType, String ip, long time) {
		if(StringUtils.isBlank(mobilePhone) || StringUtils.isBlank(code)){
			return ;
		}
		if(validType != 1 && validType != 2){
			return ;
		}
		SendValidCode validCode = new SendValidCode();
		validCode.setCreateDate(new Date());
		validCode.setTimeOut(time);
		validCode.setValidCode(code);
		validCode.setValidType(validType);
		validCode.setMobilePhone(mobilePhone);
		validCode.setIp(ip);
		ProxyFactory.baseService.addByObject(validCode);
	}
	
	/**
	 * 校验是否可以发送验证码
	 * @return
	 */
	private boolean checkPhoneSendTime(String mobilePhone){
		String sql = "select time_out from send_valid_code where mobile_phone = ? order by id desc";
		Object obj = ProxyFactory.baseService.findOneReturn(sql, mobilePhone);
		/**没有发送过验证码可以发送*/
		if(obj == null){
			return true;
		}
		/**当前时间大于上次发送时间可以继续发送*/
		if(System.currentTimeMillis() > NumberUtils.toLong(obj.toString())){
			return true;
		}
		return false;
	}


}
