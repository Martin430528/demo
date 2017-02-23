package com.project.entity.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 中国大陆地区
 * 
 * @author GuoZhiLong
 * @date 2015年10月31日 上午1:29:11
 * 
 */
@Entity
@Table(name = "region")
public class Region {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "REGION_ID")
	private Integer id;
	@Column(name = "REGION_CODE", length = 100)
	private String regionCode;
	@Column(name = "REGION_NAME", length = 100)
	private String name;
	@Column(name = "PARENT_ID")
	private Double parentId;
	@Column(name = "REGION_LEVEL")
	private Double regionLevel;
	@Column(name = "REGION_ORDER")
	private Double regionOrder;
	@Column(name = "REGION_NAME_EN", length = 100)
	private String regionNameEn;
	@Column(name = "REGION_SHORTNAME_EN", length = 10)
	private String regionShortNameEn;
	@Column(name = "REGION_STATE")
	private Integer regionState;

	
	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getParentId() {
		return parentId;
	}

	public void setParentId(Double parentId) {
		this.parentId = parentId;
	}

	public Double getRegionLevel() {
		return regionLevel;
	}

	public void setRegionLevel(Double regionLevel) {
		this.regionLevel = regionLevel;
	}

	public Double getRegionOrder() {
		return regionOrder;
	}

	public void setRegionOrder(Double regionOrder) {
		this.regionOrder = regionOrder;
	}

	public String getRegionNameEn() {
		return regionNameEn;
	}

	public void setRegionNameEn(String regionNameEn) {
		this.regionNameEn = regionNameEn;
	}

	public String getRegionShortNameEn() {
		return regionShortNameEn;
	}

	public void setRegionShortNameEn(String regionShortNameEn) {
		this.regionShortNameEn = regionShortNameEn;
	}

	public Integer getRegionState() {
		return regionState;
	}

	public void setRegionState(Integer regionState) {
		this.regionState = regionState;
	}
}
