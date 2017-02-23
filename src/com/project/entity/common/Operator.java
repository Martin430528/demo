package com.project.entity.common;

import java.io.Serializable;
import java.util.List;

import com.project.entity.admin.SysUserRole;

/**
 * 系统操作者类，用于登录后记录到HttpSession
 *
 * @author GuoZhiLong
 * @date 2015年12月9日 下午2:57:55
 */
public class Operator implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer userId;// 当前用户ID
    private Integer mark;//管理员标识0普通用户1管理员
    private List<SysUserRole> sysUserRoleList;// 用户角色
    private String userName; 
    
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public List<SysUserRole> getSysUserRoleList() {
        return sysUserRoleList;
    }

    public void setSysUserRoleList(List<SysUserRole> sysUserRoleList) {
        this.sysUserRoleList = sysUserRoleList;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
