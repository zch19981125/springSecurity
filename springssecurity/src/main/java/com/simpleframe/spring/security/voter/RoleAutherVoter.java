package com.simpleframe.spring.security.voter;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

// ~ File Information
// ====================================================================================================================

public class RoleAutherVoter implements AccessDecisionVoter<Object> {

	// ~ Static Fields
	// ==================================================================================================================

	// ~ Fields
	// ==================================================================================================================

	// ~ Constructors
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================

	@Override
	public boolean supports(ConfigAttribute attribute) {

		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {

		return true;
	}

	/**
	 * 验证.
	 * 
	 * @see org.springframework.security.access.AccessDecisionVoter#vote(org.springframework.security.core.Authentication,
	 *      java.lang.Object, java.util.Collection)
	 */
	@Override
	public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
		
		FilterInvocation fi = (FilterInvocation) object;
		
		System.out.println("url:" + fi.getRequestUrl());

		if (authentication == null) {
			return ACCESS_DENIED;
		}
		
		int result = ACCESS_ABSTAIN;
		
		Collection<? extends GrantedAuthority> authorities = extractAuthorities(authentication);

		for (ConfigAttribute attribute : attributes) {
			if (attribute.getAttribute() == null) {
				continue;
			}
			if (this.supports(attribute)) {
				result = ACCESS_DENIED;

				// Attempt to find a matching granted authority
				for (GrantedAuthority authority : authorities) {
					if (attribute.getAttribute().equals(authority.getAuthority())) {
						return ACCESS_GRANTED;
					}
				}
			}
		}

		return result;
	}
	
	Collection<? extends GrantedAuthority> extractAuthorities(
      Authentication authentication) {
      return authentication.getAuthorities();
  }
}
