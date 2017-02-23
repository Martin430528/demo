package com.project.entity.admin;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.project.entity.BaseEntity;

/**
 * 系统菜单
 *
 * @author GuoZhiLong
 * @date 2015年12月8日 下午3:22:59
 */
@Entity
@Table(name = "sys_menu")
public class SysMenu extends BaseEntity<SysMenu> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 11, name = "parent_id", nullable = false)
    private Integer parentId; // pid为0即为首级
    @Column(length = 50, name = "menu_title", nullable = false)
    private String menuTitle;// 菜单名称
    @Column(length = 50, name = "menu_code", nullable = false)
    private String menuCode;// 菜单代码
    @Column
    private String menuUrl;//菜单链接
    @Column(length = 1, name = "code_type", nullable = false)
    private Integer codeType;// 代码类型1Page2Ajax
    @Column
    private String menuIcon;
    @Column
    private Integer sort;
    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;// 创建时间
    @Column(name = "update_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;// 更新时间
    @Transient
    private List<SysMenu> sysMenuChildList;//系统菜单子级
    @Transient
    private Integer isChecked;//0未选中1已选中
    @Transient
    private Integer isDesc;//是否倒序排序1是2否

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public Integer getCodeType() {
        return codeType;
    }

    public void setCodeType(Integer codeType) {
        this.codeType = codeType;
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

    public List<SysMenu> getSysMenuChildList() {
        return sysMenuChildList;
    }

    public void setSysMenuChildList(List<SysMenu> sysMenuChildList) {
        this.sysMenuChildList = sysMenuChildList;
    }

    public Integer getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Integer isChecked) {
        this.isChecked = isChecked;
    }

    public Integer getIsDesc() {
        return isDesc;
    }

    public void setIsDesc(Integer isDesc) {
        this.isDesc = isDesc;
    }

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}
    
}
