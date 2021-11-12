package com.simpleframe.spring.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.simpleframe.spring.security.access.AccessAuthenticationEntryPoint;
import com.simpleframe.spring.security.auth.api.IAuthDetailsService;
import com.simpleframe.spring.security.user.api.IUserProfileService;
import com.simpleframe.spring.security.voter.RoleAutherVoter;
import com.simpleframe.spring.token.filter.AuthTokenFilter;
import com.simpleframe.spring.token.provider.TokenProvider;

// ~ File Information
// ====================================================================================================================

/**
 * Spring安全认证策略.
 * 
 * <pre>
 * Spring安全认证策略
 * </pre>
 * 
 * @author liutao
 * @since $Rev$
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	// ~ Static Fields
	// ==================================================================================================================
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

	// ~ Fields
	// ==================================================================================================================

	@Autowired
	private IUserProfileService userDetailsService;

	@Autowired
	private IAuthDetailsService authDetailsService;
	
	@Autowired
	private TokenProvider tokenProvider;

	// ~ Constructors
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================

	/**
	 * 用户和密码策略等.
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
	 *      #configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		// 配置用户密码和角色
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	/**
	 * 配置角色和访问权限.
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
	 *      #configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// 使用jwt, 暂时不需要csrf
		http.csrf().disable();
		
	  // 基于token, 暂时不需要session
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll();
    
    // 所有用户可以访问
    logger.info(String.format("加载无条件权限,%s", StringUtils.join(authDetailsService.getPermitAll(), ",")));
    http.authorizeRequests().antMatchers(authDetailsService.getPermitAll()).permitAll();

    // 允许匿名登录
		logger.info(String.format("加载匿名权限,%s", StringUtils.join(authDetailsService.getAnonymous(), ",")));
    http.authorizeRequests().antMatchers(authDetailsService.getAnonymous()).anonymous();
		
		// 动态读取
		Map<String, String[]> access = this.authDetailsService.getMatcherAccess();
		
		if (access != null && access.size() > 0) {
			for (String url : access.keySet()) {
				
				logger.info(String.format("加载权限,%s,%s", url, StringUtils.join(access.get(url), ",")));
				http.authorizeRequests().antMatchers(url).hasAnyAuthority(access.get(url));
			}
		}
		
		// 自定义判断
//		http.authorizeRequests().accessDecisionManager(accessDecisionManager());
		
		// 其他未设置的地址都需要认证
	  http.authorizeRequests().anyRequest().authenticated();
		
		// 处理权限返回信息
		http.exceptionHandling().authenticationEntryPoint(new AccessAuthenticationEntryPoint());
		
		// 放权集合
		List<String> urlMatchers = new ArrayList<String>();
		CollectionUtils.addAll(urlMatchers, authDetailsService.getPermitAll());
		CollectionUtils.addAll(urlMatchers, authDetailsService.getAnonymous());

		// 增加token验证过滤
		AuthTokenFilter tokenFileter = new AuthTokenFilter(userDetailsService, tokenProvider, urlMatchers);
		http.addFilterBefore(tokenFileter, UsernamePasswordAuthenticationFilter.class);
	}

	/**
	 * 认证管理器.
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#authenticationManagerBean()
	 */
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	/**
	 * 自定义权限管理器.
	 * 
	 * @return
	 */
	@Bean
	public AccessDecisionManager accessDecisionManager() {

		List<AccessDecisionVoter<? extends Object>> decisionVoters = Arrays.asList(new WebExpressionVoter(), new RoleAutherVoter(), new AuthenticatedVoter());
		return new UnanimousBased(decisionVoters);
	}

	/**
	 * 密码加密策略.
	 * 
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}
	
	/**
	 * 用户实现.
	 * 
	 * @return
	 */
	@Bean
	public IUserProfileService userProfileService() {
		return new IUserProfileService() {
			
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				
				return null;
			}
		};
	}
}
