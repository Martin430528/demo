package com.project.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.entity.common.Config;

@Controller
@RequestMapping("test")
public class TestResource {

	private String PATH = "/test/";
	
	@RequestMapping("1")
	public String test1(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		model.put("message", "test");
		
		Config config = new Config();
		config.setConfigKey("test");
		config.setConfigValue("value");
		
		model.put("config", config);
		
		return PATH.concat("1");
	}
	
}
