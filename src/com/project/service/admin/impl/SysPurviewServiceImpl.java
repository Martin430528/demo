package com.project.service.admin.impl;

import com.project.core.proxy.ProxyFactory;
import com.project.entity.admin.SysPurview;
import com.project.service.admin.ISysPurviewService;

import org.springframework.stereotype.Service;

@Service("sysPurviewService")
public class SysPurviewServiceImpl implements ISysPurviewService {

	public boolean saveOrUpdate(SysPurview sysPurview) {
		return ProxyFactory.baseService.saveOrUpdate(SysPurview.class.getName(), sysPurview);
	}

	public boolean delete(Integer id) {
		return ProxyFactory.baseService.deleteByObject(SysPurview.class.getName(), id);
	}

	public SysPurview queryById(Integer id) {
		return (SysPurview) ProxyFactory.baseService.findById(SysPurview.class.getName(), id);
	}

	public SysPurview queryByRoleId(Integer roleId) {
		String hql = "from SysPurview where sysRoleId=?";
		return (SysPurview) ProxyFactory.baseService.findObjectByHql(SysPurview.class, hql, roleId);
	}

}
