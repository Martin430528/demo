package com.project.web;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.web.common.RestResource;

/**
 * 直接访问Demo页面
 */
@Controller
@RequestMapping("/vp")
public class ViewPageResource extends RestResource {

	@RequestMapping("{page}")
	public String page(HttpServletRequest request, HttpServletResponse response, @PathVariable String page){
		page = page.replaceAll("-", "/");
		return "/demo/".concat(page);
	}
	
}
