package com.simpleframe.spring.security.auth;

import org.apache.commons.lang3.StringUtils;

// ~ File Information
// ====================================================================================================================

/**
 * 权限明细.
 * 
 * <pre>
 * 权限明细
 * </pre>
 * 
 * @author liutao
 * @since $Rev$
 *
 */
public class AuthDetails {

	// ~ Static Fields
	// ==================================================================================================================

	public static final String TYPE_ROLES = "roles";

	public static final String TYPE_ANY_ROLES = "any_roles";

	public static final String TYPE_AUTHORITIES = "authorities";

	public static final String TYPE_ANY_AUTHORITIES = "any_authorities";

	// ~ Fields
	// ==================================================================================================================

	private String[] matchers;

	private String[] roles;

	private String detailyType;

	// ~ Constructors
	// ==================================================================================================================

	public AuthDetails() {

	}

	public AuthDetails(String[] matchers, String[] roles, String detailyType) {

		this.matchers = matchers;
		this.roles = roles;
		this.detailyType = detailyType;
	}

	// ~ Methods
	// ==================================================================================================================
	
	/**
	 * 是否使用access配置.
	 * 
	 * @return
	 */
	public boolean isAccess() {
		return StringUtils.equalsAny(this.getDetailyType(), TYPE_ROLES, TYPE_AUTHORITIES);
	}
	
	/**
	 * 获取access权限.
	 * 
	 * @return
	 */
	public String[] getAccessArray() {
		
		String[] access = new String[] {};
		
		// 多角色
		if (StringUtils.equals(TYPE_ROLES, this.getDetailyType())) {
			
		}
		// 多权限
		else if (StringUtils.equals(TYPE_AUTHORITIES, this.getDetailyType())) {
			
		}

		return access;
	}
	
	public String getAccessString() {
		
		String access = "";
		
		// 单角色
		if (StringUtils.equals(TYPE_ANY_ROLES, this.getDetailyType())) {
			
		}
		// 单权限
		else if (StringUtils.equals(TYPE_ANY_AUTHORITIES, this.getDetailyType())) {
			
		}
		
		return access;
	}

	/**
	 * @return 返回 matchers。
	 */
	public String[] getMatchers() {

		return matchers;
	}

	/**
	 * @param matchers
	 *          要设置的 matchers。
	 */
	public void setMatchers(String[] matchers) {

		this.matchers = matchers;
	}

	/**
	 * @return 返回 roles。
	 */
	public String[] getRoles() {

		return roles;
	}

	/**
	 * @param roles
	 *          要设置的 roles。
	 */
	public void setRoles(String[] roles) {

		this.roles = roles;
	}

	/**
	 * @return 返回 detailyType。
	 */
	public String getDetailyType() {

		return detailyType;
	}

	/**
	 * @param detailyType
	 *          要设置的 detailyType。
	 */
	public void setDetailyType(String detailyType) {

		this.detailyType = detailyType;
	}
}
