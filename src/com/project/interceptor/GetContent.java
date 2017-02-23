package com.project.interceptor;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 特殊处理request response
 * 
 * @author GuoZhiLong
 * @date 2014年12月30日 下午2:37:27
 * 
 */
public class GetContent implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
						 FilterChain filterChain) throws IOException, ServletException {
		SysContent.SetRequest((HttpServletRequest) request);
		SysContent.setResponse((HttpServletResponse) response);
		filterChain.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}
