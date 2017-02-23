package com.project.service.caccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import com.project.core.proxy.ProxyFactory;
import com.project.service.caccount.impl.IAccountService;
import com.project.service.common.impl.CommonServiceImpl;
/**
 * 
 * @author ldm 2016年5月31日10:14:32
 *
 */
@Component
public class AccountService extends CommonServiceImpl implements IAccountService {
	
	/**
	 * 获取企业账号信息
	 */
	public List<Map<String, Object>> getAccountlist(Map<String, Object> params){
		StringBuilder sql = new StringBuilder("select * from corporate_account where 1=1 ");
		int page = 1;
        int pageSize = 0;
        
        List<Object> listValue = new ArrayList<Object>();
        if (params != null && params.size() > 0) {
        	match(params, listValue, sql);
            if (params.containsKey("order")) {
            	sql.append(" " + params.get("order"));
            }
            if (params.containsKey("page") && params.containsKey("pageSize")) {
                page = NumberUtils.toInt(params.get("page").toString(), 1);
                pageSize = NumberUtils.toInt(params.get("pageSize").toString());
            }
        }
        List<Map<String, Object>> list = ProxyFactory.baseService.findbyListResultMap(sql.toString(), (page - 1) * pageSize , pageSize, listValue);
        return list;
	}
	//添加数据
	public boolean saveOrUpdate(String className,Object o){
		return ProxyFactory.baseService.saveOrUpdate(className, o);
	}
	//获取单条数据
	public Object findById(String className, Integer id){
		return ProxyFactory.baseService.findById(className, id);
	}	
	
	/**
	 * 获取管理员账号信息
	 */
	public List<Map<String, Object>> getadminTratorlist(Map<String, Object> params){
		StringBuilder sql = new StringBuilder("select "
				+ "sys_manager.id, sys_manager.user_name, sys_manager.mark, sys_manager.real_name, sys_role.role_name, sys_user_role.sys_role_id as role_id "
				+ "from sys_manager "
				+ "left join sys_user_role ON sys_manager.id=sys_user_role.user_id "
				+ "LEFT JOIN sys_role ON sys_user_role.sys_role_id=sys_role.id where 1=1 ");
		int page = 1;
        int pageSize = 0;
        
        List<Object> listValue = new ArrayList<Object>();
        if (params != null && params.size() > 0) {
        	match(params, listValue, sql);
            if (params.containsKey("order")) {
            	sql.append(" " + params.get("order"));
            }
            if (params.containsKey("page") && params.containsKey("pageSize")) {
                page = NumberUtils.toInt(params.get("page").toString(), 1);
                pageSize = NumberUtils.toInt(params.get("pageSize").toString());
            }
        }
        List<Map<String, Object>> list = ProxyFactory.baseService.findbyListResultMap(sql.toString(), (page - 1) * pageSize , pageSize, listValue);
        return list;
	}
}
