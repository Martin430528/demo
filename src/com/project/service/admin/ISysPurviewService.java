package com.project.service.admin;

import com.project.entity.admin.SysPurview;

/**
 * 角色权限服务接口
 * 
 * @author GuoZhiLong
 * @date 2015年12月10日 上午11:38:37
 *
 */
public interface ISysPurviewService {

	/**
	 * 新增或修改
	 * @param sysPurview
	 * @return
	 */
	boolean saveOrUpdate(SysPurview sysPurview);

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	boolean delete(Integer id);

	/**
	 * 根据ID获得对象
	 * @param id
	 * @return
	 */
	SysPurview queryById(Integer id);

	/**
	 * 根据角色ID获取
	 * @param roleId
	 * @return
	 */
	SysPurview queryByRoleId(Integer roleId);
}
