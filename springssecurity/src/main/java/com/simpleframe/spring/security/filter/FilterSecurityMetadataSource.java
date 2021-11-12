package com.simpleframe.spring.security.filter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

// ~ File Information
// ====================================================================================================================

public class FilterSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	// ~ Static Fields
	// ==================================================================================================================

	// ~ Fields
	// ==================================================================================================================

	private FilterInvocationSecurityMetadataSource superMetadataSource;

	private final AntPathMatcher antPathMatcher = new AntPathMatcher();

	// 这里的需要从DB加载
	private final Map<String, String> defaultRoleMap = new HashMap<String, String>() {

		private static final long serialVersionUID = -2684160476944735806L;

		{
			put("/logon/**", "ROLE_ANONYMOUS");
			put("/verify", "ROLE_ANONYMOUS");
		}
	};

	// ~ Constructors
	// ==================================================================================================================

	public FilterSecurityMetadataSource(FilterInvocationSecurityMetadataSource superMetadataSource) {

		this.superMetadataSource = superMetadataSource;

		// TODO 从数据库加载权限配置
	}

	// ~ Methods
	// ==================================================================================================================

	/**
	 * 获取所有配置项.
	 * 
	 * @see org.springframework.security.access.SecurityMetadataSource#getAllConfigAttributes()
	 */
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {

		if (superMetadataSource != null) {
			return superMetadataSource.getAllConfigAttributes();
		}

		return null;
	}

	/**
	 * 验证请求地址.
	 * 
	 * @see org.springframework.security.access.SecurityMetadataSource#getAttributes(java.lang.Object)
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

		FilterInvocation fi = (FilterInvocation) object;
		String url = fi.getRequestUrl();

		System.out.println(">>>>>>>>>url:" + url);
		
		for (ConfigAttribute attr : getAllConfigAttributes()) {
			System.out.println(">>>value:" + attr.getClass().getName());
		}

		for (Map.Entry<String, String> entry : defaultRoleMap.entrySet()) {

			if (antPathMatcher.match(entry.getKey(), url)) {
				return SecurityConfig.createList(entry.getValue());
			}
		}

		// 返回代码定义的默认配置
		return superMetadataSource.getAttributes(object);
	}

	@Override
	public boolean supports(Class<?> clazz) {

		return FilterInvocation.class.isAssignableFrom(clazz);
	}
}
