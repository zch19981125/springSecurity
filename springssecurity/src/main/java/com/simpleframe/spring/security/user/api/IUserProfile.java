package com.simpleframe.spring.security.user.api;

import java.io.Serializable;
import java.util.Date;

// ~ File Information
// ====================================================================================================================

/**
 * 用户接口.
 * 
 * <pre>
 * 	用户接口
 * </pre>
 * 
 * @author liutao
 * @since $Rev$
 *
 */
public interface IUserProfile extends Serializable {

	// ~ Static Fields
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================
	
	String getId();

	// 获取状态
	String getStatus();
	
	// 获取用户名
	String getUsername();
	
	// 昵称
	String getNickName();
	
	// 密码
	String getPassword();
	
	// 有效期
	Date getExpired();
	
	// 头像
	String getAvatar();
	
	// 个性签名
	String getSign();
	
	// 手机号码
	String getTelephone();
	
	// 允许PC端登陆
	Boolean getAllowPC();
	
	// 允许微信端登陆
	Boolean getAllowWechat();
	
	// 允许移动端登录
	Boolean getAllowApp();
	
	// 允许其他终端
	Boolean getAllowOther();
	
	// 获取租户ID
	String getTenantId();
	
	// 用户类型
	default String getUserType() {
		return "0";
	}
}
