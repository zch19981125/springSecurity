package com.simpleframe.spring.security.filter;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.simpleframe.spring.token.provider.TokenProvider;

// ~ File Information
// ====================================================================================================================

public class UsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {

	// ~ Static Fields
	// ==================================================================================================================

	// ~ Fields
	// ==================================================================================================================

	private UserDetailsService userDetailsService;

//	private TokenProvider tokenProvider;

	// ~ Constructors
	// ==================================================================================================================

	public UsernamePasswordAuthFilter(UserDetailsService detailsService, TokenProvider tokenProvider) {
		this.userDetailsService = detailsService;
//		this.tokenProvider = tokenProvider;
	}

	// ~ Methods
	// ==================================================================================================================

	/**
	 * 验证信息.
	 * 
	 * @see org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter#attemptAuthentication(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		
		System.out.println(">>>>>>username>>>>>>>");

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (StringUtils.isAnyBlank(username, password)) {
			throw new AuthenticationServiceException("用户名或密码不能为空");
		}
		
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
		
		if (userDetails == null) {
			throw new AuthenticationServiceException("用户名或密码错误");
		}

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		authRequest.setDetails(request);
		
		// 默认认证成功
		final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();
		
		AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
		return new UsernamePasswordAuthenticationToken(authRequest.getPrincipal(), authRequest.getCredentials(), AUTHORITIES);
	}
}
