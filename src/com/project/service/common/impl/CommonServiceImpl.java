package com.project.service.common.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import com.project.core.proxy.ProxyFactory;
import com.project.entity.common.Config;
import com.project.service.common.ICommonService;
import com.project.utils.ConvertUtil;
import com.project.utils.TenpayUtil;

/**
 * 通用service实现类
 * Created by GuoZhilong on 2016/2/17.
 */
@Service("commonService")
public class CommonServiceImpl implements ICommonService {
    private final String SQL_KEY = "order$page$pageSize$";

    public boolean save(Object obj) {
        try {
            return ProxyFactory.baseService.addByObject(obj);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @SuppressWarnings("rawtypes")
	public int addReturnID(Class cls, Object obj) {
        try {
            return ProxyFactory.baseService.addReturnID(cls.getName(), obj);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public boolean update(Object obj) {
        try {
            return ProxyFactory.baseService.updateByObject(obj);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @SuppressWarnings("rawtypes")
	public boolean saveOrUpdate(Class cls, Object obj) {
        try {
            return ProxyFactory.baseService.saveOrUpdate(cls.getName(), obj);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @SuppressWarnings("rawtypes")
	public boolean saveOrUpdateByList(Class cls, List list) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        try {
            return ProxyFactory.baseService.saveOrUpdateByList(cls.getName(), list);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @SuppressWarnings("rawtypes")
	public boolean deleteById(Class cls, Integer id) {
        try {
            return ProxyFactory.baseService.deleteById(cls, id);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @SuppressWarnings("rawtypes")
	public Object queryById(Class cls, Integer id) {
        try {
            return ProxyFactory.baseService.findById(cls.getName(), id);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @SuppressWarnings("rawtypes")
	public List queryByHql(Class cls, Map<String, Object> params) {
        try {
            List<Object> listValue = new ArrayList<Object>();
            StringBuilder hql = new StringBuilder();
            hql.append("from ");
            //获取对象
            hql.append(cls.getName());
            hql.append(" where 1=1");
            int page = 1;
            int pageSize = 0;
            //判断params是否为空
            if (params != null && params.size() > 0) {
                match(params, listValue, hql);
                
                if (params.containsKey("order")) {
                    hql.append(" " + params.get("order"));
                }
                if (params.containsKey("page") && params.containsKey("pageSize")) {
                    page = NumberUtils.toInt(params.get("page").toString(), 1);
                    pageSize = NumberUtils.toInt(params.get("pageSize").toString());
                }
            } else {
                hql.append(" order by id desc");
            }
            return ProxyFactory.baseService.findByHql(cls.getClass(), hql.toString(), listValue, (page - 1) * pageSize, pageSize);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryBySql(Class cls, Map<String, Object> params) {
        try {
            List<Object> listValue = new ArrayList<Object>();
            StringBuilder sql = new StringBuilder();
            sql.append("select * from ");
            //获取对象
            Table table = (Table) cls.getAnnotation(Table.class);
            sql.append(table.name());
            sql.append(" where 1=1");
            int page = 1;
            int pageSize = 0;
            //判断params是否为空
            if (params != null && params.size() > 0) {
                match(params, listValue, sql);
                if (params.containsKey("order")) {
                    sql.append(" " + params.get("order"));
                }
                if (params.containsKey("page") && params.containsKey("pageSize")) {
                    page = NumberUtils.toInt(params.get("page").toString(), 1);
                    pageSize = NumberUtils.toInt(params.get("pageSize").toString());
                    sql.append(" limit ?,?");
                    listValue.add((page - 1) * pageSize);
                    listValue.add(pageSize);
                }
            } else {
                sql.append(" order by id desc");
            }
            return ProxyFactory.baseService.findByListObject(cls, sql.toString(), listValue);
        } catch (RuntimeException e) {
            throw e;
        }
    }


    /**
     * hql/sql拼接匹配
     *
     * @param params
     * @param listValue
     * @param sb
     * @author GuoZhilong
     */
    protected void match(Map<String, Object> params, List<Object> listValue, StringBuilder sb) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            if (StringUtils.isBlank(key) || StringUtils.isBlank(ConvertUtil.mapObjectToString(params, key))) {
                continue;
            }
            if (SQL_KEY.contains(key + "$")) {
                continue;
            }
            if (key.startsWith("%")) {//like
                key = key.replace("%", "");
                sb.append(" and ");
                sb.append(key);
                sb.append(" like ?");
                listValue.add("%" + entry.getValue() + "%");
                continue;
            } else if (key.startsWith("#%")) {//orlike
                key = key.replace("#%", "");
                StringBuilder tempSb = new StringBuilder();
                String[] keyArr = key.split(",");
                for (String k : keyArr) {
                    if (tempSb.length() == 0) {
                        tempSb.append(k).append(" like ?");
                    } else {
                        tempSb.append(" or ").append(k).append(" like ?");
                    }
                    listValue.add("%" + entry.getValue() + "%");
                }
                sb.append(" and (").append(tempSb).append(")");
                continue;
            } else if (key.startsWith("!")) {//不等于
                key = key.replace("!", "");
                sb.append(" and ");
                sb.append(key);
                sb.append(" <> ?");
                listValue.add(entry.getValue());
                continue;
            } else if (key.startsWith("@")) {//in
                key = key.replace("@", "");
                sb.append(" and ");
                sb.append(key);
                sb.append(" in (");
                String[] valArr = entry.getValue().toString().split(",");
                for (String val : valArr) {
                    sb.append("?,");
                    if (TenpayUtil.isNum(val)) {
                        listValue.add(NumberUtils.toInt(val));
                    } else {
                        listValue.add(val);
                    }
                }
                sb.replace(sb.length() - 1, sb.length(), ")");
                continue;
            } else if (key.startsWith("|")) {//or
                key = key.replace("|", "");
                sb.append(" or ");
                sb.append(key);
                sb.append(" =?");
                listValue.add(entry.getValue());
                continue;
            } else if (key.startsWith(">")) {
                key = key.replace(">", "");
                sb.append(" and ");
                sb.append(key);
                sb.append(" > ?");
                listValue.add(entry.getValue());
                continue;
            } else if (key.startsWith("<")) {
                key = key.replace("<", "");
                sb.append(" and ");
                sb.append(key);
                sb.append(" < ?");
                listValue.add(entry.getValue());
                continue;
            } else if (key.startsWith("=>")) {
                key = key.replace("=>", "");
                sb.append(" and ");
                sb.append(key);
                sb.append(" >= ?");
                listValue.add(entry.getValue());
                continue;
            } else if (key.startsWith("=<")) {
                key = key.replace("=<", "");
                sb.append(" and ");
                sb.append(key);
                sb.append(" <= ?");
                listValue.add(entry.getValue());
                continue;
            } else if (key.startsWith("^")) {//instr
                key = key.replace("^", "");
                sb.append(" and instr(");
                sb.append(key);
                sb.append(",?) > 0 ");
                listValue.add(entry.getValue());
                continue;
            }
            sb.append(" and ");
            sb.append(key);
            sb.append(" =?");
            listValue.add(entry.getValue());
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public int countBySql(Class cls, Map<String, Object> params) {
        try {
            List<Object> listValue = new ArrayList<Object>();
            StringBuilder sql = new StringBuilder();
            sql.append("select count(1) from ");
            //获取对象
            Table table = (Table) cls.getAnnotation(Table.class);
            sql.append(table.name());
            sql.append(" where 1=1");
            //判断params是否为空
            if (params != null && params.size() > 0) {
                match(params, listValue, sql);
            }
            return ProxyFactory.baseService.countBySql(sql.toString(), listValue);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @SuppressWarnings("rawtypes")
	public Object queryByField(Class cls, String field, Object value) {
        try {
            return ProxyFactory.baseService.findByEQ(cls.getName(), field, value);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean updateField(Class cls, Integer id, String field, Object value) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("update ");
            Table table = (Table) cls.getAnnotation(Table.class);
            sql.append(table.name());
            sql.append(" set ");
            sql.append(field);
            sql.append("=? ");
            sql.append("where id=?");
            return ProxyFactory.baseService.update(sql.toString(), value, id);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<Object, Object> queryByValuesToMap(Class cls, String propertyName, List<Object> valueList) {
        try {
            if (CollectionUtils.isNotEmpty(valueList)) {
                StringBuilder values = new StringBuilder();
                for (Object ids : valueList) {
                    values.append(ids + ",");
                }
                values.deleteCharAt(values.length() - 1);
                //通过参数调用上述方法进行查询
                Map<String, Object> params = new HashedMap<String, Object>();
                params.put("@" + propertyName, values);
                List<Object> list = this.queryByHql(cls, params);
                if (CollectionUtils.isNotEmpty(list)) {
                    //最终返回结果
                    Map<Object, Object> resultMap = new HashMap<Object, Object>();
                    //将传过来的propertyName首字母转为大写并拼接get，形成get方法名称
                    String getMethodName = "get" + StringUtils.capitalize(propertyName);
                    try {
                        for (Object obj : list) {
                            //根据指定字段的get方法名称获取对应类中的get方法，也就是getMethodName和实体类中的get方法是同名的
                            Method method = obj.getClass().getMethod(getMethodName);
                            //设置字段的访问权限
                            method.setAccessible(true);
                            //获取指定字段的值
                            Object returnValue = method.invoke(obj);
                            resultMap.put(returnValue, obj);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return resultMap;
                }
            }
        } catch (RuntimeException e) {
            throw e;
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
	public int countByHql(Class cls, Map<String, Object> params) {
        try {
            List<Object> listValue = new ArrayList<Object>();
            StringBuilder hql = new StringBuilder();
            hql.append("select count(*) from ");
            //获取对象
            hql.append(cls.getName());
            hql.append(" where 1=1");
            //判断params是否为空
            if (params != null && params.size() > 0) {
                match(params, listValue, hql);
            }
            return ProxyFactory.baseService.countByHql(hql.toString(), listValue);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @SuppressWarnings("rawtypes")
	public Object queryObjByHql(Class cls, Map<String, Object> params) {
        try {
            List<Object> listValue = new ArrayList<Object>();
            StringBuilder hql = new StringBuilder();
            hql.append("from ");
            //获取对象
            hql.append(cls.getName());
            hql.append(" where 1=1");
            //判断params是否为空
            if (params != null && params.size() > 0) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    String key = entry.getKey();
                    if (StringUtils.isBlank(key) || entry.getValue() == null) {
                        continue;
                    }
                    hql.append(" and ");
                    hql.append(key);
                    hql.append(" =?");
                    listValue.add(entry.getValue());
                }
                return ProxyFactory.baseService.findObjectByHql(cls, hql.toString(), listValue);
            } else {
                //当查询参数为空，直接返回null
                return null;
            }
        } catch (RuntimeException e) {
            throw e;
        }

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<Object, Object> queryByParamsToMap(Class cls, String propertyName, Map<String, Object> params) {
        try {
            if (params != null && params.size() > 0) {
                //通过参数调用上述方法进行查询
                List<Object> list = this.queryByHql(cls, params);
                if (CollectionUtils.isNotEmpty(list)) {
                    //最终返回结果
                    Map<Object, Object> resultMap = new HashMap<Object, Object>();
                    //将传过来的propertyName首字母转为大写并拼接get，形成get方法名称
                    String getMethodName = "get" + StringUtils.capitalize(propertyName);
                    try {
                        for (Object obj : list) {
                            //根据指定字段的get方法名称获取对应类中的get方法，也就是getMethodName和实体类中的get方法是同名的
                            Method method = obj.getClass().getMethod(getMethodName);
                            //设置字段的访问权限
                            method.setAccessible(true);
                            //获取指定字段的值
                            Object returnValue = method.invoke(obj);
                            resultMap.put(returnValue, obj);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return resultMap;
                }
            }else{
            	//通过参数调用上述方法进行查询
                List<Object> list = this.queryByHql(cls, null);
                if (CollectionUtils.isNotEmpty(list)) {
                    //最终返回结果
                    Map<Object, Object> resultMap = new HashMap<Object, Object>();
                    //将传过来的propertyName首字母转为大写并拼接get，形成get方法名称
                    String getMethodName = "get" + StringUtils.capitalize(propertyName);
                    try {
                        for (Object obj : list) {
                            //根据指定字段的get方法名称获取对应类中的get方法，也就是getMethodName和实体类中的get方法是同名的
                            Method method = obj.getClass().getMethod(getMethodName);
                            //设置字段的访问权限
                            method.setAccessible(true);
                            //获取指定字段的值
                            Object returnValue = method.invoke(obj);
                            resultMap.put(returnValue, obj);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return resultMap;
                }
            }
        } catch (RuntimeException e) {
            throw e;
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
	public Object queryObject(Class cls, String propertyName,
                              Object propertyValue) {
        return ProxyFactory.baseService.findByEQ(cls.getName(), propertyName, propertyValue);
    }

	@Override
	public List<Map<String, Object>> queryBySql(StringBuilder sql,
			Map<String, Object> params) {
		sql.append(" where 1=1");
		int page = 1;
        int pageSize = 0;
        //判断params是否为空
        List<Object> listValue = new ArrayList<Object>();
        if (params != null && params.size() > 0) {
        	match(params, listValue, sql);
            if (params.containsKey("order")) {
            	sql.append(" " + params.get("order"));
            }
            if (params.containsKey("page") && params.containsKey("pageSize")) {
                page = NumberUtils.toInt(params.get("page").toString(), 1);
                pageSize = NumberUtils.toInt(params.get("pageSize").toString());
            }
        } else {
        	sql.append(" order by id desc");
        }
		return ProxyFactory.baseService.findbyListResultMap(sql.toString(), (page - 1) * pageSize, pageSize, listValue);
	}
	
	public Map<Integer, Config> getConfigMap(String ids){
		String configHql = "from Config where id in ("+ids+")";
		@SuppressWarnings("unchecked")
		List<Config> configList = ProxyFactory.baseService.findByHql(Config.class, configHql, 0, 0);
		Map<Integer, Config> configMap = new HashMap<Integer, Config>();
		if(CollectionUtils.isNotEmpty(configList)){
			for(Config config : configList){
				configMap.put(config.getId(), config);
			}
		}
		return configMap;
	}

	@Override
	public int addReturnID(Object obj) {
		return ProxyFactory.baseService.addReturnID(obj.getClass().getName(), obj);
	}

	@Override
	public List<Map<String, Object>> queryBySql(StringBuilder sql, int page, int pageSize) {
		return ProxyFactory.baseService.findbyListResultMap(sql.toString(), (page - 1) * pageSize, pageSize, null);
	}

	@Override
	public boolean executeSQL(String sql) {
		return ProxyFactory.baseService.executeSql(sql);
	}

}
