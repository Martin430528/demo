package com.project.service.common.impl;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.project.service.common.IPhoneValidCodeService;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;

@Service("phoneValidCodeService")
public class PhoneValidCodeServiceImpl implements IPhoneValidCodeService {

	@SuppressWarnings("unchecked")
	public boolean sendSMSValidCode(String phone, String templateId, String[] params) {
		boolean res = false;
		try {
			HashMap<String, Object> result = null;
			CCPRestSDK restAPI = new CCPRestSDK();
			restAPI.init("sandboxapp.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
			restAPI.setAccount("aaf98f894979d7be014982f35fb8067b", "7d4598c9e6814b2c95f25bbfecab138f");// 初始化主帐号名称和主帐号令牌(accountSid,accountToken)
			restAPI.setAppId("8a48b55149d5792d0149df9e5cdc051b");// 初始化应用ID(AppId)
			result = restAPI.sendTemplateSMS(phone, templateId, params);
			System.out.println(phone + "短信验证码发送结果：" + result);
			if ("000000".equals(result.get("statusCode"))) {
				// 正常返回输出data包体信息（map）
				HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
				Set<String> keySet = data.keySet();
				for (String key : keySet) {
					Object object = data.get(key);
					System.out.println(key + " = " + object);
				}
				res = true;
			} else {
				res = false;
				System.out.println(result);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean voiceValidCode(String phone, String randomNum) {
		boolean res = false;
		try {
			HashMap<String, Object> result = null;
			CCPRestSDK restAPI = new CCPRestSDK();
			restAPI.init("sandboxapp.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
			restAPI.setAccount("aaf98f894979d7be014982f35fb8067b", "7d4598c9e6814b2c95f25bbfecab138f");// 初始化主帐号名称和主帐号令牌
			restAPI.setAppId("8a48b55149d5792d0149df9e5cdc051b");// 初始化应用ID
			//result = restAPI.voiceVerify("验证码内容", "号码","显示的号码","3(播放次数)","状态通知回调地址", "语言类型", "第三方私有数据");
			result = restAPI.voiceVerify(randomNum, phone,"","3","", "", "");
			System.out.println(phone + "语音验证码发送结果：" + result);
			if ("000000".equals(result.get("statusCode"))) {
				// 正常返回输出data包体信息（map）
				HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
				Set<String> keySet = data.keySet();
				for (String key : keySet) {
					Object object = data.get(key);
					System.out.println(key + " = " + object);
				}
				res = true;
			} else {
				res = false;
				// 异常返回输出错误码和错误信息
				System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

}
