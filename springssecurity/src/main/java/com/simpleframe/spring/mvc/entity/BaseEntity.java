package com.simpleframe.spring.mvc.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// ~ File Information
// ====================================================================================================================

/**
 * 基础实体类.
 * 
 * <pre>
 * 	基础实体类, 只是处理了json转换hibernate 懒加载问题.
 * </pre>
 * 
 * @author liutao
 * @since $Rev$
 *
 */
@JsonIgnoreProperties({
  "hibernateLazyInitializer",
  "handler"
})
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	// ~ Static Fields
	// ==================================================================================================================

	private static final long serialVersionUID = -479899587695555088L;

	// ~ Fields
	// ==================================================================================================================

	// ~ Constructors
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================
	
}
