package com.simpleframe.spring.security.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// ~ File Information
// ====================================================================================================================

public class UserProfileContext {

	// ~ Static Fields
	// ==================================================================================================================
	
	private static final Logger logger = LoggerFactory.getLogger(UserProfileContext.class);

	// ~ Fields
	// ==================================================================================================================

	// ~ Constructors
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================

	/**
	 * 获取认证.
	 * 
	 * @return
	 */
	private static Authentication getAuthentication() {
		
		Authentication auth = null;
		
		try {
			auth = SecurityContextHolder.getContext().getAuthentication();
		} catch (Exception e) {
			logger.info("未获取到上下文信息, " + e.getMessage());
		}
		return auth;
	}
	
	/**
	 * 获取用户信息.
	 * 
	 * @return
	 */
	public static UserProfile getUserProfile() {
		
		Authentication auth = getAuthentication();
		
		if (auth != null) {
			// 获取用户信息
			Object principal = auth.getPrincipal();
			if (principal instanceof UserProfile) {
				return (UserProfile) principal;
			}
		}
		
		return null;
	}
	
	/**
	 * 获取当前登陆机构ID.
	 * 
	 * @return
	 */
	public static String getTenantId() {
		UserProfile user = getUserProfile();
		if (user != null) {
			return user.getTenantId();
		}
		return null;
	}
}
