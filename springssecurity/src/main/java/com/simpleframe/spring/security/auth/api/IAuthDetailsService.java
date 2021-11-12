package com.simpleframe.spring.security.auth.api;

import java.util.LinkedHashMap;
import java.util.List;

// ~ File Information
// ====================================================================================================================

public interface IAuthDetailsService {

	// ~ Static Fields
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================

	/**
	 * 默认允许访问的菜单.
	 * 
	 * @return
	 */
	default String[] getDefaultAnonymous() {
		return new String[] {};
	}
	
	/**
	 * 默认允许访问的菜单.
	 * 
	 * @return
	 */
	default String[] getDefaultPermitAll() {
		return new String[] {"/logon/**", "/verify/**", "/webSocket/**"};
	}
	
	/**
	 * 获取所有允许访问的菜单.
	 * 
	 * @return
	 */
	String[] getPermitAll();
	
	/**
	 * 获取所有可匿名访问的菜单.
	 * 
	 * @return
	 */
	String[] getAnonymous();
	/**
	 * 获取权限及访问地址.
	 * 
	 * @return
	 */
	LinkedHashMap<String, String[]> getMatcherAccess();
	
	/**
	 * 获取匹配的菜单权限.
	 * 
	 * @param perms
	 * @return
	 */
	List<String> getMenuPerms(String perms);
}
