package com.simpleframe.spring.token.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.simpleframe.boot.common.exception.ServiceException;
import com.simpleframe.spring.security.user.api.IUserProfileService;
import com.simpleframe.spring.token.provider.TokenProvider;

// ~ File Information
// ====================================================================================================================

public class AuthTokenFilter extends OncePerRequestFilter {

	// ~ Static Fields
	// ==================================================================================================================

	private final static String XAUTH_TOKEN_HEADER_NAME = "auth-token";
	
	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	// ~ Fields
	// ==================================================================================================================

	private IUserProfileService userDetailsService;
	
	private TokenProvider tokenProvider;
	
	private List<AntPathRequestMatcher> urlMathers = new ArrayList<AntPathRequestMatcher>();

	// ~ Constructors
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================

	public AuthTokenFilter(IUserProfileService detailsService, TokenProvider tokenProvider) {

		this.userDetailsService = detailsService;
		this.tokenProvider = tokenProvider;
	}
	
	public AuthTokenFilter(IUserProfileService detailsService, TokenProvider tokenProvider, List<String> urlMathers) {

		this.userDetailsService = detailsService;
		this.tokenProvider = tokenProvider;
		
		if (urlMathers != null && urlMathers.size() > 0) {
			
			for (String urlMater: urlMathers) {
				this.urlMathers.add(new AntPathRequestMatcher(urlMater));
			}
		}
	}
	
	/**
	 * 过滤验证token.
	 * 
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	    throws ServletException, IOException {
		
		// 如果没有匹配的URL则走token验证
		if (!this.pathMatcher(request)) {
			
			// 先从header中取token
			String authToken = request.getHeader(XAUTH_TOKEN_HEADER_NAME);
			
			// 验证地址是否是匿名或者授权可访问地址
			if (org.springframework.util.StringUtils.hasText(authToken)) {
				
				// 从自定义tokenProvider中解析用户
				String username = this.tokenProvider.getUsername(authToken);
				
				logger.info("获取当前token, 解析用户:" + username);

				if (StringUtils.isNotBlank(username)) {
					
					UserDetails userProfile = userDetailsService.loadUserByUsername(username);
					
					if (userProfile == null) {
						throw new ServiceException("该token已过期或用户已登出");
					}
					
					// 用户不能为空, 且token未过期
					else if (userProfile != null) {
						
						// 重新组装token
						UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userProfile, userProfile.getPassword(), userProfile.getAuthorities());

						// 这里还是上面见过的，存放认证信息，如果没有走这一步，下面的doFilter就会提示登录了
						SecurityContextHolder.getContext().setAuthentication(token);
					}
				}
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
	/**
	 * 请求地址验证, 是否可访问.
	 * 
	 * @param request
	 * @return
	 */
	public boolean pathMatcher(HttpServletRequest request) {
		
		if (urlMathers != null && urlMathers.size() > 0) {
			
			for (AntPathRequestMatcher urlMater: urlMathers) {
				if (urlMater.matches(request)) {
					return true;
				}
			}
		}
		
		return false;
	} 
}
