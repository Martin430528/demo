package com.project.authority;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限自定义注解
 * 
 * 用法：在需要权限控制的方法头部加上@Authority(value = "自定义名称", validate = 是否权限控制true/false,
 * resultType =结果类型 page/ajax ResultTypeEnum.PAGE/ ResultTypeEnum.JSON)
 * 
 * @author GuoZhiLong
 * @date 2015年8月4日 下午11:48:22
 * 
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Authority {

	/**
	 * 是否验证权限，默认验证
	 * 
	 * @author GuoZhiLong
	 * @return
	 * @return boolean
	 */
	boolean validate() default true;

	/**
	 * 父级权限码
	 * 
	 * @author GuoZhiLong
	 * @return
	 * @return String
	 */
	String parentVal();

	/**
	 * 父级权限别名(中文名称)
	 * 
	 * @author GuoZhiLong
	 * @return
	 * @return String
	 */
	String pAliasesVal();

	/**
	 * 权限码
	 * 
	 * @author GuoZhiLong
	 * @return
	 * @return String
	 */
	String purviewVal();

	/**
	 * 权限别名(中文名称)
	 * 
	 * @author GuoZhiLong
	 * @return
	 * @return String
	 */
	String aliasesVal();
	
	/**排序
	 * @return
	 * @author LiMin
	 */
	int sort();
	
	/**菜单请求链接
	 * @return
	 * @author LiMin
	 */
	String menuUrl() default "javascript:void(0);";

	/**
	 * 返回类型，默认返回页面
	 * 
	 * @author GuoZhiLong
	 * @return
	 * @return ResultTypeEnum
	 */
	ResultTypeEnum resultType() default ResultTypeEnum.PAGE;
}
