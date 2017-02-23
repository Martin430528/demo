package com.project.core.dialect;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

/**
 * mysql5方言
 * 
 * @author GuoZhiLong
 * @date 2015年1月14日 下午3:06:28
 * 
 */
public class MySQLDialect extends MySQL5Dialect {

	public MySQLDialect() {
		super();
		registerHibernateType(Types.LONGVARCHAR, StandardBasicTypes.TEXT.getName());
		registerHibernateType(Types.TINYINT, StandardBasicTypes.INTEGER.getName());
		registerHibernateType(Types.BIGINT, StandardBasicTypes.INTEGER.getName());
	}
}