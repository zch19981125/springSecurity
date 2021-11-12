package com.simpleframe.spring.security.access;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simpleframe.boot.common.result.ResultData;

// ~ File Information
// ====================================================================================================================

public class AccessAuthenticationEntryPoint implements AuthenticationEntryPoint {

	// ~ Static Fields
	// ==================================================================================================================

	private static final ObjectMapper objectMapper = new ObjectMapper();

	// ~ Fields
	// ==================================================================================================================

	// ~ Constructors
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================


	/**
	 * 执行返回信息.
	 * @see org.springframework.security.web.AuthenticationEntryPoint#commence(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
		throws IOException, ServletException {

		// AJAX请求,使用response发送403
		if (isAjaxRequest(request)) {
			response.setHeader("Access-Control-Allow-Origin", response.getHeader("Origin"));
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setContentType("application/json; charset=utf-8");
			response.setCharacterEncoding("UTF-8");

			ResultData result = ResultData.fail();
			result.setErrorCode(String.valueOf(HttpStatus.FORBIDDEN.value()));
			result.setMsg(HttpStatus.FORBIDDEN.getReasonPhrase());

			response.getWriter().write(objectMapper.writeValueAsString(result));
			response.getWriter().flush();
		}
		// 非AJAX请求，跳转系统默认的403错误界面
		else if (!response.isCommitted()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, authException.getMessage());
		}
	}


	/**
	 * 判断是否为ajax请求.
	 */
	private boolean isAjaxRequest(HttpServletRequest request) {

		String requestedWith = request.getHeader("X-Requested-With");

		if ((null != request.getHeader("accept") &&
			request.getHeader("accept").indexOf("application/json") > -1)
			|| StringUtils.isNotBlank(requestedWith) && requestedWith.equals("XMLHttpRequest")) {
			return true;
		}
		return false;
	}
}
