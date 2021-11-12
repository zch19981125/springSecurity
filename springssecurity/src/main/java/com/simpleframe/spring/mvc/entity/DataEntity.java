package com.simpleframe.spring.mvc.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

// ~ File Information
// ====================================================================================================================

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class DataEntity extends BaseEntity {

	// ~ Static Fields
	// ==================================================================================================================

	private static final long serialVersionUID = -4908769947547854627L;

	// ~ Fields
	// ==================================================================================================================

	// 是否删除
	@Column(name = "is_deleted", columnDefinition = "char")
	@Type(type = "true_false")
	private boolean deleted;

	@Column(name = "create_by", updatable = false)
	@CreatedBy
	private String createBy;

	@Column(name = "update_by")
	@LastModifiedBy
	private String updateBy;

	@Column(name = "create_date", updatable = false)
	@CreatedDate
	private Date createDate;

	@Column(name = "update_date")
	@LastModifiedDate
	private Date updateDate;

	// ~ Constructors
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================


	/**
	 * @return 返回 deleted。
	 */
	public boolean isDeleted() {

		return deleted;
	}

	/**
	 * @param deleted
	 *          要设置的 deleted。
	 */
	public void setDeleted(boolean deleted) {

		this.deleted = deleted;
	}

	/**
	 * @return 返回 createBy。
	 */
	public String getCreateBy() {

		return createBy;
	}

	/**
	 * @param createBy
	 *          要设置的 createBy。
	 */
	public void setCreateBy(String createBy) {

		this.createBy = createBy;
	}

	/**
	 * @return 返回 updateBy。
	 */
	public String getUpdateBy() {

		return updateBy;
	}

	/**
	 * @param updateBy
	 *          要设置的 updateBy。
	 */
	public void setUpdateBy(String updateBy) {

		this.updateBy = updateBy;
	}

	/**
	 * @return 返回 createDate。
	 */
	public Date getCreateDate() {

		return createDate;
	}

	/**
	 * @param createDate
	 *          要设置的 createDate。
	 */
	public void setCreateDate(Date createDate) {

		this.createDate = createDate;
	}

	/**
	 * @return 返回 updateDate。
	 */
	public Date getUpdateDate() {

		return updateDate;
	}

	/**
	 * @param updateDate
	 *          要设置的 updateDate。
	 */
	public void setUpdateDate(Date updateDate) {

		this.updateDate = updateDate;
	}
}
