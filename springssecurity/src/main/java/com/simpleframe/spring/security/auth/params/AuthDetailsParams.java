package com.simpleframe.spring.security.auth.params;

// ~ File Information
// ====================================================================================================================

public class AuthDetailsParams {

	// ~ Static Fields
	// ==================================================================================================================

	// ~ Fields
	// ==================================================================================================================

	// ~ Constructors
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================

	public class AuthRole {
		
		// 超级管理员
		public static final String SUPER_ADMIN = "SUPER_ADMIN";
		
		// 监管管理员
		public static final String MANAGE_ADMIN = "MANAGE_ADMIN";
		
		// 普通用户
		public static final String NORMAL_USER = "NORMAL_USER";
		
		// 未认证用户
		public static final String UN_CERT_NORMAL_USER = "UN_CERT_NORMAL_USER";
		
		// 匿名用户
		public static final String ANONYMOUS = "anonymous";
	}
}
