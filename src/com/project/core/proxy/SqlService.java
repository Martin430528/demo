package com.project.core.proxy;

import com.project.core.ar.ActiveRecord;
import com.project.utils.DataUtil;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.transform.Transformers;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes", "static-access"})
public class SqlService {
    private ActiveRecord ar = null;

    public SessionFactory getSF() {
        try {
            Class entityClass = Class.forName(String.class.getName());
            ar = ActiveRecord.of(entityClass);
            return ar.getSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将className压入 AR
     *
     * @param className
     * @return
     */
    private ActiveRecord intiAR(String className) {
        try {
            Class entityClass = Class.forName(className);
            ar = ActiveRecord.of(entityClass);
            return ar;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 根据对象进行添加
    public boolean add(String className, Object o) {
        try {
            intiAR(className).save(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addByList(String className, List list) {
        try {
            intiAR(className).saveOrUpdateAll(list);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Integer addReturnID(String className, Object o) {
        intiAR(className).save(o);
        Integer id = 0;
        try {
            Method m = o.getClass().getMethod("getId");
            id = (Integer) m.invoke(o);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 根据对象进行删除
    public boolean delete(String className, Object o) {
        try {
            intiAR(className).delete(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 根据对象进行更新
    public boolean update(String className, Object o) {
        try {
            intiAR(className).update(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据ID获取对象
     *
     * @param id
     * @param className
     * @return
     */
    public Object findById(String className, Serializable id) {
        return intiAR(className).get(id);
    }

    public Object findByEQ(String className, String propertyName, Object propertyValue) {
        return intiAR(className).findOneEq(propertyName, propertyValue);
    }

    /**
     * 根据对象返回记录总总条数
     *
     * @param className
     * @return
     */
    public int cout(String className) {
        return intiAR(className).count();
    }

    /**
     * 根据相应的参数返回记录总总条数
     *
     * @param className
     * @param propertyName
     * @param propertyValue
     * @return
     */
    public int count(String className, String propertyName, Object propertyValue) {
        return intiAR(className).count(propertyName, propertyValue);
    }

    /**
     * 根据相应的参数返回记录总总条数
     *
     * @param className
     * @param propertyName
     * @param propertyValue
     * @param model
     * @return
     */
    public int coutByArray(String className, String[] propertyName, Object[] propertyValue, Integer[] model) {
        return intiAR(className).count(propertyName, propertyValue, model);
    }

    /**
     * 查询所有记录
     *
     * @param className
     * @return
     */
    public List findAll(String className) {
        return intiAR(className).findAll();
    }

    /**
     * 查询符合条件的记录
     *
     * @param className
     * @param propertyName
     * @param propertyValue
     * @return
     */
    public List findEq(String className, String propertyName, Object propertyValue) {
        return intiAR(className).findEq(propertyName, propertyValue);
    }

    /**
     * 查询符合条件的记录，并按照参数排序
     *
     * @param className
     * @param propertyName
     * @param propertyValue
     * @param order
     * @param isAsc
     * @param start
     * @param end
     * @return
     */
    public List findByPar(String className, String propertyName, Object propertyValue, String order, int isAsc,
                          int start, int end) {
        Criterion se = DataUtil.getSE(propertyName, propertyValue, 1);
        Order order2 = null;
        if (isAsc == 1) {
            order2 = Order.asc(order);
        } else {
            order2 = Order.desc(order);
        }
        return intiAR(className).findByCriteria(start, end, order2, se);
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
        Criterion[] ce = DataUtil.getSE(propertyName, propertyValue, model);
        return intiAR(className).findByCriteria(ce);
    }

    /**
     * 根据相应的参数返回list
     *
     * @param propertyName
     * @param propertyValue
     * @param model
     * @param order         排序的字段名
     * @param isAsc         1为正序，其他为倒序
     * @param start         开始数
     * @param end           最大返回列数
     * @return
     */
    public List findByArray(String className, String[] propertyName, Object[] propertyValue, Integer[] model,
                            String order, int isAsc, int start, int end) {
        Criterion[] se = new Criterion[0];
        if (propertyName != null && propertyName.length > 0) {
            se = new Criterion[propertyName.length];
            for (int i = 0; i < propertyName.length; i++) {
                se[i] = DataUtil.getSE(propertyName[i], propertyValue[i], model[i]);
            }
        }
        Order order2 = null;
        if (null != order && !"".equals(order)) {
            if (isAsc == 1) {
                order2 = Order.asc(order);
            } else {
                order2 = Order.desc(order);
            }
        }
        return intiAR(className).findByCriteria(start, end, order2, se);
    }

    /**
     * 使用BQC参数返回list
     *
     * @param className
     * @param order
     * @param criterions
     * @return
     */
    public List findByCriteria(String className, Order order, Criterion... criterions) {
        return intiAR(className).findByCriteria(order, criterions);
    }

    /**
     * 使用BQC参数返回list
     *
     * @param className
     * @param order
     * @param criterions
     * @return
     */
    public List findByCriteria(String className, int firstResult, int rowCount, Order order, Criterion... criterions) {
        return intiAR(className).findByCriteria(firstResult, rowCount, order, criterions);
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
    public List findByHql(String className, String hql, int start, int max) {
        return intiAR(className).findByValueBean(hql, start, max);
    }

    public List findByHql(Class clsz, String hql, List<Object> params, int start, int max) {
        return intiAR(clsz.getName()).findByValueBean(hql, params, start, max);
    }

    /**
     * 执行SQL查询语句
     *
     * @param queryString
     * @return
     */
    public List findSql(String queryString) {
        String className = String.class.getName();
        return intiAR(className).findSql(queryString);
    }

    /**
     * 执行SQL语句
     *
     * @param queryString
     * @return
     */
    public void executeSql(String queryString) {
        String className = String.class.getName();
        intiAR(className).executeCall(queryString);
    }

    public void deleteByHql(String sql, Object... params) {
        String className = String.class.getName();
        if (ArrayUtils.isEmpty(params)) {
            intiAR(className).bulkUpdate(sql);
        } else {
            intiAR(className).bulkUpdate(sql, params);
        }

    }

    public boolean deleteById(String className, Serializable id) {
        try {
            intiAR(className).delete(intiAR(className).load(id));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 数据监听中,UPDATE只能用merge方式，才能监听到数据前后的修改值
     *
     * @param className
     * @param o
     * @return
     * @author bin ko
     * @create 2011-5-24 上午10:08:12
     */
    public boolean updateByListener(String className, Object o) {
        try {
            intiAR(className).merge(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveOrUpdate(String className, Object o) {
        intiAR(className).merge(o);
        return true;
    }

    public boolean saveOrUpdateByList(String className, List list) {
        intiAR(className).saveOrUpdateAll(list);
        return true;
    }

    public List findByListObject(Class cl, String sql) {
        Session session = null;
        try {
            SessionFactory sf = ar.getSessionFactory();
            session = sf.getCurrentSession();
            return session.createSQLQuery(SqlUtil.decode(sql)).addEntity(cl).list();
        } catch (Exception e) {
        }
        return null;
    }

    public List findByListObject(Class cl, String sql, List<Object> params) {
        Session session = null;
        try {
            SessionFactory sf = ar.getSessionFactory();
            session = sf.getCurrentSession();
            SQLQuery query = session.createSQLQuery(sql);
            if (!CollectionUtils.isEmpty(params)) {
                for (int i = 0, j = params.size(); i < j; i++) {
                    query.setParameter(i, params.get(i));
                }
            }
            return query.addEntity(cl).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int countBySql(String sql, List<Object> params) {
        Session session = null;
        try {
            SessionFactory sf = ar.getSessionFactory();
            session = sf.getCurrentSession();
            SQLQuery query = session.createSQLQuery(sql);
            if (!CollectionUtils.isEmpty(params)) {
                for (int i = 0, j = params.size(); i < j; i++) {
                    query.setParameter(i, params.get(i));
                }
            }
            Object res = query.uniqueResult();
            if(res == null){
            	return 0;
            }
            int totalRecord = NumberUtils.toInt(res.toString(), 0);
            return totalRecord;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public double countBySqlDouble(String sql) {
        Session session = null;
        try {
            SessionFactory sf = ar.getSessionFactory();
            session = sf.getCurrentSession();
            SQLQuery query = session.createSQLQuery(sql);
            Object res = query.uniqueResult();
            if(res == null){
            	return 0;
            }
            double totalRecord = NumberUtils.toDouble(res.toString());
            return totalRecord;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List findByList(String sql, List<Object> params) {
        Session session = null;
        try {
            SessionFactory sf = ar.getSessionFactory();
            session = sf.getCurrentSession();
            SQLQuery query = session.createSQLQuery(sql);
            if (!CollectionUtils.isEmpty(params)) {
                for (int i = 0, j = params.size(); i < j; i++) {
                    if (null == params.get(i) || StringUtils.isEmpty(params.get(i).toString())) {
                        continue;
                    }
                    query.setParameter(i, params.get(i));
                }
            }
            List datas = (List) query.list();
            return datas;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int executeSql(String queryString, List<Object> params) {
        String className = String.class.getName();
        return intiAR(className).executeCall(queryString, params);
    }

    public int countByHql(String hql, List<Object> params) {
        try {
            Session session = ar.getSessionFactory().getCurrentSession();
            Query query = session.createQuery(hql);
            if (!CollectionUtils.isEmpty(params)) {
                for (int i = 0, j = params.size(); i < j; i++) {
                    if (null == params.get(i) || StringUtils.isEmpty(params.get(i).toString())) {
                        continue;
                    }
                    query.setParameter(i, params.get(i));
                }
            }
            int count = NumberUtils.toInt(query.uniqueResult().toString(), 0);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> findbyListResultMap(String sql, int start,
			int max, List<Object> params){
		Session session = null;
		try {
			SessionFactory sf = ar.getSessionFactory();
			session = sf.openSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			if (!CollectionUtils.isEmpty(params)) {
				for (int i = 0, j = params.size(); i < j; i++) {
					if(null == params.get(i) || StringUtils.isEmpty(params.get(i).toString())){
						continue;
					}
					query.setParameter(i, params.get(i));
				}
			}
			query.setFirstResult(start);
			if(max > 0){
				query.setMaxResults(max);
			}
			List datas = (List) query.list();
			return datas;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}
    
}
