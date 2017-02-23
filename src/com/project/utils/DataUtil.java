package com.project.utils;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class DataUtil {
	/**
	 * 封装查询条件到Criterion接口
	 *
	 * @author tianwei
	 * @date 2010-4-8
	 * @param string
	 * @param object
	 * @param integer
	 * @return
	 */

	public static Criterion getSE(String propertyName, Object propertyValue,
								  int mode) {
		Criterion se = null;
		switch (mode) {
		case 1:
			// 等于查询
			se = Restrictions.eq(propertyName, propertyValue);
			break;
		case 2:
			// 小于查询
			se = Restrictions.lt(propertyName, propertyValue);
			break;
		case 3:
			// 大于查询
			se = Restrictions.gt(propertyName, propertyValue);
			break;
		case 4:
			// between
			  if  (propertyValue.getClass().isArray())  {
				  Object[] objects =  (Object[])propertyValue;
				  se = Restrictions.between(propertyName, objects[0], objects[1]);
			  }
			break;
		case 5:
			// 小于等于
			se = Restrictions.le(propertyName, propertyValue);
			break;
		case 6:
			// 大于等于
			se = Restrictions.ge(propertyName, propertyValue);
			break;
		case 7:
			// like
			se = Restrictions.like(propertyName, String.valueOf(propertyValue));
			break;
		case 8:
			// 不等于
			se = Restrictions.ne(propertyName, propertyValue);
			break;
		case 9:
			// in
			Object[] objectss = (Object[]) propertyValue;
			se = Restrictions.in(propertyName, objectss);
			break;
		default:
			// 等于查询
			se = Restrictions.eq(propertyName, propertyValue);
			break;

		}
		return se;
	}

	/**
	 * 封装查询条件到数组 Criterion[]
	 *
	 * @author tianwei
	 * @date 2010-4-9
	 * @param propertyName
	 * @param propertyValue
	 * @param model
	 * @return
	 */
	public static Criterion[] getSE(String[] propertyName,
									Object[] propertyValue, Integer[] model) {
		Criterion[] se = null;
		if (propertyName != null && propertyName.length > 0) {
			se = new Criterion[propertyName.length];
			for (int i = 0; i < propertyName.length; i++) {
				se[i] = DataUtil.getSE(propertyName[i], propertyValue[i],
						model[i]);
			}
		} else {
			se = new Criterion[0];
		}
		return se;
	}

	/**
	 * 根据mode生成查询判断语句
	 *
	 * @author tianwei
	 * @date 2010-4-8
	 * @param propertyName
	 * @param propertyValue
	 * @param mode
	 * @return
	 */
	public static String getSql(String propertyName, Object propertyValue,
			Integer mode) {
		String se = "";
		switch (mode) {
		case 0:
			// 直接返回sql语句 用于有or的情况下自己拼接 or 语句
			se = propertyName;
			break;
		case 1:
			// 等于查询
			se = propertyName + " = '" + propertyValue + "'";
			break;
		case 2:
			// 小于查询
			se = propertyName + " < '" + propertyValue + "'";
			break;
		case 3:
			// 大于查询
			se = propertyName + " > '" + propertyValue + "'";
			break;
		case 4:
			// between
			 if  (propertyValue.getClass().isArray())  {
				  Object[] objects =  (Object[])propertyValue;
				  se = propertyName + " between " +objects[0] + " and "
							+ objects[1];
			  }
			break;
		case 5:
			// 小于等于
			se = propertyName + " <= '" + propertyValue + "'";
			break;
		case 6:
			// 大于等于
			se = propertyName + " >= '" + propertyValue + "'";
			break;
		case 7:
			// like
			se = propertyName + " like '" + propertyValue + "%'";
			break;
		case 8:
			// 不等于
			se = propertyName + " != '" + propertyValue + "'";
			break;
		case 9:
			// in
			se = propertyName + " in (" + propertyValue + ")";
			break;
		default:
			// 等于查询
			se = propertyName + " = '" + propertyValue + "'";
		}

		return " " + se;
	}

	/**
	 * 拼接字符串生成查询语句
	 *
	 * @author tianwei
	 * @date 2010-4-8
	 * @param propertyName
	 * @param propertyValue
	 * @param model
	 * @return
	 */
	public static String getSql(String[] propertyName, Object[] propertyValue,
			Integer[] model) {
		StringBuilder sb = new StringBuilder("");
		if (propertyName != null && propertyName.length > 0) {
			int ln = propertyName.length;
			for (int i = 0; i < ln; i++) {
				if (i > 0)
					sb.append(" AND ");

				sb.append(DataUtil.getSql(propertyName[i], propertyValue[i],
						model[i]));
			}
		}
		return sb.toString();
	}

}