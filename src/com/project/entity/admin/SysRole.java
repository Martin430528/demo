package com.project.entity.admin;

import com.project.entity.BaseEntity;
import com.project.utils.TenpayUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * 角色
 *
 * @author GuoZhiLong
 * @date 2015年12月8日 下午12:05:38
 */
@Entity
@Table(name = "sys_role")
public class SysRole extends BaseEntity<SysRole> {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 20, name = "role_name", nullable = false)
	private String roleName;// 角色名称
	@Column(name = "create_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;// 创建时间
	@Column(name = "update_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;// 更新时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public String getCreateDateStr() {
		if (this.createDate == null) {
			return null;
		}
		return TenpayUtil.date2String(this.createDate, "yyyy-MM-dd HH:mm:ss");
	}

}
