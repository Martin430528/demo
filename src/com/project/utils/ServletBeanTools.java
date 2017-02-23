package com.project.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Servlet业务中实体工具
 *
 * @author LiuDing 2014-2-16-下午08:10:06
 */
public class ServletBeanTools {

	/**
	 * 自动匹配参数赋值到实体bean中
	 *
	 * @author LiuDing 2014-2-16 下午10:23:37
	 * @param bean
	 * @param request
	 */
	public static void populate(Object bean, HttpServletRequest request) {
		Class<? extends Object> clazz = bean.getClass();
		Method ms[] = clazz.getDeclaredMethods();
		String mname;
		String field;
		String fieldType;
		String value;
		for (Method m : ms) {
			mname = m.getName();
			if (!mname.startsWith("set")
					|| ArrayUtils.isEmpty(m.getParameterTypes())) {
				continue;
			}
			try {
				field = mname.toLowerCase().charAt(3)
						+ mname.substring(4, mname.length());
				value = request.getParameter(field);
				if (StringUtils.isEmpty(value)) {
					continue;
				}
				fieldType = m.getParameterTypes()[0].getName();
				// 以下可以确认value为String类型
				if (String.class.getName().equals(fieldType)) {
					m.invoke(bean, (String) value);
				} else if (Integer.class.getName().equals(fieldType)
						&& NumberUtils.isDigits((String) value)) {
					m.invoke(bean, Integer.valueOf((String) value));
				} else if (Short.class.getName().equals(fieldType)
						&& NumberUtils.isDigits((String) value)) {
					m.invoke(bean, Short.valueOf((String) value));
				} else if (Float.class.getName().equals(fieldType)
						&& NumberUtils.isNumber((String) value)) {
					m.invoke(bean, Float.valueOf((String) value));
				} else if (Double.class.getName().equals(fieldType)
						&& NumberUtils.isNumber((String) value)) {
					m.invoke(bean, Double.valueOf((String) value));
				} else if (Date.class.getName().equals(fieldType)) {
					m.invoke(bean, DateUtils.parseDate((String) value,
							"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"));
				} else {
					m.invoke(bean, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * orgi中的非空字段覆盖bean中对应字段
	 *
	 * @author GuoZhiLong
	 * @param bean
	 * @param orgi
	 * @return void
	 * @throws
	 */
	public static void copyIfNotNull(Object bean, Object orgi) {
		Class<? extends Object> clazz = bean.getClass();
		Class<? extends Object> ogclazz = orgi.getClass();
		Method ms[] = clazz.getDeclaredMethods();
		Method ogms[] = ogclazz.getDeclaredMethods();
		String mname;
		String field;
		Object value = null;
		for (Method m : ms) {
			mname = m.getName();
			if (!mname.startsWith("set")
					|| ArrayUtils.isEmpty(m.getParameterTypes())) {
				continue;
			}
			try {
				field = mname.toLowerCase().charAt(3)
						+ mname.substring(4, mname.length());
				for (Method gms : ogms) {
					if (gms.getName().toUpperCase()
							.equals("GET" + field.toUpperCase())) {
						value = gms.invoke(orgi);
					}
				}
				if (null == value) {
					continue;
				}
				m.invoke(bean, value);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}
}
