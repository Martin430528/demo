package com.project.web.weixin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.constants.WeixinConstants;
import com.project.utils.weixin.SignUtil;
import com.project.web.common.RestResource;

@Controller
@RequestMapping("weixin")
public class WeixinResource extends RestResource {
	
	private Logger logger = Logger.getLogger(WeixinResource.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public Object get(HttpServletRequest request, HttpServletResponse response,
			String signature, String timestamp, String nonce, String echostr) throws IOException{
		String respmsg = "";
		logger.info("signature="+signature);
		logger.info("timestamp="+timestamp);
		logger.info("nonce="+nonce);
		logger.info("echostr="+echostr);
		if (StringUtils.isNotBlank(signature) && StringUtils.isNotBlank(timestamp) && StringUtils.isNotBlank(nonce) && StringUtils.isNotBlank(echostr)
				&& SignUtil.checkSignature(WeixinConstants.TOKEN, signature, timestamp, nonce)) {
			respmsg = echostr;
		}
		response.getWriter().print(respmsg);
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public Object post(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException{
		
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(request
				.getInputStream(), "UTF-8"));
		String xml = null;
		xml = br.readLine();
		while (xml != null) {
			sb.append(xml);
			xml = br.readLine();
		}
		xml = sb.toString();
		
		logger.info(xml);
		
		return null;
	}
	
}
