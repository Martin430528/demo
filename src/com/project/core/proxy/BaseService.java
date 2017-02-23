package com.project.core.proxy;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import com.project.utils.SpringContextUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class BaseService implements IBaseService {
    Logger logger = Logger.getLogger(this.getClass());
    SqlService service = (SqlService) SpringContextUtil.getBean("sqlService");

    public boolean add(String sql) {
        try {
            service.executeSql(SqlUtil.decode(sql));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(String sql) {
        try {
            service.executeSql(SqlUtil.decode(sql));
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean addByList(String className, List list) {
        return this.service.addByList(className, list);
    }

    public boolean addByObject(String className, Object o) {
        return this.service.add(className, o);
    }

    public Integer addReturnID(String className, Object o) {
        return this.service.addReturnID(className, o);
    }

    public boolean updateByObject(String className, Object o) {
        return this.service.update(className, o);
    }

    public boolean deleteByObject(String className, Object o) {
        return this.service.delete(className, o);
    }

    public Object findById(String className, Integer id) {
        return this.service.findById(className, id);
    }

    public int count(String className) {
        return this.service.cout(className);
    }

    public int countByPar(String className, String propertyName, Object propertyValue) {
        return this.service.count(className, propertyName, propertyValue);
    }

    public int coutByArray(String className, String[] propertyName, Object[] propertyValue, Integer[] model) {
        return this.service.coutByArray(className, propertyName, propertyValue, model);
    }

    public List findAll(String className) {
        return this.service.findAll(className);
    }

    public List findEq(String className, String propertyName, Object propertyValue) {
        return this.service.findEq(className, propertyName, propertyValue);
    }

    public List findByPar(String className, String propertyName, Object propertyValue, String order, int isAsc,
                          int start, int end) {
        return this.service.findByPar(className, propertyName, propertyValue, order, isAsc, start, end);
    }

    /**
     * 根据属性及其值数组查询
     *
     * @param className
     * @param propertyName
     * @param propertyValue
     * @param model
     * @return
     * @author 蒲刚敏
     * @date 2010-12-23
     */
    public List findByParArr(String className, String[] propertyName, Object[] propertyValue, Integer[] model) {
        return this.service.findByParArr(className, propertyName, propertyValue, model);
    }

    /**
     * 根据属性及其值数组查询 如果不需要排序 则order可以为""或null
     */
    public List findByArray(String className, String[] propertyName, Object[] propertyValue, Integer[] model,
                            String order, int isAsc, int start, int end) {
        return this.service.findByArray(className, propertyName, propertyValue, model, order, isAsc, start, end);
    }

    public List findByCriteria(String className, Order order, Criterion... criterions) {
        return this.service.findByCriteria(className, order, criterions);
    }

    public List findByCriteriaForMax(String className, int firstResult, int rowCount, Order order,
                                     Criterion... criterions) {

        return this.service.findByCriteria(className, firstResult, rowCount, order, criterions);
    }

    /**
     * 根据HQL语句查询记录集合
     *
     * @param className 对象名 该hql语句用到的对象随便传个即可
     * @param hql       hql语句
     * @param start     起始位置，不需要可以为0
     * @param max       最多返回的记录数 不需要该参数时 可以设置为0
     * @return
     * @author 蒲刚敏
     * @date 2010-12-24
     */
    public void deleteByHql(String hql, Object... params) {
        this.service.deleteByHql(hql, params);
    }

    public Object findByEQ(String className, String propertyName, Object propertyValue) {
        return this.service.findByEQ(className, propertyName, propertyValue);
    }

    // 数据监听
    public boolean saveOrUpdate(String className, Object o) {
        return this.service.saveOrUpdate(className, o);
    }

    public boolean saveOrUpdateByList(String className, List list) {
        return this.service.saveOrUpdateByList(className, list);
    }

    public boolean executeSql(String sql) {
        this.service.executeSql(SqlUtil.decode(sql));
        return true;
    }

    public SessionFactory getSF() {
        return this.service.getSF();
    }

    public List findByListObject(Class cl, String sql) {
        return this.service.findByListObject(cl, sql);
    }

    public List findByListObject(Class cl, String sql, List<Object> params) {
        return this.service.findByListObject(cl, sql, params);
    }

    public List findByListObject(Class cl, String sql, Object... params) {
        return this.service.findByListObject(cl, sql, Arrays.asList(params));
    }

    public List getListSql(String sql) {
        return findByList(sql);
    }

    public int countBySql(String sql, List<Object> params) {
        return this.service.countBySql(sql, params);
    }
    
    public double countBySqlDouble(String sql) {
        return this.service.countBySqlDouble(sql);
    }

    public int countBySql(String sql, Object... params) {
        return this.service.countBySql(sql, Arrays.asList(params));
    }

    public boolean updateByObject(Object o) {
        return this.service.update(o.getClass().getName(), o);
    }

    public boolean addByObject(Object o) {
        return this.service.add(o.getClass().getName(), o);
    }

    public Object findObjectByHql(Class clsz, String hql, Object... params) {
        List ls = this.service.findByHql(clsz, hql, Arrays.asList(params), 0, 1);
        if (ls == null || ls.isEmpty())
            return null;
        return ls.get(0);
    }

    public Object findObjectByHql(Class clsz, String hql, List<Object> params) {
        List ls = this.service.findByHql(clsz, hql, params, 0, 1);
        if (ls == null || ls.isEmpty())
            return null;
        return ls.get(0);
    }

    public List findByList(String sql, List<Object> params) {
        return this.service.findByList(sql, params);
    }

    public List findByList(String sql, Object... params) {
        return this.service.findByList(sql, Arrays.asList(params));
    }

    public Object[] findByOne(String sql, List<Object> params) {
        List<?> l = findByList(sql, params);
        if (CollectionUtils.isNotEmpty(l)) {
            return (Object[]) l.get(0);
        }
        return null;
    }

    public Object[] findByOne(String sql, Object... params) {
        List<?> l = findByList(sql, params);
        if (CollectionUtils.isNotEmpty(l)) {
            return (Object[]) l.get(0);
        }
        return null;
    }

    public boolean update(String sql, List<Object> params) {
        try {
            service.executeSql(SqlUtil.decode(sql), params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(String sql, Object... params) {
        return update(sql, Arrays.asList(params));
    }

    public Object findOneReturn(String sql, List<Object> params) {
        List<?> l = findByList(sql, params);
        if (CollectionUtils.isNotEmpty(l)) {
            return (Object) l.get(0);
        }
        return null;
    }

    public Object findOneReturn(String sql, Object... params) {
        List<?> l = findByList(sql, params);
        if (CollectionUtils.isNotEmpty(l)) {
            return (Object) l.get(0);
        }
        return null;
    }

    public List findByHql(Class clsz, String hql, List<Object> params, int start, int max) {
        return this.service.findByHql(clsz, hql, params, start, max);
    }

    public List findByHql(Class clsz, String hql, int start, int max, Object... params) {
        return this.service.findByHql(clsz, hql, Arrays.asList(params), start, max);
    }

    public boolean deleteById(Class clsz, Integer id) {
        return this.service.deleteById(clsz.getName(), id);
    }

    public int countByHql(String hql, List<Object> params) {
        return this.service.countByHql(hql, params);
    }

    public int countByHql(String hql, Object... params) {
        return this.service.countByHql(hql, Arrays.asList(params));
    }

    public List<Map<String, Object>> findbyListResultMap(String sql, int start,
			int max, List<Object> params) {
		return this.service.findbyListResultMap(sql, start, max, params);
	}
}
