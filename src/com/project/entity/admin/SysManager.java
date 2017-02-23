package com.project.entity.admin;

import com.project.entity.BaseEntity;
import com.project.utils.TenpayUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * 系统管理员
 *
 * @author GeRuzhen
 */
@Entity
@Table(name = "sys_manager")
public class SysManager extends BaseEntity<SysManager> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 36)
	private String email;// 邮箱
	@Column(length = 11, name = "mobile_phone")
	private String mobilePhone;// 手机号
	@Column(length = 36, name = "password")
	private String password;// 密码
	@Column(length = 20, name = "real_name")
	private String realName;// 真实姓名
	@Column(length = 20, name = "user_name")
	private String userName;// 用户名
	@Column(length = 200, name = "remark")
	private String remark;// 备注
	@Column(length = 1, name = "sex")
	private Integer sex;// 性别
	@Column(length = 1, name = "state")
	private Integer state;// 状态
	@Column(name = "create_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;// 创建时间
	@Column(name = "update_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;// 修改时间
	@Column(name = "login_date", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date loginDate;// 登录时间
	@Column(length = 1)
	private Integer mark;// 1超级管理员标识2普通用户

	@Transient
	private Integer roleId;// 角色ID
	@Transient
	private String roleName;// 角色名称

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getCreateDateStr() {
		if (this.createDate == null) {
			return null;
		}
		return TenpayUtil.date2String(this.createDate, "yyyy-MM-dd HH:mm:ss");
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getLoginDateStr() {
		if (this.loginDate == null) {
			return null;
		}
		return TenpayUtil.date2String(this.loginDate, "yyyy-MM-dd HH:mm:ss");
	}

	public Integer getMark() {
		return mark;
	}

	public void setMark(Integer mark) {
		this.mark = mark;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

}
