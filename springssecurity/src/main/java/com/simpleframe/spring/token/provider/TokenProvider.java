package com.simpleframe.spring.token.provider;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

// ~ File Information
// ====================================================================================================================

/**
 * token验证和生成工具.
 * 
 * <pre>
 * 	token验证和生成工具
 * </pre>
 * 
 * @author liutao
 * @since $Rev$
 *
 */
@Component
public class TokenProvider {

	// ~ Static Fields
	// ==================================================================================================================

	// logger 日志
	private final static Logger logger = LoggerFactory.getLogger(TokenProvider.class);

	// ~ Fields
	// ==================================================================================================================
	
	// 验证证书
	@Value("${spring.token.secret:simpleFrame}")
	private String secretKey;

	// token 是有效期单位秒, 默认1小时
	@Value("${spring.token.validity:3600}")
	private Long tokenValidity;

	// 使用HS256算法
	private Algorithm algorithm;

	// ~ Constructors
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================

	/**
	 * 创建默认token.
	 * 
	 * @param userName
	 * @return
	 */
	public String createToken(String userName) {

		try {

			JWTCreator.Builder builder = JWT.create();

			// 设置过期时间一个小时
			builder.withExpiresAt(new Date(System.currentTimeMillis() + tokenValidity * 1000));

			// 索赔:添加自定义声明值,完成荷载的信息
			builder.withClaim("userName", userName);

			// 签署:调用sign()传递算法实例
			return builder.sign(getAlgorithm());
		} catch (Exception e) {
			logger.error("无效的签名信息! ", e.getMessage());
		}
		return null;
	}
	
	/**
	 * 指定创建token时间.
	 * 
	 * @param userName
	 * @param seconds
	 * @return
	 */
	public String createTokenSec(String userName, Long seconds) {

		try {

			JWTCreator.Builder builder = JWT.create();
			
			// 如果小于一小时, 修改成一小时
			if (seconds < tokenValidity) {
				seconds = tokenValidity;
			}

			// 设置过期时间
			builder.withExpiresAt(new Date(System.currentTimeMillis() + seconds * 1000));

			// 索赔:添加自定义声明值,完成荷载的信息
			builder.withClaim("userName", userName);

			// 签署:调用sign()传递算法实例
			return builder.sign(getAlgorithm());
		} catch (Exception e) {
			logger.error("无效的签名信息! ", e.getMessage());
		}
		return null;
	}
	
	/**
	 * 获取token内容.
	 * 
	 * @param authToken
	 * @return
	 */
	public String getUsername(String authToken) {
		
		try {
			// 这将用于验证令牌的签名
			JWTVerifier verifier = JWT.require(getAlgorithm()).build();
			
			// 针对给定令牌执行验证
			DecodedJWT jwt = verifier.verify(authToken);
			
			// 获取令牌中定义的声明
			Map<String, Claim> claims = jwt.getClaims();
			
			// 返回指定键映射到的值
			return claims.get("userName") == null ? null : claims.get("userName").asString();
		} catch (JWTVerificationException e) {
			logger.error("校验失败或token已过期! ", e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * 获取token内容.
	 * 
	 * @param authToken
	 * @param key
	 * @return
	 */
	public Claim getClaimObject(String authToken, String key) {
		
		try {
			// 这将用于验证令牌的签名
			JWTVerifier verifier = JWT.require(getAlgorithm()).build();
			
			// 针对给定令牌执行验证
			DecodedJWT jwt = verifier.verify(authToken);
			
			// 获取令牌中定义的声明
			Map<String, Claim> claims = jwt.getClaims();
			
			// 返回指定键映射到的值
			return claims.get(key);
		} catch (JWTVerificationException e) {
			logger.error("校验失败或token已过期! ", e.getMessage());
		}
		
		return null;
	}

	/**
	 * 验证token.
	 * 
	 * @param authToken
	 * @param userName
	 * @return
	 */
	public boolean validateToken(String authToken, String userName) {
		
		String vUserName = this.getUsername(authToken);
		
		// 如果编译token用户名是空或者不同, 则返回false
		if (StringUtils.isNotBlank(vUserName)) {
			return StringUtils.equals(vUserName, userName);
		}
		
		return false;
	}
	
	/**
	 * 验证token.
	 * 
	 * @param authToken
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean validateToken(String authToken, String key, String value) {
		
		Claim tokenValue = this.getClaimObject(authToken, key);
		
		String strTokenValue = tokenValue != null ? tokenValue.asString() : null;
		
		// 如果编译token用户名是空或者不同, 则返回false
		if (StringUtils.isNotBlank(strTokenValue)) {
			return StringUtils.equals(strTokenValue, value);
		}
		
		return false;
	}
	
	/**
	 * 获取algorithm, 如果不存在就创建.
	 * 
	 * @return
	 */
	private Algorithm getAlgorithm() {
		if (this.algorithm == null) {
			this.algorithm = Algorithm.HMAC256(this.secretKey);
		}
		return this.algorithm;
	}
}
