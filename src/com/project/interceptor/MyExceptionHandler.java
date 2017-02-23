package com.project.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.project.exception.BusinessException;
import com.project.exception.NoPermissionException;
import com.project.exception.ParameterException;
import com.project.exception.SessionInvalidException;

/**
 * Spring MVC自带的全局异常处理类,
 * 当业务逻辑抛出异常时都会被该类拦截并进行处理.
 * @author LiMin
 *
 */
public class MyExceptionHandler implements HandlerExceptionResolver {

	private Logger logger = Logger.getLogger(MyExceptionHandler.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		Map<String, Object> model = new HashMap<String, Object>();
        model.put("ex", ex);
        logger.error(ex.getMessage(), ex);//打印异常信息
        
        HandlerMethod method = (HandlerMethod) handler;
        ResponseBody body = method.getMethodAnnotation(ResponseBody.class);
        if (body == null) {//页面请求
        	// 根据不同错误转向不同页面
            if (ex instanceof BusinessException) {//业务逻辑处理出错
                
            } else if (ex instanceof ParameterException) {//参数处理出错。
                
            } else if (ex instanceof NoPermissionException) {//无操作权限
            	
			} else if (ex instanceof SessionInvalidException) {//登录超时
				return new ModelAndView("sys/login", model);
			} else {  //其他数据类型错误
                
            }
            return new ModelAndView("error/error", model);
		} else {//AJAX请求
			// 设置状态码,注意这里不能设置成500，设成500JQuery不会出错误提示           
			//并且不会有任何反应    
			response.setStatus(HttpStatus.SC_OK);   
			// 设置ContentType    
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);   
			// 避免乱码    
			response.setCharacterEncoding("UTF-8");    
			response.setHeader("Cache-Control", "no-cache, must-revalidate");   
			try {
				PrintWriter writer = response.getWriter();
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("errcode", -1);
				jsonObject.put("errmsg", ex.getMessage());
				writer.write(jsonObject.toJSONString());    
				writer.close();    
			} catch (IOException e) {
				e.printStackTrace();   
			}
			return new ModelAndView("/error/error", model);
		}
	}

}
