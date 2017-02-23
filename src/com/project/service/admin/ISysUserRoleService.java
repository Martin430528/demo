package com.project.service.admin;

import java.util.List;

import com.project.entity.admin.SysUserRole;
import com.project.service.common.ICommonService;

/**
 * 用户角色服务接口
 * 
 * @author GuoZhiLong
 * @date 2015年12月10日 上午11:39:06
 *
 */
public interface ISysUserRoleService extends ICommonService{
	/**
	 * 查询
	 * @param sysUserRole
	 * @param start
	 * @param numPerPage
	 * @return
	 */
	List<SysUserRole> queryByParams(SysUserRole sysUserRole, Integer start, Integer numPerPage);

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	boolean delete(Integer id);

	/**
	 * 根据id获取对象
	 * @param id
	 * @return
	 */
	SysUserRole queryById(Integer id);

	SysUserRole queryByUid(Integer userId);
}
