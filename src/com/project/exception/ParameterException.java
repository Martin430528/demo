package com.project.exception;

/**
 * 参数错误
 * @author LiMin
 * 2016年7月14日
 */
public class ParameterException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 57054207331840980L;

	public ParameterException(String msg){
		super(msg);
	}
	
}
