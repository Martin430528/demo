package com.constants;


/**
 * 全局常量类
 * 
 * @author LiMin 2016年5月26日
 */
public class IConstants {
	
	public static final int OK = 0;//请求成功状态码
	public static final int FAIL = -1;//请求失败状态码
	public static final String SUCCESS_MSG = "操作成功！";
    public static final String FAILURE_MSG = "操作失败，请重试！";
    public static final String SYSTEM_ERROR = "系统忙，刷新页面重试或联系管理员！";
    public static final String DATA_ERROR = "您请求的数据不存在！";
    public static final String PROPERTY_ERROR = "您请求数据格式有误！";
    public static final String PARAMS_ERROR = "参数错误！";
    public static final String FORM_VALID_ERROR = "填写不完整或有误！";
    public static final String LOGIN_OUT = "未登录，请登录！";
    public static final String LOGIN_TIMEOUT = "登录超时，请重新登录！";
    public static final String LOGIN_ERROR = "用户名或密码错误！";
    public static final String LOGIN_NAME_EXIST = "用户名已经存在！";
	
}
