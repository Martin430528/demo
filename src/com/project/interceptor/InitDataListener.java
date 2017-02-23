package com.project.interceptor;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;

@SuppressWarnings("rawtypes")
@Repository
public class InitDataListener implements ApplicationListener {
	
	private static boolean isStart = false;
	@Override
	public void onApplicationEvent(ApplicationEvent arg0) {
		if (!isStart) {//这个可以解决项目启动加载两次的问题
			isStart = true;
		}
	}

}
