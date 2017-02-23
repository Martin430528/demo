package com.project.exception;

/**
 * 无权限异常
 * @author LiMin
 * 2016年7月14日
 */
public class NoPermissionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 57054207331840980L;
	
	public NoPermissionException(String message){
		super(message);
	}
	
}
