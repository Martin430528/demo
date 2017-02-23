package com.project.service.common;

import java.util.List;
import java.util.Map;

/**
 * 通用service
 * Created by GuoZhilong on 2016/2/17.
 */
public interface ICommonService {


    /**
     * 保存
     *
     * @param obj
     * @return
     * @author GuoZhilong
     */
    boolean save(Object obj);


    /**
     * 保存并返回id
     *
     * @param cls
     * @param obj
     * @return
     * @author GuoZhilong
     */
    @SuppressWarnings("rawtypes")
	int addReturnID(Class cls, Object obj);


    /**
     * 更新
     *
     * @param obj
     * @return
     * @author GuoZhilong
     */
    boolean update(Object obj);


    /**
     * 新增/更新
     *
     * @param cls
     * @param obj
     * @return
     * @author GuoZhilong
     */
    @SuppressWarnings("rawtypes")
	boolean saveOrUpdate(Class cls, Object obj);


    /**
     * 批量新增/批量更新
     *
     * @param cls
     * @param list
     * @return
     * @author GuoZhilong
     */
    @SuppressWarnings("rawtypes")
	boolean saveOrUpdateByList(Class cls, List list);


    /**
     * 根据id删除
     *
     * @param cls
     * @param id
     * @return
     * @author GuoZhilong
     */
    @SuppressWarnings("rawtypes")
	boolean deleteById(Class cls, Integer id);


    /**
     * 根据id查询对象
     *
     * @param cls
     * @param id
     * @return
     * @author GuoZhilong
     */
    @SuppressWarnings("rawtypes")
	Object queryById(Class cls, Integer id);


    /**
     * 根据参数查询列表信息(hql)
     *
     * @param cls
     * @param params
     * @return
     * @author GuoZhilong
     */
    @SuppressWarnings("rawtypes")
	List queryByHql(Class cls, Map<String, Object> params);


    /**
     * 根据参数查询列表信息(sql)
     *
     * @param cls
     * @param params
     * @return
     * @author GuoZhilong
     */
    @SuppressWarnings("rawtypes")
	List queryBySql(Class cls, Map<String, Object> params);


    /**
     * 根据参数统计列表信息(sql)
     *
     * @param cls
     * @param params
     * @return
     * @author GuoZhilong
     */
    @SuppressWarnings("rawtypes")
	int countBySql(Class cls, Map<String, Object> params);


    /**
     * 根据指定字段查询(经常用于查询某值是否存在)
     *
     * @param cls
     * @param field
     * @param value
     * @return
     * @author GuoZhilong
     */
    @SuppressWarnings("rawtypes")
	Object queryByField(Class cls, String field, Object value);


    /**
     * 根据id更新指定字段
     *
     * @param cls
     * @param id
     * @param field
     * @param value
     * @return
     * @author GuoZhilong
     */
    @SuppressWarnings("rawtypes")
	boolean updateField(Class cls, Integer id, String field, Object value);


    /**
     * 根据内容串查询并添加至map
     *
     * @param cls
     * @param propertyName 字段名称(键)
     * @param valueList    内容串
     * @return
     * @author GuoZhilong
     */
    @SuppressWarnings("rawtypes")
	Map<Object, Object> queryByValuesToMap(Class cls, String propertyName, List<Object> valueList);


    /**
     * 根据参数统计列表信息(hql)
     *
     * @param cls
     * @param params
     * @return
     * @author GuoZhilong
     */
    @SuppressWarnings("rawtypes")
	int countByHql(Class cls, Map<String, Object> params);

    /**
     * 根据参数获取对象
     *
     * @param cls
     * @param params
     * @return
     * @author GuoZhilong
     */
    @SuppressWarnings("rawtypes")
	Object queryObjByHql(Class cls, Map<String, Object> params);


    /**
     * 根据参数查询并添加至map
     *
     * @param cls
     * @param propertyName 字段名称(键)
     * @param params       参数(与接口queryByHql中的参数一致)
     * @return
     * @author GuoZhilong
     */
    @SuppressWarnings("rawtypes")
	Map<Object, Object> queryByParamsToMap(Class cls, String propertyName, Map<String, Object> params);
    
    /**根据字段查对象
     * @param cls
     * @param propertyName
     * @param propertyValue
     * @return
     */
    @SuppressWarnings("rawtypes")
	Object queryObject(Class cls, String propertyName, Object propertyValue);

    /**根据SQL查询Map结果集
     * @param sql
     * @param params
     * @return List<Map<String,Object>>
     */
    List<Map<String, Object>> queryBySql(StringBuilder sql, Map<String, Object> params);
    
    List<Map<String, Object>> queryBySql(StringBuilder sql, int page, int pageSize);
    
    int addReturnID(Object obj);
    
    boolean executeSQL(String sql);
    
}
