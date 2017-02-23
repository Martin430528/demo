package com.project.freemarker;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.constants.SessionConstants;
import com.project.entity.admin.SysMenu;
import com.project.entity.admin.SysPurview;
import com.project.entity.admin.SysUserRole;
import com.project.entity.common.Operator;
import com.project.interceptor.SysContent;
import com.project.service.admin.ISysMenuService;
import com.project.service.admin.ISysPurviewService;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * freemarker权限判断
 * 
 * @author GuoZhiLong
 * @date 2015年12月9日 下午4:03:51
 *
 */
public class FreemarkerAuthorityFilter implements TemplateMethodModelEx {

	@Autowired
	private ISysMenuService sysMenuService;// 系统菜单

	@Autowired
	private ISysPurviewService sysPurviewService;// 角色对应的权限(菜单)

	@SuppressWarnings("rawtypes")
	public Object exec(List param) throws TemplateModelException {
		String authorityValue = param.get(0).toString();
		HttpServletRequest request = SysContent.getRequest();

		Operator operator = (Operator) request.getSession().getAttribute(SessionConstants.OPERATOR_KEY);
		if (operator == null) {
			return false;
		}
		if(operator.getMark()==1){
			return true;
		}
		if (CollectionUtils.isEmpty(operator.getSysUserRoleList())) {
			return false;
		}
		SysMenu sysMenu = sysMenuService.queryByMenuCode(authorityValue);
		if (sysMenu == null) {
			return false;
		}
		List<SysUserRole> sysUserRoleList = operator.getSysUserRoleList();
        for (SysUserRole sysUserRole : sysUserRoleList) {
            String menuIds = "";
            SysPurview sysPurview = sysPurviewService.queryByRoleId(sysUserRole.getSysRoleId());
            if (sysPurview == null) {
                return false;
            }
            menuIds = sysPurview.getMenuIds();
            if (!("," + menuIds + ",").contains("," + sysMenu.getId() + ",")) {
                return false;
            }
        }
        return true;
	}
}
