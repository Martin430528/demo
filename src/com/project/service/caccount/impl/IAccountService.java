package com.project.service.caccount.impl;

import java.util.List;
import java.util.Map;

import com.project.service.common.ICommonService;
/**
 * @author ldm 2016年5月31日10:14:50
 */
public interface IAccountService extends ICommonService {

	/**
	 * 获取企业账号信息
	 */
	public List<Map<String, Object>> getAccountlist(Map<String, Object> params);
	//添加数据
	public boolean saveOrUpdate(String className,Object o);
	//获取单条数据
	public Object findById(String className, Integer id);
	/**
	 * 获取管理员账号信息
	 */
	public List<Map<String, Object>> getadminTratorlist(Map<String, Object> params);
}
