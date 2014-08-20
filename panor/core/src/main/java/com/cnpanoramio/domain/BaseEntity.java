package com.cnpanoramio.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;


@MappedSuperclass
public abstract class BaseEntity implements Serializable, CreateModifiable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5571215461166687799L;

	/** 创建日期 */
	@Column(name="create_date", nullable = false, updatable = false)
	private Date createDate;

	/** 修改日期 */
	@Column(name="modify_date", nullable = false)
	private Date modifyDate;

	/**
	 * 获取创建日期
	 * 
	 * @return 创建日期
	 */
	@JsonProperty
	@Field(store = Store.YES, index = Index.NO)
	@DateBridge(resolution = Resolution.SECOND)
	
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * 设置创建日期
	 * 
	 * @param createDate
	 *            创建日期
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 获取修改日期
	 * 
	 * @return 修改日期
	 */
	@JsonProperty
	@Field(store = Store.YES, index = Index.NO)
	@DateBridge(resolution = Resolution.SECOND)
	
	public Date getModifyDate() {
		return modifyDate;
	}

	/**
	 * 设置修改日期
	 * 
	 * @param modifyDate
	 *            修改日期
	 */
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
}
