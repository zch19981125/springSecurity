package com.simpleframe.spring.security.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.simpleframe.boot.common.annotation.authorize.AccessAnonymous;
import com.simpleframe.boot.common.annotation.authorize.AccessAuthority;
import com.simpleframe.boot.common.annotation.authorize.AccessPermitAll;
import com.simpleframe.spring.security.auth.api.IAuthDetailsService;

// ~ File Information
// ====================================================================================================================

@Service
public class AuthDetailsService implements IAuthDetailsService {

	// ~ Static Fields
	// ==================================================================================================================

	// 保存权限
	private final Map<String, Object> access = new HashMap<String, Object>();

	// ~ Fields
	// ==================================================================================================================

	@Autowired
	private WebApplicationContext applicationContext;

	// ~ Constructors
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================

	/**
	 * 获取所有可无条件访问的地址.
	 * 
	 * @see com.simpleframe.spring.auth.IAuthDetailsService#getPermitAll()
	 */
	@Override
	public String[] getPermitAll() {

		// 返回结果
		@SuppressWarnings("unchecked")
		Set<String> permitAll = (Set<String>) MapUtils.getObject(this.getAccess(), "permitAll", new HashSet<String>());

		String[] defaultPermit = this.getDefaultPermitAll();

		if (defaultPermit != null && defaultPermit.length > 0) {
			for (String permit : defaultPermit) {
				permitAll.add(permit);
			}
		}

		return permitAll.toArray(new String[permitAll.size()]);
	}

	/**
	 * 获取匿名访问url地址.
	 * 
	 * @see com.simpleframe.spring.auth.IAuthDetailsService#getAnonymous()
	 */
	@Override
	public String[] getAnonymous() {

		// 返回结果
		@SuppressWarnings("unchecked")
		Set<String> anonymous = (Set<String>) MapUtils.getObject(this.getAccess(), "anonymous", new HashSet<String>());

		String[] defaultAnonymous = this.getDefaultAnonymous();

		if (defaultAnonymous != null && defaultAnonymous.length > 0) {
			for (String anm : defaultAnonymous) {
				anonymous.add(anm);
			}
		}

		return anonymous.toArray(new String[anonymous.size()]);
	}

	/**
	 * 获取菜单下所有权限.
	 * 
	 * @param perms
	 * @return
	 */
	@Override
	public List<String> getMenuPerms(String perms) {

		// 记录权限
		List<String> results = new ArrayList<String>();

		if (StringUtils.isBlank(perms)) {
			return results;
		}

		// 通过bean获取controller 请求地址
		RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);

		// 获取url与类和方法的对应信息
		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

		for (Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {

			RequestMappingInfo requestInfo = m.getKey();
			HandlerMethod method = m.getValue();
			String className = method.getMethod().getDeclaringClass().getName();

			PatternsRequestCondition pattern = requestInfo.getPatternsCondition();
			String reqUrl = "";

			// 获取请求地址
			for (String url : pattern.getPatterns()) {
				reqUrl = url;
			}

			if (StringUtils.startsWith(className, "com.simpleframe.module.")) {

				className = StringUtils
				  .replace(method.getMethod().getDeclaringClass().getName(), "com.simpleframe.module.", "");
				String moduleName = StringUtils.substring(className, 0, StringUtils.indexOf(className, "."));
				String perm = StringUtils.replace(moduleName + reqUrl, "/", ":");

				if (StringUtils.indexOf(perm, perms) == 0) {
					results.add(perm);
				}
			}
		}

		return results;
	}

	/**
	 * 获取权限信息.
	 * 
	 * @return
	 */
	private Map<String, Object> getAccess() {

		if (this.access != null && this.access.size() > 0) {
			return this.access;
		}

		// 记录返回结果
		LinkedHashMap<String, String[]> results = new LinkedHashMap<String, String[]>();
		Set<String> permitAll = new HashSet<String>();
		Set<String> anonymous = new HashSet<String>();

		// 通过bean获取controller 请求地址
		RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);

		// 获取url与类和方法的对应信息
		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

		// 记录权限
		Set<String> authories = null;

		for (Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {

			RequestMappingInfo requestInfo = m.getKey();
			HandlerMethod method = m.getValue();

			// 判断是否为无条件授权地址
			AccessPermitAll accessPermitAll = m.getValue().getMethodAnnotation(AccessPermitAll.class);
			if (accessPermitAll != null) {

				PatternsRequestCondition pattern = requestInfo.getPatternsCondition();
				String reqUrl = "";

				// 获取请求地址
				for (String url : pattern.getPatterns()) {
					reqUrl = url;
				}

				// 判断如果请求地址为空则不进行处理
				if (StringUtils.isBlank(reqUrl)) {
					continue;
				}

				permitAll.add(reqUrl);

				continue;
			}

			// 判断是否为匿名用户
			AccessAnonymous accessAnonymous = m.getValue().getMethodAnnotation(AccessAnonymous.class);
			if (accessAnonymous != null) {

				PatternsRequestCondition pattern = requestInfo.getPatternsCondition();
				String reqUrl = "";

				// 获取请求地址
				for (String url : pattern.getPatterns()) {
					reqUrl = url;
				}

				// 判断如果请求地址为空则不进行处理
				if (StringUtils.isBlank(reqUrl)) {
					continue;
				}

				anonymous.add(reqUrl);

				continue;
			}

			// 获取权限
			AccessAuthority aeessAuthority = m.getValue().getMethodAnnotation(AccessAuthority.class);

			if (aeessAuthority != null) {

				PatternsRequestCondition pattern = requestInfo.getPatternsCondition();
				String reqUrl = "";

				// 获取请求地址
				for (String url : pattern.getPatterns()) {
					reqUrl = url;
				}

				// 判断如果请求地址为空则不进行处理
				if (StringUtils.isBlank(reqUrl)) {
					continue;
				}

				authories = new HashSet<String>();

				// 判断是否有默认角色
				if (aeessAuthority.roles() != null && aeessAuthority.roles().length > 0) {
					for (String role : aeessAuthority.roles()) {
						authories.add(StringUtils.startsWith(role, "ROLE_") ? role : ("ROLE_" + role));
					}
				}

				// 判断是否有默认权限, 如果有默认权限则不加入其他权限
				if (StringUtils.isNotBlank(aeessAuthority.authority())) {
					authories.add(aeessAuthority.authority());
				}
				// 否则通过url去加载
				else {

					String className = method.getMethod().getDeclaringClass().getName();

					if (StringUtils.startsWith(className, "com.simpleframe.module.")) {

						className = StringUtils
						  .replace(method.getMethod().getDeclaringClass().getName(), "com.simpleframe.module.", "");
						String moduleName = StringUtils.substring(className, 0, StringUtils.indexOf(className, "."));
						String perm = StringUtils.replace(moduleName + reqUrl, "/", ":");

						authories.add(perm);
					}
				}

				// 判断authories是否为空, 为空则不进行判权
				if (authories != null && authories.size() > 0) {
					results.put(reqUrl, authories.toArray(new String[authories.size()]));
				}
			}
		}

		access.put("permitAll", permitAll);
		access.put("anonymous", anonymous);
		access.put("authority", results);

		return access;
	}

	/**
	 * 获取权限地址和访问权限.
	 * 
	 * @see com.simpleframe.spring.security.auth.api.IAuthDetailsService#getMatcherAccess()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public LinkedHashMap<String, String[]> getMatcherAccess() {

		return (LinkedHashMap<String, String[]>) MapUtils
		  .getObject(this.getAccess(), "authority", new LinkedHashMap<String, String[]>());
	}
}
