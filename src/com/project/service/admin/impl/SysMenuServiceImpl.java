package com.project.service.admin.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.core.proxy.ProxyFactory;
import com.project.entity.admin.SysManager;
import com.project.entity.admin.SysMenu;
import com.project.entity.admin.SysPurview;
import com.project.entity.admin.SysUserRole;
import com.project.service.admin.ISysMenuService;
import com.project.service.common.ICommonService;

@Service("sysMenuService")
public class SysMenuServiceImpl implements ISysMenuService {

	@Autowired
	private ICommonService commonService;
	
	public SysMenu queryByMenuCode(String menuCode) {
		String hql = "from SysMenu where menuCode=?";
		return (SysMenu) ProxyFactory.baseService.findObjectByHql(SysMenu.class, hql, menuCode);
	}

	public boolean saveOrUpdate(SysMenu sysMenu) {
		return ProxyFactory.baseService.saveOrUpdate(SysMenu.class.getName(), sysMenu);
	}

	@SuppressWarnings("unchecked")
	public List<SysMenu> queryByParams(SysMenu sysMenu, Integer start, Integer numPerPage) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		sb.append("from SysMenu where 1=1");
		if (sysMenu != null) {
			if (sysMenu.getParentId() != null) {
				sb.append(" and parentId=?");
				params.add(sysMenu.getParentId());
			}
		}
		return ProxyFactory.baseService.findByHql(SysMenu.class, sb.toString(), params, start, numPerPage);
	}

	public boolean delete(Integer id) {
		return ProxyFactory.baseService.deleteById(SysMenu.class, id);
	}

	public SysMenu queryById(Integer id) {
		return (SysMenu) ProxyFactory.baseService.findById(SysMenu.class.getName(), id);
	}

	public SysMenu queryByCodeAndPid(String menuCode, Integer parentId) {
		String hql = "from SysMenu where menuCode=? and parentId=?";
		return (SysMenu) ProxyFactory.baseService.findObjectByHql(SysMenu.class, hql, menuCode, parentId);
	}

	public int addReturnID(SysMenu sysMenu) {
		return ProxyFactory.baseService.addReturnID(SysMenu.class.getName(), sysMenu);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SysMenu> queryByManager(int managerId) {
		List<SysMenu> menuList = new ArrayList<SysMenu>();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("@codeType", "0,1");
		params.put("!menuCode", "sys_login");
		params.put("order", "order by sort asc");
		
		//根据ID查询管理员
		SysManager manager = (SysManager) commonService.queryById(SysManager.class, managerId);
		if(manager.getMark().intValue() == 1){//超级管理员
			menuList = commonService.queryByHql(SysMenu.class, params);
		}else{//普通管理员
			//查询用户拥有哪些角色
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("userId", managerId);
			List<SysUserRole> userRoles = commonService.queryByHql(SysUserRole.class, params1);
			
			//根据角色查询菜单IDs
			StringBuffer roleIds = new StringBuffer();
			roleIds.append("0");
			if(CollectionUtils.isNotEmpty(userRoles)){
				for (SysUserRole sysUserRole : userRoles) {
					roleIds.append(",").append(sysUserRole.getSysRoleId());
				}
			}
			Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("@sysRoleId", roleIds.toString());
			List<SysPurview> purviews = commonService.queryByHql(SysPurview.class, params2);
			
			StringBuffer menuIds = new StringBuffer();
			menuIds.append("0");
			if(CollectionUtils.isNotEmpty(purviews)){
				for (SysPurview sysPurview : purviews) {
					menuIds.append(",").append(sysPurview.getMenuIds());
				}
			}
			
			params.put("@id", menuIds.toString());
			menuList = commonService.queryByHql(SysMenu.class, params);
		}
		return menuList;
	}

}
