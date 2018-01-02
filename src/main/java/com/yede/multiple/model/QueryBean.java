package com.yede.multiple.model;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

public class QueryBean extends GenericObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String ASCENDING_ORDER = "asc";
	public static final String DESCENDING_ORDER = "desc";
	private static final String DEFAULT_ORDER = DESCENDING_ORDER;
	private static final String DEFAULT_ORDER_BY = "id";
	private static final int DEFAULT_PAGE_START = 1;
	private static final int DEFAULT_PAGE_SIZE = 20;
	public static final int MAX_PAGE_SIZE = 999999;
	
	private String orderBy = DEFAULT_ORDER_BY;
	@Getter
	@Setter
	private String order = DEFAULT_ORDER;
	@Getter
	@Setter
	@Min(value = 1, message = "当前页码不合法")
	private int pageStart = DEFAULT_PAGE_START;

	@Getter
	@Setter
	@Min(value = 1, message = "每页展示数量不合法")
	private int pageSize = DEFAULT_PAGE_SIZE;

	private boolean deleted = false;
	
	
	
	public QueryBean() {
		
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getPageStart() {
		return pageStart;
	}

	public void setPageStart(int pageStart) {
		this.pageStart = pageStart;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
