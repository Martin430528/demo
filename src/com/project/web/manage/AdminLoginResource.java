package com.project.web.manage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.constants.IConstants;
import com.constants.SessionConstants;
import com.project.entity.admin.SysManager;
import com.project.entity.admin.SysMenu;
import com.project.exception.ParameterException;
import com.project.service.admin.ISysManagerService;
import com.project.service.admin.ISysMenuService;
import com.project.service.common.IOperatorService;
import com.project.web.common.RestResource;

/**
 * 乐购e佰后台管理--登录
 * 
 * @author GuoZhiLong
 * @date 2015年8月3日 上午11:32:58
 * 
 */
@Controller
@RequestMapping("/sys/login")
public class AdminLoginResource extends RestResource {
	
	@Autowired
	private ISysManagerService sysManagerService;
	@Autowired
	private IOperatorService operatorService;
	@Autowired
	private ISysMenuService sysMenuService;

	/**
	 * 登录页面
	 * 
	 * @author GuoZhiLong
	 * @param req
	 * @param resp
	 * @return
	 * @return Object
	 * 
	 */
	@RequestMapping()
	public String login(HttpServletRequest req, HttpServletResponse resp) {
		return "/manage/login";
	}

	@ResponseBody
	@RequestMapping(value = "/ajaxLogin", method = RequestMethod.POST)
	public Map<String, Object> ajaxLogin(HttpServletRequest request, HttpServletResponse response, String userName, String password){
		if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
			throw new ParameterException("用户名和密码不能为空！");
		}
		SysManager sysManager = sysManagerService.login(userName, password);
		if(sysManager == null){
			return error(IConstants.LOGIN_ERROR);
		}
		operatorService.setOperator(request, sysManager.getId());

		//查询拥有权限的菜单列表
		List<SysMenu> menuList = sysMenuService.queryByManager(sysManager.getId());
		
		List<SysMenu> result = new ArrayList<SysMenu>();
		if(CollectionUtils.isNotEmpty(menuList)){
			for (SysMenu sysMenu : menuList) {
				if(sysMenu.getParentId().intValue() == 0){//一级菜单
					result.add(sysMenu);
				}
			}
			for(SysMenu sysMenu : result){
				List<SysMenu> menus = new ArrayList<SysMenu>();
				for(SysMenu menu : menuList){
					if(menu.getParentId().intValue() == sysMenu.getId().intValue()){
						menus.add(menu);
					}
				}
				if(CollectionUtils.isNotEmpty(menus)){
					sysMenu.setMenuUrl(menus.get(0).getMenuUrl());
				}
				sysMenu.setSysMenuChildList(menus);
			}
		}
		
		request.getSession().setAttribute(SessionConstants.MENU_LIST, result);
		return success(IConstants.SUCCESS_MSG);
	}

	@ResponseBody
	@RequestMapping(value = "/ajaxLogout")
	public Map<String, Object> ajaxLogout(HttpServletRequest request, HttpServletResponse response) {
		operatorService.logout(request, response);
		return success(IConstants.SUCCESS_MSG);
	}
	
}
