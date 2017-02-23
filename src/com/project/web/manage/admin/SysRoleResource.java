package com.project.web.manage.admin;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.constants.IConstants;
import com.project.entity.admin.SysRole;
import com.project.service.admin.ISysManagerService;
import com.project.service.admin.ISysRoleService;
import com.project.utils.ConvertUtil;
import com.project.web.common.RestResource;

/**
 * 系统角色
 * Created by GuoZhilong on 2016/2/24.
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleResource extends RestResource {
	
    private String PATH = "/manage/admin/";

    @Autowired
    private ISysManagerService sysManagerService;
    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 角色列表
     *
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping("list")
    public String rolelist(HttpServletRequest req, HttpServletResponse resp) {
        return PATH.concat("admin-role");
    }


    /**
     * 异步获取角色列表
     *
     * @param req
     * @param resp
     * @param sysRole
     * @return
     * @author GuoZhilong
     */
    @ResponseBody
    @RequestMapping("ajaxGetList")
    public Map<String, Object> ajaxGetList(HttpServletRequest request, HttpServletResponse response) {
    	Map<String, Object> params = returnPara(request);
        params.put("order", "order by createDate desc");
        int page = NumberUtils.toInt(ConvertUtil.mapObjectToString(params, "page"));
        int pageSize = NumberUtils.toInt(ConvertUtil.mapObjectToString(params, "pageSize"));
        int totalCount = sysManagerService.countBySql(SysRole.class, null);
        @SuppressWarnings("unchecked")
		List<SysRole> list = sysRoleService.queryByHql(SysRole.class, params);
        return successList(list, pageSize, page, totalCount, null);
    }


    /**
     * 编辑角色信息
     *
     * @param request
     * @param response
     * @param sysRole
     * @return
     * @author GuoZhilong
     */
    @ResponseBody
    @RequestMapping("ajaxEdit")
    public Map<String, Object> ajaxEdit(HttpServletRequest request, HttpServletResponse response, SysRole sysRole) {
        if (sysRole == null) {
            return error(IConstants.PARAMS_ERROR);
        }
        int roleId = 0;
        //更新操作
        if (sysRole.getId() != null && sysRole.getId() > 0) {
            SysRole oldSysRole = (SysRole) sysRoleService.queryById(SysRole.class, sysRole.getId());
            if (oldSysRole == null) {
            	return error(IConstants.PARAMS_ERROR);
            }
            if (!oldSysRole.getRoleName().equals(sysRole.getRoleName())) {
                SysRole role = (SysRole) sysRoleService.queryByField(SysRole.class, "roleName", sysRole.getRoleName());
                if (role != null) {
                	return error("该角色名称已存在");
                }
            }
            oldSysRole.setUpdateDate(new Date());
            oldSysRole.setRoleName(sysRole.getRoleName());
            boolean res = sysRoleService.update(oldSysRole);
            if (res) {
                roleId = oldSysRole.getId();
            }
        } else {
            SysRole role = (SysRole) sysRoleService.queryByField(SysRole.class, "roleName", sysRole.getRoleName());
            if (role != null) {
            	return error("该角色名称已存在");
            }
            //新增操作
            sysRole.setUpdateDate(new Date());
            sysRole.setCreateDate(new Date());
            roleId = sysRoleService.addReturnID(sysRole);
        }
        if (roleId <= 0) {
        	return error(IConstants.FAILURE_MSG);
        }else{
        	return success(IConstants.SUCCESS_MSG);
        }
    }


    /**
     * 删除角色
     *
     * @param request
     * @param response
     * @return
     * @author GuoZhilong
     */
    @ResponseBody
    @RequestMapping("ajaxDel")
    public Map<String, Object> ajaxDel(HttpServletRequest request, HttpServletResponse response) {
    	Integer id = NumberUtils.toInt(request.getParameter("id"), 0);
        SysRole role = (SysRole)sysRoleService.queryById(SysRole.class, id);
        if(role == null){
        	return error(IConstants.PARAMS_ERROR);
        }
        if (sysRoleService.deleteById(SysRole.class, id)) {
        	return success(IConstants.SUCCESS_MSG);
        }else{
        	return error(IConstants.FAILURE_MSG);
        }
    }
}
