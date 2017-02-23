package com.project.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 分页实体
 * 
 * @author GuoZhiLong
 * @date 2015年2月5日 上午12:13:10
 * 
 * @param <T>
 */
public class BaseEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	// 前端选择的页
	private Integer pageNum;
	// 每页多少条
	private Integer numPerPage;
	// 每页多少条
	private String numPerPageStr;
	// 总数
	private int totalCount;
	// 当前页
	private Integer currentPage;

	// 关键字查询
	private String keyword;

	private List<T> list;

	public Integer getPageNum() {
		if (this.pageNum == null || this.pageNum.intValue() <= 0) {
			return 1;
		}
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getNumPerPage() {
		if (this.numPerPage == null || this.numPerPage.intValue() <= 0) {
			this.numPerPage = 10;// 默认显示页
		}
		return numPerPage;
	}

	/**
	 * mysql分页 获得开始
	 * 
	 * @author GuoZhiLong
	 * @return
	 * @return Integer
	 * @throws
	 */
	public Integer getStart() {
		return (this.getCurrentPage().intValue() - 1) * this.getNumPerPage();
	}

	public void setNumPerPage(Integer numPerPage) {
		this.numPerPage = numPerPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getCurrentPage() {
		if (this.pageNum == null || this.pageNum.intValue() <= 0) {
			return 1;
		}
		this.currentPage = this.pageNum;
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * 前端myPagination分页时使用，算出总页数
	 * 
	 * @Title: getTotalPage
	 * @author 郭志龙
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int getTotalPage() {
		if (numPerPage == 0) {
			numPerPage = 10;
		}
		if (totalCount == 0) {
			return 0;
		}
		return (totalCount + numPerPage - 1) / numPerPage;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public String getNumPerPageStr() {
		return numPerPageStr;
	}

	public void setNumPerPageStr(String numPerPageStr) {
		this.numPerPageStr = numPerPageStr;
	}

}
