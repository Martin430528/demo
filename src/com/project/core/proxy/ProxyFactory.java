package com.project.core.proxy;

public class ProxyFactory {
	public static IBaseService baseService = null;

	static {
		try {
			baseService = (IBaseService) Class.forName(BaseService.class.getName()).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
