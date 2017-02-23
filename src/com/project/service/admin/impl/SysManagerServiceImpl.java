package com.project.service.admin.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.constants.SessionConstants;
import com.project.core.proxy.ProxyFactory;
import com.project.entity.admin.SysManager;
import com.project.entity.admin.SysUserRole;
import com.project.entity.common.Operator;
import com.project.service.admin.ISysManagerService;
import com.project.service.admin.ISysUserRoleService;
import com.project.service.common.impl.CommonServiceImpl;
import com.project.utils.MD5;

@Service("sysManagerService")
public class SysManagerServiceImpl extends CommonServiceImpl implements ISysManagerService {

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    public Integer getLoginId(HttpServletRequest request) {
        Operator operator = (Operator) request.getSession().getAttribute(SessionConstants.OPERATOR_KEY);
        if (operator != null) {
            return operator.getUserId();
        }
        return null;
    }

    public boolean isLogin(HttpServletRequest request) {
        return getLoginId(request) != null;
    }

    public void markLogin(HttpServletRequest request, Operator operator) {
        request.getSession().setAttribute(SessionConstants.OPERATOR_KEY, operator);
    }

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute(SessionConstants.OPERATOR_KEY) != null) {
            session.removeAttribute(SessionConstants.OPERATOR_KEY);
        }
    }

    public SysManager login(String userName, String password) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            return null;
        }
        String hql = " from SysManager where userName = ? ";
        List<Object> params = new ArrayList<Object>();
        params.add(userName);
        @SuppressWarnings("unchecked")
		List<SysManager> list = ProxyFactory.baseService.findByHql(SysManager.class, hql, params, 0, 1);
        if(CollectionUtils.isEmpty(list)){
        	return null;
        }
        SysManager sm = list.get(0);
        if(!MD5.encode32(password).equals(sm.getPassword())){
        	return null;
        }
        return sm;
    }

    public SysManager queryByUserName(String userName) {
        if (StringUtils.isBlank(userName)) {
            return null;
        }
        return (SysManager) ProxyFactory.baseService.findByEQ(SysManager.class.getName(), "userName", userName);
    }

    public boolean customSave(SysManager userManager) {
        if (userManager == null) {
            return false;
        }
        boolean res = false;
        try {
            //新增
            int userId = ProxyFactory.baseService.addReturnID(SysManager.class.getName(), userManager);
            if (userId > 0) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setSysRoleId(userManager.getRoleId());
                sysUserRole.setUserId(userId);
                sysUserRole.setCreateDate(new Date());
                res = sysUserRoleService.saveOrUpdate(SysUserRole.class, sysUserRole);
            }
        } catch (RuntimeException e) {
            throw e;
        }

        return res;
    }

    public boolean customUpdate(SysManager userManager) {
        if (userManager == null) {
            return false;
        }
        boolean res = false;
        try {
            //更新
            if (userManager.getId() != null && userManager.getId() > 0) {
                boolean upRes = ProxyFactory.baseService.updateByObject(SysManager.class.getName(), userManager);
                if (upRes) {
                    if (userManager.getMark() != 1) {
                        SysUserRole sysUserRole = sysUserRoleService.queryByUid(userManager.getId());
                        sysUserRole.setSysRoleId(userManager.getRoleId());
                        res = sysUserRoleService.saveOrUpdate(SysUserRole.class, sysUserRole);
                    }
                }
            }
        } catch (RuntimeException e) {
            throw e;
        }

        return res;
    }

    public boolean deleteBySql(String sql){
    	return ProxyFactory.baseService.delete(sql);
    }
}
