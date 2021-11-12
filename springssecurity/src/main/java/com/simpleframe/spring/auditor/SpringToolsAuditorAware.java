package com.simpleframe.spring.auditor;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.simpleframe.spring.security.user.UserProfile;
import com.simpleframe.spring.security.user.UserProfileContext;

// ~ File Information
// ====================================================================================================================

/**
 * 用来处理基础信息注解等信息.
 * 
 * <pre>
 * 	用来处理基础信息注解等信息
 * </pre>
 * 
 * @author liutao
 * @since $Rev$
 *
 */
@Component
public class SpringToolsAuditorAware implements AuditorAware<String> {

	// ~ Static Fields
	// ==================================================================================================================

	// ~ Fields
	// ==================================================================================================================

	// ~ Constructors
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================

	/**
	 * 获取用户ID.
	 * 
	 * @see org.springframework.data.domain.AuditorAware#getCurrentAuditor()
	 */
	@Override
	public Optional<String> getCurrentAuditor() {
		
		UserProfile user =	UserProfileContext.getUserProfile();
		
		if (user != null) {
			return Optional.ofNullable(user.getUserId());
		}
		return Optional.empty();
	}
}
