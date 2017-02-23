package com.project.web.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.constants.IConstants;
import com.project.service.common.ISendValidCodeService;
import com.project.utils.MD5;

/**
 * 手机号码，短信验证码，语音验证码校验
 *
 * @author GeRuzhen
 * @date 2015年12月28日
 */
@Controller
@RequestMapping("/valid")
public class SendValidCodeResource extends RestResource {

    @Autowired
    private ISendValidCodeService sendValidCodeService;

    /**
     * 发送验证码
     *
     * @param request
     * @param response
     * @param mobilePhone 接收验证码手机号
     * @param sendType    所属模块 1.注册
     * @param type        验证码类别 1.短信 2.语音
     * @return
     * @author GeRuzhen
     */
    @ResponseBody
    @RequestMapping("/sendCode")
    public Map<String, Object> sendSMSCode(HttpServletRequest request, HttpServletResponse response, String mobilePhone, String sendType, String type) {
    	if (StringUtils.isBlank(mobilePhone)) {
            return error(IConstants.PARAMS_ERROR);
        }
        boolean bo = sendValidCodeService.sendCode(request, mobilePhone, NumberUtils.toInt(sendType, 1), NumberUtils.toInt(type, 1));
        if (bo) {
            return success(IConstants.SUCCESS_MSG);
        }
        return error(IConstants.FAILURE_MSG);
    }

    /**
     * 校验验证码
     *
     * @param request
     * @param response
     * @param phone    手机号码
     * @param code     验证码
     * @author GeRuzhen
     */
    @ResponseBody
    @RequestMapping(value = "checkCode")
    public Map<String, Object> checkCode(HttpServletRequest request, HttpServletResponse response,
                            String phone, String code) {
        if (StringUtils.isBlank(code) || StringUtils.isBlank(phone)) {
            return error(IConstants.PARAMS_ERROR);
        }
        int i = sendValidCodeService.checkCodeSession(request, MD5.encode32(phone + code));
        if (i == 0) {
            return error("验证码错误！");
        }
        if (i == 2) {
            return error("验证码已失效");
        }
        return success(IConstants.SUCCESS_MSG);
    }
	
}
