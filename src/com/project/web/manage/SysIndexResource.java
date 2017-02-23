package com.project.web.manage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.web.common.RestResource;

/**管理端首页
 * @author LiMin
 * 2016年6月22日
 */
@Controller
@RequestMapping("/sys/index")
public class SysIndexResource extends RestResource {

	@RequestMapping()
	public String index(HttpServletRequest request, HttpServletResponse response){
		return "/manage/index";
	}
	
}
