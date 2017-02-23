package com.project.service.admin;

import java.util.List;

import com.project.entity.admin.SysMenu;

/**
 * 系统菜单服务接口
 * 
 * @author GuoZhiLong
 * @date 2015年12月10日 上午11:00:41
 *
 */
public interface ISysMenuService {

	/**
	 * 根据菜单代码获取对象
	 * @param menuCode
	 * @return
	 */
	SysMenu queryByMenuCode(String menuCode);

	/**
	 * 新增或修改
	 * @param sysUserRole
	 * @return
	 */
	boolean saveOrUpdate(SysMenu sysMenu);

	/**
	 * 条件查询
	 * @param sysMenu
	 * @param start
	 * @param numPerPage
	 * @return
	 */
	List<SysMenu> queryByParams(SysMenu sysMenu, Integer start, Integer numPerPage);

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
	SysMenu queryById(Integer id);
	
	SysMenu queryByCodeAndPid(String menuCode,Integer parentId);
	
	int addReturnID(SysMenu sysMenu);
	
	/**根据会员ID查询
	 * @param managerId
	 * @return
	 */
	List<SysMenu> queryByManager(int managerId);
	
}
