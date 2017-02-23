package com.project.core.proxy;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface IBaseService {
    public SessionFactory getSF();

    /**
     * ========== 执行SQL 增删查改================
     */
    public boolean add(String sql);

    public boolean delete(String sql);

    public boolean update(String sql, List<Object> params);

    /**
     * 根据sql更新
     *
     * @param sql
     * @param params 如果没用参数则传StringUtils.EMPTY
     * @return boolean
     * @author GuoZhiLong
     */
    public boolean update(String sql, Object... params);

    public List findByList(String sql, List<Object> params);

    public List findByList(String sql, Object... params);

    public List getListSql(String sql);

    public List findByListObject(Class cl, String sql);

    public List findByListObject(Class cl, String sql, List<Object> params);

    public List findByListObject(Class cl, String sql, Object... params);

    public Object[] findByOne(String sql, List<Object> params);

    public Object[] findByOne(String sql, Object... params);

    public Object findOneReturn(String sql, List<Object> params);

    public Object findOneReturn(String sql, Object... params);

    public int countBySql(String sql, List<Object> params);

    public int countBySql(String sql, Object... params);
    
    public double countBySqlDouble(String sql);

    /**
     * ============根据HQL语句查询=======
     **/

    public List findByHql(Class clsz, String hql, List<Object> params, int start, int max);

    public List findByHql(Class clsz, String hql, int start, int max, Object... params);

    public void deleteByHql(String hql, Object... params);

    /**
     * ========== 执行对象 增删查改================
     */

    public Integer addReturnID(String className, Object o);

    public boolean addByList(String className, List list);

    public boolean addByObject(String className, Object o);

    public boolean addByObject(Object o);

    public boolean updateByObject(String className, Object o);

    public boolean updateByObject(Object o);

    public boolean deleteByObject(String className, Object o);

    public Object findById(String className, Integer id);

    public Object findByEQ(String className, String propertyName, Object propertyValue);

    public int count(String className);

    public int countByPar(String className, String propertyName, Object propertyValue);

    public int coutByArray(String className, String[] propertyName, Object[] propertyValue, Integer[] model);

    public List findAll(String className);

    public List findEq(String className, String propertyName, Object propertyValue);

    public List findByPar(String className, String propertyName, Object propertyValue, String order, int isAsc,
                          int start, int end);

    public List findByParArr(String className, String[] propertyName, Object[] propertyValue, Integer[] model);

    public List findByArray(String className, String[] propertyName, Object[] propertyValue, Integer[] model,
                            String order, int isAsc, int start, int end);

    public List findByCriteria(String className, Order order, Criterion... criterions);

    public List findByCriteriaForMax(String className, int firstResult, int rowCount, Order order,
                                     Criterion... criterions);

    public boolean saveOrUpdate(String className, Object o);

    public boolean saveOrUpdateByList(String className, List list);

    public boolean executeSql(String sql);

    public Object findObjectByHql(Class clsz, String hql, Object... params);

    public Object findObjectByHql(Class clsz, String hql, List<Object> params);

    public boolean deleteById(Class clsz, Integer id);

    public int countByHql(String hql, List<Object> params);

    public int countByHql(String hql, Object... params);
    
    /**
	 * 方法描述：查询并返回List<Map<String, Object>>
	 * 创建人： LiMin
	 * 创建时间：Jan 30, 2015 4:21:30 PM
	 *
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findbyListResultMap(String sql, int start,int max, List<Object> params);

}
