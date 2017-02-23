package com.project.service.admin.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.core.proxy.ProxyFactory;
import com.project.entity.admin.SysUserRole;
import com.project.service.admin.ISysUserRoleService;
import com.project.service.common.impl.CommonServiceImpl;

@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends CommonServiceImpl implements ISysUserRoleService {

	@SuppressWarnings("unchecked")
	public List<SysUserRole> queryByParams(SysUserRole sysUserRole, Integer start, Integer numPerPage) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		sb.append("from SysUserRole where 1=1");
		if (sysUserRole != null) {
			if (sysUserRole.getUserId() != null) {
				sb.append(" and userId=?");
				params.add(sysUserRole.getUserId());
			}
		}
		return ProxyFactory.baseService.findByHql(SysUserRole.class, sb.toString(), params, start, numPerPage);
	}

	public boolean delete(Integer id) {
		return ProxyFactory.baseService.deleteById(SysUserRole.class, id);
	}

	public SysUserRole queryById(Integer id) {
		return (SysUserRole) ProxyFactory.baseService.findById(SysUserRole.class.getName(), id);
	}

	public SysUserRole queryByUid(Integer userId) {
		String hql="from SysUserRole where userId=?";
		return (SysUserRole)ProxyFactory.baseService.findObjectByHql(SysUserRole.class,hql,userId);
	}

}
