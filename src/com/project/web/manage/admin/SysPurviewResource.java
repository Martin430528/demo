package com.project.web.manage.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.constants.IConstants;
import com.project.entity.admin.SysMenu;
import com.project.entity.admin.SysPurview;
import com.project.entity.admin.SysRole;
import com.project.service.admin.ISysMenuService;
import com.project.service.admin.ISysPurviewService;
import com.project.service.common.ICommonService;
import com.project.utils.ConvertUtil;
import com.project.web.common.RestResource;

/**
 * 权限管理
 * Created by GuoZhilong on 2016/1/16.
 */
@Controller
@RequestMapping("/sys/purview")
public class SysPurviewResource extends RestResource {
	
    private static final String PATH = "/manage/admin/";

    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private ISysPurviewService sysPurviewService;
    @Autowired
    private ICommonService commonService;

    /**
     * 权限列表
     *
     * @param request
     * @param response
     * @param roleId
     * @return
     * @author GuoZhilong
     */
    @RequestMapping("list/{roleId}")
    public String add(HttpServletRequest request, HttpServletResponse response, @PathVariable int roleId) {
        String menuIds = "";
        if (roleId > 0) {
        	//查询管理员组
        	SysRole role = (SysRole) commonService.queryById(SysRole.class, roleId);
        	request.setAttribute("role", role);
            SysPurview sysPurview = sysPurviewService.queryByRoleId(roleId);
            if (sysPurview != null) {
                menuIds = sysPurview.getMenuIds();
            }
        }
        SysMenu sysMenu = new SysMenu();
        sysMenu.setParentId(0);
        sysMenu.setIsDesc(1);
        List<SysMenu> sysMenuList = sysMenuService.queryByParams(sysMenu, 0, 0);
        for (SysMenu sysMenuTemp : sysMenuList) {
            sysMenu.setParentId(sysMenuTemp.getId());
            sysMenu.setIsDesc(2);
            if(StringUtils.isNotBlank(menuIds)){
            	if (("," + menuIds + ",").contains("," + sysMenuTemp.getId() + ",")) {
            		sysMenuTemp.setIsChecked(1);
                } else {
                	sysMenuTemp.setIsChecked(0);
                }
            }
            List<SysMenu> sysMenuChildList = sysMenuService.queryByParams(sysMenu, 0, 0);
            if (CollectionUtils.isNotEmpty(sysMenuChildList) && StringUtils.isNotBlank(menuIds)) {
                for (SysMenu sysMenuChecked : sysMenuChildList) {
                    if (("," + menuIds + ",").contains("," + sysMenuChecked.getId() + ",")) {
                        sysMenuChecked.setIsChecked(1);
                    } else {
                        sysMenuChecked.setIsChecked(0);
                    }
                }
            }
            sysMenuTemp.setSysMenuChildList(sysMenuChildList);
        }
        request.setAttribute("sysMenuList", sysMenuList);
        return PATH.concat("purview");
    }

    /**
     * 角色授权
     *
     * @param request
     * @param response
     * @return
     * @author GuoZhilong
     */
    @ResponseBody
    @RequestMapping("ajaxEdit")
    public Map<String, Object> ajaxEdit(HttpServletRequest request, HttpServletResponse response) {
        String menuIds = request.getParameter("menuIds");
        Integer roleId = NumberUtils.toInt(request.getParameter("roleId"), 0);
        if (roleId == 0) {
        	return error(IConstants.PARAMS_ERROR);
        }
        SysPurview sysPurview = sysPurviewService.queryByRoleId(roleId);
        if (sysPurview == null) {
            sysPurview = new SysPurview();
            sysPurview.setCreateDate(new Date());
        }
        sysPurview.setSysRoleId(roleId);
        sysPurview.setMenuIds("");
        if (StringUtils.isNotBlank(menuIds)) {
        	//查询所有菜单ID的pid
        	StringBuilder sql = new StringBuilder();
        	sql.append("select distinct parent_id from sys_menu");
        	Map<String, Object> params = new HashMap<String, Object>();
        	params.put("@id", menuIds);
        	List<Map<String, Object>> menuList = commonService.queryBySql(sql, params);
        	
        	if(CollectionUtils.isNotEmpty(menuList)){
        		for (Map<String, Object> map : menuList) {
        			String id = ConvertUtil.mapObjectToString(map, "parent_id");
        			if("0".equals(id)){
        				continue;
        			}
					if(!("," + menuIds + ",").contains("," + id + ",")){
						menuIds += "," + id;
					}
				}
        	}
            sysPurview.setMenuIds(menuIds);
        }
        boolean res = sysPurviewService.saveOrUpdate(sysPurview);
        if (!res) {
        	return error(IConstants.FAILURE_MSG);
        }
        return success(IConstants.SUCCESS_MSG);
    }
}
