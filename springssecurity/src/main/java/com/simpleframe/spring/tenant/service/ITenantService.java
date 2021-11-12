package com.simpleframe.spring.tenant.service;

import com.simpleframe.spring.tenant.TenantProfile;

// ~ File Information
// ====================================================================================================================

public interface ITenantService {

	// ~ Static Fields
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================

	/**
	 * 获取当前用户的机构信息.
	 * 
	 * <pre>
	 * 如果tenantId不存在, 则获取当前
	 * </pre>
	 * @tenantId 
	 * @return
	 */
	TenantProfile getTenantProfile(String tenantId);
	
	/**
	 * 获取当前用户的机构信息.
	 * 
	 * @return
	 */
	TenantProfile getTenantProfile();
}
