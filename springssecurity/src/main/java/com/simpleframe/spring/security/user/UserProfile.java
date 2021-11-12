package com.simpleframe.spring.security.user;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simpleframe.boot.common.exception.ServiceException;
import com.simpleframe.boot.common.param.StatusConst;
import com.simpleframe.spring.security.user.api.IUserProfile;
import com.simpleframe.spring.tenant.TenantProfile;
import com.simpleframe.spring.tenant.TenantProfileContext;

// ~ File Information
// ====================================================================================================================

/**
 * 用户对象.
 * 
 * <pre>
 * 用户对象
 * </pre>
 * 
 * @author liutao
 * @since $Rev$
 *
 */
public class UserProfile implements UserDetails {

	// ~ Static Fields
	// ==================================================================================================================

	private static final long serialVersionUID = -7902219051770341636L;

	// ~ Fields
	// ==================================================================================================================

	// 权限
	private Collection<? extends GrantedAuthority> authorities;

	private String userId;

	// 用户信息
	private String username;

	private String nickName;

	private String telephone;

	@JsonIgnore
	private String password;

	private String avatar;

	private String sign;

	private String tenantId;

	// 允许PC端登录
	private boolean allowPC;

	// 允许移动端登录
	private boolean allowApp;

	// 允许微信端登录
	private boolean allowWechat;

	// 允许其他终端登录
	private boolean allowOther;

	// 账号锁定
	private boolean accountNonLocked;

	// 过期
	private boolean accountNonExpired;

	// 证书
	private boolean credentialsNonExpired;

	// 启用
	private boolean enabled;

	// 用户类型
	private String userType;

	// 用户访问的token
	private String token;

	// 用户可访问菜单
	private Set<String> access;
	
	// 机构信息
	@JsonIgnore
	private TenantProfile tenantProfile;

	// ~ Constructors
	// ==================================================================================================================

	public UserProfile(IUserProfile user, Collection<? extends GrantedAuthority> authorities, String token) {

		// 判断用户获取机构信息
		if (user == null) {
			throw new ServiceException("用户信息为空");
		}

		this.userId = user.getId();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.sign = user.getSign();
		this.avatar = user.getAvatar();
		this.telephone = user.getTelephone();
		this.nickName = user.getNickName();
		this.allowPC = user.getAllowPC() == null ? false : user.getAllowPC();
		this.allowApp = user.getAllowApp() == null ? false : user.getAllowApp();
		this.allowOther = user.getAllowOther() == null ? false : user.getAllowOther();
		this.allowWechat = user.getAllowWechat() == null ? false : user.getAllowWechat();
		this.enabled = StringUtils.equals(StatusConst.ENABLE, user.getStatus());
		this.userType = user.getUserType();
		this.tenantId = user.getTenantId();

		this.accountNonLocked = true;
		this.credentialsNonExpired = true;
		this.accountNonExpired = true;

		// 用户是否过期
		if (user.getExpired() != null) {
			this.accountNonExpired = (user.getExpired().getTime() > new Date().getTime());
		}

		this.authorities = authorities;
		this.access = new HashSet<String>();
		this.token = token;
	}
	
	public UserProfile(IUserProfile user,
	    Collection<? extends GrantedAuthority> authorities,
	    Set<String> access) {

		// 判断用户获取机构信息
		if (user == null) {
			throw new ServiceException("用户信息为空");
		}

		this.userId = user.getId();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.sign = user.getSign();
		this.avatar = user.getAvatar();
		this.telephone = user.getTelephone();
		this.nickName = user.getNickName();
		this.allowPC = user.getAllowPC() == null ? false : user.getAllowPC();
		this.allowApp = user.getAllowApp() == null ? false : user.getAllowApp();
		this.allowOther = user.getAllowOther() == null ? false : user.getAllowOther();
		this.allowWechat = user.getAllowWechat() == null ? false : user.getAllowWechat();
		this.enabled = StringUtils.equals(StatusConst.ENABLE, user.getStatus());
		this.userType = user.getUserType();
		this.tenantId = user.getTenantId();

		this.accountNonLocked = true;
		this.credentialsNonExpired = true;
		this.accountNonExpired = true;

		// 用户是否过期
		if (user.getExpired() != null) {
			this.accountNonExpired = (user.getExpired().getTime() > new Date().getTime());
		}

		this.authorities = authorities;
		this.access = access;
	}

	public UserProfile(IUserProfile user, Collection<? extends GrantedAuthority> authorities, Set<String> access, String token) {
		if (user == null) {
			throw new ServiceException("用户信息为空");
		} else {
			this.userId = user.getId();
			this.username = user.getUsername();
			this.password = user.getPassword();
			this.sign = user.getSign();
			this.avatar = user.getAvatar();
			this.telephone = user.getTelephone();
			this.nickName = user.getNickName();
			this.allowPC = user.getAllowPC() == null ? false : user.getAllowPC();
			this.allowApp = user.getAllowApp() == null ? false : user.getAllowApp();
			this.allowOther = user.getAllowOther() == null ? false : user.getAllowOther();
			this.allowWechat = user.getAllowWechat() == null ? false : user.getAllowWechat();
			this.enabled = StringUtils.equals("0", user.getStatus());
			this.userType = user.getUserType();
			this.tenantId = user.getTenantId();
			this.accountNonLocked = true;
			this.credentialsNonExpired = true;
			this.accountNonExpired = true;
			if (user.getExpired() != null) {
				this.accountNonExpired = user.getExpired().getTime() > (new Date()).getTime();
			}

			this.authorities = authorities;
			this.access = access;
			this.token = token;
		}
	}

	// ~ Methods
	// ==================================================================================================================

	/**
	 * 用户权限.
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return authorities;
	}

	/**
	 * 用户密码.
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
	 */
	@Override
	public String getPassword() {

		return this.password;
	}

	/**
	 * 用户名.
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
	 */
	@Override
	public String getUsername() {

		return this.username;
	}

	/**
	 * 账号过期.
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
	 */
	@Override
	public boolean isAccountNonExpired() {

		return accountNonExpired;
	}

	/**
	 * 账号锁定.
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
	 */
	@Override
	public boolean isAccountNonLocked() {

		return accountNonLocked;
	}

	/**
	 * 用户凭证过期.
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
	 */
	@Override
	public boolean isCredentialsNonExpired() {

		return this.credentialsNonExpired;
	}

	/**
	 * 是否启用.
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
	 */
	@Override
	public boolean isEnabled() {

		return this.enabled;
	}
	
	/**
	 * 是否超级管理员.
	 * 
	 * @return
	 */
	public boolean isSuperAdmin() {
		return StringUtils.equals("9", this.userType);
	}
	
	/**
	 * 是普通用户人员.
	 * 
	 * @return
	 */
	public boolean isNormal() {
		return StringUtils.equals("0", this.userType);
	}

	public String getUserId() {

		return userId;
	}

	public String getNickName() {

		return nickName;
	}

	public String getTelephone() {

		return telephone;
	}

	public String getAvatar() {

		return avatar;
	}

	public String getSign() {

		return sign;
	}

	public String getTenantId() {

		return tenantId;
	}

	/**
	 * @return 返回 userType。
	 */
	public String getUserType() {

		return userType;
	}

	public boolean isAllowPC() {

		return allowPC;
	}

	public boolean isAllowApp() {

		return allowApp;
	}

	public boolean isAllowWechat() {

		return allowWechat;
	}

	public boolean isAllowOther() {

		return allowOther;
	}

	public String getToken() {

		return token;
	}

	public void setToken(String token) {

		this.token = token;
	}

	/**
	 * @return 返回 access。
	 */
	public Set<String> getAccess() {

		return access;
	}
	
	// =================机构信息============================================
	
	/**
	 * @return 返回 tenantProfile。
	 */
	@JsonIgnore
	public TenantProfile getTenantProfile() {
		
		if (this.tenantProfile == null && StringUtils.isNotBlank(this.getTenantId())) {
			this.tenantProfile = TenantProfileContext.getTenantProfile(this.getTenantId());
		}
	
		return tenantProfile;
	}
	
	/**
	 * 用户权重.
	 * 
	 * @return
	 */
	public Integer getWeights() {
		
		Integer weights = 0;
		
		TenantProfile tProfile = this.getTenantProfile();
		
		if (tProfile != null) {
			weights = tProfile.getWeights();
		}
		
		return weights;
	}
	
	/**
	 * 机构名称.
	 * 
	 * @return
	 */
	public String getTenantName() {
		
		TenantProfile tProfile = this.getTenantProfile();
		
		if (tProfile != null) {
			return tProfile.getTenantName();
		}
		return null;
	}
}
