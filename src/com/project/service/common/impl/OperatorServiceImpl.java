package com.project.service.common.impl;

import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.constants.SessionConstants;
import com.project.entity.admin.SysManager;
import com.project.entity.admin.SysUserRole;
import com.project.entity.common.Operator;
import com.project.service.admin.ISysManagerService;
import com.project.service.admin.ISysUserRoleService;
import com.project.service.common.IOperatorService;
import com.project.utils.WebTools;

@Service("operatorService")
public class OperatorServiceImpl implements IOperatorService {

    @Autowired
    private ISysManagerService sysManagerService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;

    public boolean setOperator(HttpServletRequest request, int userId) {
        try {
            if (userId > 0) {
                SysManager sysManager = (SysManager)sysManagerService.queryById(SysManager.class, userId);
                if (sysManager == null) {
                    return false;
                }
                Operator operator = new Operator();
                operator.setUserId(sysManager.getId());
                operator.setMark(sysManager.getMark());
                operator.setUserName(sysManager.getUserName());
                List<SysUserRole> sysUserRoleList = new ArrayList<SysUserRole>();
                SysUserRole sysUserRole = sysUserRoleService.queryByUid(userId);
                if (sysUserRole != null) {
                    sysUserRoleList.add(sysUserRole);
                }
                if (CollectionUtils.isNotEmpty(sysUserRoleList)) {
                    operator.setSysUserRoleList(sysUserRoleList);
                }
                request.getSession().setAttribute(SessionConstants.OPERATOR_KEY, operator);
            }
        } catch (RuntimeErrorException e) {
            throw e;
        }
        return false;
    }

    public void setOperator(HttpServletRequest request, HttpServletResponse response, int userId, int day) {
        if (userId <= 0) {
            return;
        }
        //设置登录信息
        setOperator(request, userId);
        WebTools.addCookie(response, SessionConstants.OPERATOR_KEY, String.valueOf(userId), day);
    }

    public Operator getOperator(HttpServletRequest request) {
        return (Operator) request.getSession().getAttribute(SessionConstants.OPERATOR_KEY);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute(SessionConstants.OPERATOR_KEY);
        WebTools.removeCookie(response, SessionConstants.OPERATOR_KEY);
    }

}
