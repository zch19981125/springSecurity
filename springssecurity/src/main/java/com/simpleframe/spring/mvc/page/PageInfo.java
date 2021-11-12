package com.simpleframe.spring.mvc.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.fasterxml.jackson.annotation.JsonIgnore;

// ~ File Information
// ====================================================================================================================

/**
 * page分页相关类.
 * 
 * <pre>
 * page分页相关
 * </pre>
 * 
 * @author liutao
 * @since $Rev$
 *
 * @param <T>
 */
public class PageInfo<T> implements Serializable {

	// ~ Static Fields
	// ==================================================================================================================

	private static final long serialVersionUID = -3863319312280575486L;

	private static final Integer DEFAULT_PAGE_SIZE = 10;

	private static final String SORT_NORMAL = "normal";
	private static final String SORT_DESC = "desc";

	// ~ Fields
	// ==================================================================================================================

	// 数据集合
	private List<T> content;

	// 当前页
	private Integer pageNumber;

	// 分页展示数量
	private Integer pageSize;

	// 数据总数
	private Long total;

	// 排序的栏位
	private String sortKey;

	// 排序方式
	private String sortOrder;

	// 默认排序栏位
	private String defaultSortKey;

	// 默认排序方式
	private String defaultSortOrder;

	// ~ Constructors
	// ==================================================================================================================

	public PageInfo() {
		this.content = new ArrayList<>();
		this.pageNumber = 1;
		this.pageSize = DEFAULT_PAGE_SIZE;
	}

	public PageInfo(Page<T> page) {

		this.content = page.getContent();
		this.total = page.getTotalElements();

		if (page.getPageable() != null) {
			this.pageSize = page.getPageable().getPageSize();
			this.pageNumber = page.getPageable().getPageNumber() + 1;
		}
	}

	// ~ Methods
	// ==================================================================================================================

	public void setPage(Page<T> page) {

		this.content = page.getContent();
		this.total = page.getTotalElements();

		if (page.getPageable() != null) {
			this.pageSize = page.getPageable().getPageSize();
			this.pageNumber = page.getPageable().getPageNumber() + 1;
		}
	}
	
	public void setPage(List<T> contents, long total) {
		this.content = contents;
		this.total = total;
	}

	public List<T> getContent() {

		return content;
	}

	public void setContent(List<T> content) {

		this.content = content;
	}

	public Integer getPageNumber() {

		if (this.pageNumber == null || this.pageNumber <= 0) {
			this.pageNumber = 1;
		}

		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {

		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {

		if (this.pageSize == null || this.pageSize <= 0) {
			this.pageSize = DEFAULT_PAGE_SIZE;
		}

		return pageSize;
	}

	public void setPageSize(Integer pageSize) {

		this.pageSize = pageSize;
	}

	public Long getTotal() {

		return total;
	}

	public void setTotal(Long total) {

		this.total = total;
	}

	public String getSortKey() {

		return sortKey;
	}

	public void setSortKey(String sortKey) {

		this.sortKey = sortKey;
	}

	public String getSortOrder() {

		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {

		this.sortOrder = sortOrder;
	}

	/**
	 * 获取分页排序.
	 * 
	 * @return
	 */
	@JsonIgnore
	public Sort getPageSort() {

		Sort sort = null;
		
		List<Order> orders = new ArrayList<Order>();

		// 加入其它排序条件
		if (StringUtils.isNoneBlank(this.sortKey, this.sortOrder)
		    && !StringUtils.equalsAnyIgnoreCase(SORT_NORMAL, this.sortOrder)) {
			if (StringUtils.equalsAnyIgnoreCase(SORT_DESC, this.sortOrder)) {
				orders.add(new Order(Sort.Direction.DESC, this.sortKey));
			} else {
				orders.add(new Order(Sort.Direction.ASC, this.sortKey));
			}
		}
		
		// 加入默认条件
		if (StringUtils.isNoneBlank(this.defaultSortKey, this.defaultSortOrder)) {
			if (StringUtils.equalsAnyIgnoreCase(SORT_DESC, this.defaultSortOrder)) {
				orders.add(new Order(Sort.Direction.DESC, this.defaultSortKey));
			} else {
				orders.add(new Order(Sort.Direction.ASC, this.defaultSortKey));
			}
		}
		
		if (orders != null && orders.size() > 0) {
			sort = Sort.by(orders);
		} else {
			sort = Sort.unsorted();
		}

		return sort;
	}
	
	/**
	 * 增加排序.
	 * 
	 * @return
	 */
	@JsonIgnore
	public String getOrderby() {
		
		String orderBy = "";
		
		if (StringUtils.isNoneBlank(this.sortKey, this.sortOrder)) {
			if (StringUtils.equalsAnyIgnoreCase(SORT_DESC, this.sortOrder)) {
				orderBy += this.sortKey + " desc ";
				
			} else {
				orderBy += this.sortKey + " asc ";
			}
		}
		
		// 加入其它排序条件
		if (StringUtils.isNoneBlank(this.defaultSortKey, this.defaultSortOrder)
		    && !StringUtils.equalsAnyIgnoreCase(SORT_NORMAL, this.defaultSortOrder)) {
			if (StringUtils.isNotBlank(orderBy)) {
				orderBy += ", ";
			}
			
			if (StringUtils.equalsAnyIgnoreCase(SORT_DESC, this.defaultSortOrder)) {
				orderBy += this.defaultSortKey + " desc ";
			} else {
				orderBy += this.defaultSortKey + " asc ";
			}
		}
		
		if (StringUtils.isNotBlank(orderBy)) {
			return " order by " + orderBy;
		}
		
		return orderBy;
	}

	/**
	 * pageable用来分页使用.
	 * 
	 * @return
	 */
	@JsonIgnore
	public Pageable getPageable() {
		
		return PageRequest.of(getPageNumber() - 1, this.pageSize, this.getPageSort());
	}

	public String getDefaultSortKey() {

		return defaultSortKey;
	}

	public void setDefaultSortKey(String defaultSortKey) {

		this.defaultSortKey = defaultSortKey;
	}

	public String getDefaultSortOrder() {

		return defaultSortOrder;
	}

	public void setDefaultSortOrder(String defaultSortOrder) {

		this.defaultSortOrder = defaultSortOrder;
	}
}
