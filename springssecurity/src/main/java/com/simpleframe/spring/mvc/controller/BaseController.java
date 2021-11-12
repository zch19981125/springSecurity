package com.simpleframe.spring.mvc.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

// ~ File Information
// ====================================================================================================================

@CrossOrigin(origins = {
  "*"
})
public abstract class BaseController {

	// ~ Static Fields
	// ==================================================================================================================

	// ~ Fields
	// ==================================================================================================================

	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;

	// ~ Constructors
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================

	@ModelAttribute
	public void initRequestAndResponse(HttpServletRequest request, HttpServletResponse response, Model model) {

		this.request = request;
		this.response = response;
		this.session = request.getSession();
	}

	/**
	 * @return 返回 paramsMap.
	 */
	public Map<String, Object> getParameterMap() {

		Map<String, Object> parameterMap = new HashMap<String, Object>();

		Map<String, String[]> params = request.getParameterMap();

		if (params != null && params.size() > 0) {
			String[] propValues = null;
			for (String propKey : params.keySet()) {

				propValues = params.get(propKey);

				if (propValues.length == 1) {
					parameterMap.put(propKey, propValues[0]);
				} else {
					parameterMap.put(propKey, propValues);
				}
			}
		}

		return parameterMap;
	}

	/**
	 * 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// Date 类型转换
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}

	public HttpServletRequest getRequest() {

		return request;
	}

	public void setRequest(HttpServletRequest request) {

		this.request = request;
	}

	public HttpServletResponse getResponse() {

		return response;
	}

	public void setResponse(HttpServletResponse response) {

		this.response = response;
	}

	public HttpSession getSession() {

		return session;
	}

	public void setSession(HttpSession session) {

		this.session = session;
	}
}
