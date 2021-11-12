package com.simpleframe.spring.tenant;

import org.apache.commons.lang3.StringUtils;

import com.simpleframe.spring.context.SpringContextHolder;
import com.simpleframe.spring.security.user.UserProfile;
import com.simpleframe.spring.security.user.UserProfileContext;
import com.simpleframe.spring.tenant.service.ITenantService;

// ~ File Information
// ====================================================================================================================

public class TenantProfileContext {

	// ~ Static Fields
	// ==================================================================================================================

	// ~ Fields
	// ==================================================================================================================

	// ~ Constructors
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================

	/**
	 * 获取机构主键.
	 * 
	 * @return
	 */
	public static String getTenantId() {
		
		UserProfile user = UserProfileContext.getUserProfile();
		
		if (user != null) {
			
			// 如果是普通用户的情况下, 需要查看是是否存在tenantId
			if (user.isSuperAdmin()) {
				return null;
			}
			
			// 如果当前机构ID是空, 则返回
			else if (StringUtils.isNotBlank(user.getTenantId())) {
				return user.getTenantId();
			}
		}
		
		return "NO_TENANT";
	}
	
	/**
	 * 获取当前登录用户机构信息.
	 * 
	 * @return
	 */
	public static TenantProfile getTenantProfile() {
		return SpringContextHolder.getBean(ITenantService.class).getTenantProfile();
	}
	
	/**
	 * 获取当前登录用户机构信息.
	 * 
	 * @param tenantId
	 * @return
	 */
	public static TenantProfile getTenantProfile(String tenantId) {
		return SpringContextHolder.getBean(ITenantService.class).getTenantProfile(tenantId);
	}
}
