package com.project.web.manage.admin;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.constants.IConstants;
import com.project.entity.admin.SysManager;
import com.project.entity.admin.SysRole;
import com.project.exception.ParameterException;
import com.project.service.admin.ISysManagerService;
import com.project.service.caccount.impl.IAccountService;
import com.project.utils.ConvertUtil;
import com.project.utils.MD5;
import com.project.utils.ServletBeanTools;
import com.project.web.common.RestResource;

/**
 * 企业账号
 * @author ldm 2016年5月23日16:04:36
 *
 */
@Controller
@RequestMapping("/sys/admin")
public class SysAdminResource extends RestResource {
	
	private String PATH = "/manage/admin/";//访问路径
	protected static final String PARENTVAL = "sys_admin_";
	protected static final String PALIASESVAL = "权限管理";
	
	@Autowired
	private IAccountService accountService;//自动装配
	@Autowired
	private ISysManagerService sysManagerService;
	
	/**授权管理员账号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("list")
	public String list1(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("order", "order by id desc");
		@SuppressWarnings("unchecked")
		List<SysRole> roleList = accountService.queryByHql(SysRole.class, params);
		request.setAttribute("roleList", roleList);
		return PATH.concat("admin-list");
	}
    
	
	/**
	 * 查询管理员账号数据
	 */
	@ResponseBody
    @RequestMapping("getadminTratorlist")
    public Map<String, Object> getadminTratorlist(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> params = returnPara(request);
        params.put("order", "order by sys_manager.mark asc, sys_manager.create_date desc");
        List<Map<String, Object>> list = accountService.getadminTratorlist(params);
        Integer totalCount = accountService.countBySql(SysManager.class, params);
        int pageSize = NumberUtils.toInt(ConvertUtil.mapObjectToString(params, "pageSize"));
        int page = NumberUtils.toInt(ConvertUtil.mapObjectToString(params, "page"));
        return successList(list, pageSize, page, totalCount, null);
    }
    
	/**
	 * 管理员账号增改
	 * @throws IOException 
	 */
	@ResponseBody
    @RequestMapping("updateAdmin")
    public Map<String, Object> updateAdmin(HttpServletRequest request,HttpServletResponse response, SysManager entity) throws IOException {
    	entity.setUpdateDate(new Date());
    	boolean result = false;
        if(entity.getId() == null){
            //新增
        	if(StringUtils.isNotBlank(entity.getPassword())){
        		entity.setPassword(MD5.encode32(entity.getPassword()));
        	}else{
        		entity.setPassword(MD5.encode32("111111"));
        	}
        	entity.setState(1);
        	entity.setCreateDate(new Date());
        	entity.setMark(2);
	        result = sysManagerService.customSave(entity);
        }else{
        	//修改
        	SysManager qentity = (SysManager) accountService.queryById(SysManager.class, entity.getId());
        	if(qentity == null){
        		throw new ParameterException(IConstants.PARAMS_ERROR);
        	}
        	if(StringUtils.isNotBlank(entity.getPassword())){
        		entity.setPassword(MD5.encode32(entity.getPassword()));
        	}
        	ServletBeanTools.copyIfNotNull(qentity, entity);
        	result = sysManagerService.customUpdate(qentity);
        }
        if(result){
        	return success(IConstants.SUCCESS_MSG);
        }else{
        	return error(IConstants.FAILURE_MSG);
        }
    }

	/**
	 * 删除管理员
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@ResponseBody
    @RequestMapping("deleteAdmin")
    public Map<String, Object> deleteAdmin(HttpServletRequest request,HttpServletResponse response) {
		int id = NumberUtils.toInt(request.getParameter("id"), 0);
        SysManager manager = (SysManager) sysManagerService.queryById(SysManager.class, id);
        if(manager == null){
        	return error(IConstants.PARAMS_ERROR);
        }
        if(sysManagerService.deleteById(SysManager.class, id)){
        	sysManagerService.deleteBySql("DELETE FROM sys_user_role WHERE user_id="+id);
        	return success(IConstants.SUCCESS_MSG);
        }else{
        	return error(IConstants.FAILURE_MSG);
        }
    }	
}
