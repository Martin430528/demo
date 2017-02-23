package com.project.core.ar;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;

import com.project.utils.DataUtil;

import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActiveRecord {
	/**
	 * 所有实体类共用一个hibernate template。 以后可以扩展成不同的entity class可选择一个其它的hiernate
	 * template
	 */
	protected static HibernateTemplate t = new HibernateTemplate();

	/**
	 * 被of方法使用
	 */
	private static Map<Class<?>, ActiveRecord> forof = new HashMap<Class<?>, ActiveRecord>();

	/**
	 * 获取一个实体类的ActiveRecord对象，用于对该实体类进行查询等操作。比如: User user = (User)
	 * ActiveRecord.of(User.class).findOneEq("logonName", "joe");
	 *
	 * @param entityClass
	 * @return
	 */
	public static ActiveRecord of(Class<?> entityClass) {
		ActiveRecord ar = forof.get(entityClass);
		if (ar == null) {
			ar = new ActiveRecord(entityClass);
			forof.put(entityClass, ar);
		}
		return ar;
	}

	public static void setSessionFactory(SessionFactory sf) {
		t.setSessionFactory(sf);
	}

	public static SessionFactory getSessionFactory() {
		return t.getSessionFactory();
	}

	public static HibernateTemplate getHibernateTemplate() {
		return t;
	}

	// -------------------------------------------------------

	/**
	 * 具体的实体对象Class类???比如User.class,Topic.class
	 */
	private Class<?> entityClass;
	private String entityClassSimpleName;

	protected ActiveRecord() {
		this.entityClass = this.getClass();
		entityClassSimpleName = this.entityClass.getSimpleName();
	}

	private ActiveRecord(Class entityClass) {
		this.entityClass = entityClass;
		this.entityClassSimpleName = this.entityClass.getSimpleName();
	}

	// ----------------------------------

	// ----------------------------------

	/**
	 * 返回第一个对象，无时返回null
	 */
	public static Object unique(List list) {
		return list.size() == 0 ? null : list.get(0);
	}

	/**
	 * 返回第一个对象，无时抛出异常
	 */

	public static Object requiredUnique(List list) {
		if (list.size() == 0) {
			throw new RuntimeException();
		}
		return list.get(0);
	}

	// -------------------------------------------------------

	public int count() {
		return ((Number) unique(find("select count(*) from " + entityClass.getSimpleName()))).intValue();
	}

	public int count(String propertyName, Object propertyValue) {
		return ((Number) unique(
				find("select count(*) from " + entityClass.getSimpleName() + " obj where obj." + propertyName + "=?",
						propertyValue))).intValue();
	}

	public int count(String[] propertyNames, Object[] propertyValues) {
		if (propertyValues.length == 1) {
			return count(propertyNames[0], propertyValues[0]);
		}
		StringBuilder sb = new StringBuilder("select count(*) from ");
		sb.append(entityClass.getSimpleName()).append(" obj where ");
		for (int i = 0; i < propertyNames.length; i++) {
			if (i != 0) {
				sb.append(" AND ");
			}
			sb.append("obj.").append(propertyNames[i]).append("=?");
		}
		Object o = unique(find(sb.toString(), propertyValues));
		if (o != null)
			return ((Number) o).intValue();
		else
			return 0;

	}

	public int count(String[] propertyName, Object[] propertyValue, Integer[] model) {
		int count = 0;
		if (propertyName.length == 0) {
			count = count();
		} else {
			StringBuilder sb = new StringBuilder("select count(id) from ");
			sb.append(entityClass.getSimpleName()).append(" where ");
			for (int i = 0; i < propertyName.length; i++) {
				if (i != 0) {
					sb.append(" AND ");
				}
				sb.append(DataUtil.getSE(propertyName[i], propertyValue[i], model[i]));
			}
			count = ((Number) unique(find(sb.toString()))).intValue();
		}
		return count;
	}

	public List findByCriteria(Criterion... criterion) {
		DetachedCriteria detachedCrit = DetachedCriteria.forClass(entityClass);
		for (Criterion c : criterion) {
			if (c != null) {
				detachedCrit.add(c);
			}
		}
		return findByCriteria(detachedCrit);
	}

	public List findAll() {
		return t.find("from " + entityClassSimpleName);
	}

	public List findAll(Order order) {
		return findByCriteria(order);
	}

	public List find(int firstResult, int rowCount) {
		return findByCriteria(firstResult, rowCount);
	}

	public List findByCriteria(int firstResult, int rowCount, Order order, Criterion... criterions) {
		DetachedCriteria detachedCrit = DetachedCriteria.forClass(entityClass);

		for (Criterion c : criterions) {
			if (c != null) {
				detachedCrit.add(c);
			}
		}
		if (order != null)
			detachedCrit.addOrder(order);
		return findByCriteria(detachedCrit, firstResult, rowCount);
	}

	public List findByCriteria(int firstResult, int rowCount, Criterion... criterions) {
		DetachedCriteria detachedCrit = DetachedCriteria.forClass(entityClass);
		if (null != criterions) {
			for (Criterion c : criterions) {
				if (c != null) {
					detachedCrit.add(c);
				}
			}
		}
		return findByCriteria(detachedCrit, firstResult, rowCount);
	}

	public List findByCriteria(Order order, Criterion... criterions) {
		return findByCriteria(new Order[] { order }, criterions);
	}

	public List findByCriteria(Order order1, Order order2, Criterion... criterions) {
		return findByCriteria(new Order[] { order1, order2 }, criterions);
	}

	public List findByCriteria(Order[] orders, Criterion... criterions) {
		DetachedCriteria detachedCrit = DetachedCriteria.forClass(entityClass);
		if (criterions != null) {
			for (Criterion c : criterions) {
				if (c != null) {
					detachedCrit.add(c);
				}
			}
		}
		if (orders != null) {
			for (Order order : orders) {
				detachedCrit.addOrder(order);
			}
		}
		return findByCriteria(detachedCrit);
	}

	public Object findOneEq(String propertyName, Object value) {
		return unique(findEq(propertyName, value));
	}

	public List findEq(String propertyName, Object value) {
		return findByCriteria(Restrictions.eq(propertyName, value));
	}

	public List findBt(String propertyName, Object lo, Object hi) {
		return findByCriteria(Restrictions.between(propertyName, lo, hi));
	}

	public List findLt(String propertyName, Object v) {
		return findByCriteria(Restrictions.lt(propertyName, v));
	}

	public List findLe(String propertyName, Object v) {
		return findByCriteria(Restrictions.le(propertyName, v));
	}

	public List findGt(String propertyName, Object v) {
		return findByCriteria(Restrictions.gt(propertyName, v));
	}

	public List findGe(String propertyName, Object v) {
		return findByCriteria(Restrictions.ge(propertyName, v));
	}

	public Object findOneWhere(String where, Object... args) {
		return unique(findWhere(where, args));
	}

	public List findWhere(String where, Object... args) {
		String csn = entityClass.getSimpleName();
		StringBuilder sb = new StringBuilder();
		sb.append("from ").append(csn).append(' ').append(Character.toLowerCase(csn.charAt(0))).append(" where ")
				.append(where);
		return find(sb.toString(), args);
	}

	public List findSql(String queryString) {
		Session session = t.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(queryString);
		List l = query.list();
		return l;
	}

	public void executeCall(String queryString) {
		Session session = t.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(queryString);
		query.executeUpdate();
	}

	public String findCall(String queryString) {
		Session session = t.getSessionFactory().getCurrentSession();
		session.setFlushMode(FlushMode.AUTO);
		Transaction t = session.beginTransaction();
		try {
			Query query = session.createSQLQuery(queryString);
			query.setTimeout(15);
			String q = (String) query.uniqueResult();
			t.commit();
			return q;
		} catch (Exception e) {
			return null;
		}
	}

	public Object get(Serializable id, boolean lock) {
		if (lock)
			return get(id, LockMode.PESSIMISTIC_WRITE);
		else
			return get(id);
	}

	public Object load(Serializable id, boolean lock) {
		if (lock)
			return load(id, LockMode.PESSIMISTIC_WRITE);
		else
			return load(id);
	}

	public Object get(String entityName, Serializable id, boolean lock) {
		if (lock)
			return get(entityName, id, LockMode.PESSIMISTIC_WRITE);
		else
			return get(entityName, id);
	}

	public Object load(String entityName, Serializable id, boolean lock) {
		if (lock)
			return load(entityName, id, LockMode.PESSIMISTIC_WRITE);
		else
			return load(entityName, id);
	}

	public Object execute(HibernateCallback action) throws DataAccessException {
		return t.execute(action);
	}

	public List executeFind(HibernateCallback action) throws DataAccessException {
		return (List) t.execute(action);
	}

	// -------------------------------------------------------------------------
	// Convenience methods for loading individual objects
	// -------------------------------------------------------------------------

	public Object get(Serializable id) throws DataAccessException {
		return t.get(entityClass, id);
	}

	public Object get(Serializable id, LockMode lockMode) throws DataAccessException {
		return t.get(entityClass, id, lockMode);
	}

	public Object get(String entityName, Serializable id) throws DataAccessException {
		return t.get(entityName, id);
	}

	public Object get(String entityName, Serializable id, LockMode lockMode) throws DataAccessException {
		return t.get(entityName, id, lockMode);
	}

	public Object load(Serializable id) throws DataAccessException {
		return t.load(entityClass, id);
	}

	public Object load(Serializable id, LockMode lockMode) throws DataAccessException {
		return t.load(entityClass, id, lockMode);
	}

	public Object load(String entityName, Serializable id) throws DataAccessException {
		return t.load(entityName, id);
	}

	public Object load(String entityName, Serializable id, LockMode lockMode) throws DataAccessException {
		return t.load(entityName, id, lockMode);
	}

	public void load() throws DataAccessException {
		t.load(this, _getId());
	}

	public List loadAll() throws DataAccessException {
		return t.loadAll(entityClass);
	}

	public Serializable _getId() {
		Method[] ms = this.entityClass.getClass().getMethods();
		boolean found = false;
		Serializable id = null;
		for (int i = 0; i < ms.length; i++) {
			Method m = ms[i];
			if (m.getAnnotation(Id.class) != null) {
				try {
					id = (Serializable) m.invoke(this);
					found = true;
				} catch (Exception e) {
					throw new InvalidDataAccessApiUsageException("fail to get the id of object: " + this);
				}
				break;
			}
		}
		if (!found) {
			throw new InvalidDataAccessApiUsageException("not found the id getter.");
		}
		if (id == null) {
			throw new IllegalArgumentException("the id value is not assign for this object.");
		}
		return id;
	}

	public void refresh() throws DataAccessException {
		t.refresh(this);
	}

	public void refresh(LockMode lockMode) throws DataAccessException {
		t.refresh(this, lockMode);
	}

	public boolean contains() throws DataAccessException {
		return t.contains(this);
	}

	public void evict() throws DataAccessException {
		t.evict(this);
	}

	public void initialize() throws DataAccessException {
		t.initialize(this);
	}

	public Filter enableFilter(String filterName) throws IllegalStateException {
		return t.enableFilter(filterName);
	}

	// -------------------------------------------------------------------------
	// Convenience methods for storing individual objects
	// -------------------------------------------------------------------------

	public void lock(LockMode lockMode) throws DataAccessException {
		t.lock(this, lockMode);
	}

	public void lock(String entityName, LockMode lockMode) throws DataAccessException {
		t.lock(entityName, this, lockMode);
	}

	public Serializable save() throws DataAccessException {
		return t.save(this);
	}

	public Serializable save(Object entity) throws DataAccessException {
		return t.save(entity);
	}

	public Serializable save(String entityName) throws DataAccessException {
		return t.save(entityName, this);
	}

	public void update(Object entity) throws DataAccessException {
		t.update(entity);
	}

	public void update() throws DataAccessException {
		t.update(this);
	}

	public void update(LockMode lockMode) throws DataAccessException {
		t.update(this, lockMode);
	}

	public void update(String entityName) throws DataAccessException {
		t.update(entityName, this);
	}

	public void update(String entityName, LockMode lockMode) throws DataAccessException {
		t.update(entityName, this, lockMode);
	}

	public void saveOrUpdate(Object entity) throws DataAccessException {
		t.saveOrUpdate(entity);
	}

	public void saveOrUpdate() throws DataAccessException {
		t.saveOrUpdate(this);
	}

	public void saveOrUpdate(String entityName) throws DataAccessException {
		t.save(entityName, this);
	}

	/**
	 * hibernate4已经删除saveOrUpdateAll方法，重写就是迭代器中单独调用saveOrUpdate
	 *
	 * @param entities
	 * @throws DataAccessException
	 * @author: 郭志龙
	 * @return: void
	 */
	public void saveOrUpdateAll(Collection entities) throws DataAccessException {
		for (Object entitie : entities) {
			if (t.contains(entitie)) {
				t.merge(entitie);
			} else {
				t.saveOrUpdate(entitie);
			}
		}
	}

	public void replicate(ReplicationMode replicationMode) throws DataAccessException {
		t.replicate(this, replicationMode);
	}

	public void replicate(String entityName, ReplicationMode replicationMode) throws DataAccessException {
		t.replicate(entityName, this, replicationMode);
	}

	public void persist() throws DataAccessException {
		t.persist(this);
	}

	public void persist(String entityName) throws DataAccessException {
		t.persist(entityName, this);
	}

	public Object merge() throws DataAccessException {
		return t.merge(this);
	}

	public Object merge(String entityName) throws DataAccessException {
		return t.merge(entityName, this);
	}

	public Object merge(Object entity) throws DataAccessException {
		return t.merge(entity);
	}

	public void delete(Object entity) throws DataAccessException {
		t.delete(entity);
	}

	public void delete() throws DataAccessException {
		t.delete(this);
	}

	public void delete(LockMode lockMode) throws DataAccessException {
		t.delete(this, lockMode);
	}

	public void deleteAll(Collection entities) throws DataAccessException {
		t.deleteAll(entities);
	}

	public void flush() throws DataAccessException {
		t.flush();
	}

	public void clear() throws DataAccessException {
		t.clear();
	}

	// -------------------------------------------------------------------------
	// Convenience finder methods for HQL strings
	// -------------------------------------------------------------------------

	public List find(String queryString, Object... values) throws DataAccessException {
		t.setMaxResults(0);
		return t.find(queryString, values);
	}

	public List find(String queryString, int max, Object... values) throws DataAccessException {
		t.setMaxResults(max);
		return t.find(queryString, values);
	}

	public List findByNamedParam(String queryString, String paramName, Object value) throws DataAccessException {
		return t.findByNamedParam(queryString, paramName, value);
	}

	public List findByNamedParam(String queryString, String[] paramNames, Object[] values) throws DataAccessException {
		return t.findByNamedParam(queryString, paramNames, values);
	}

	public List findByValueBean(String queryString, Object valueBean) throws DataAccessException {
		return t.findByValueBean(queryString, valueBean);
	}

	public List findByValueBean(String queryString) throws DataAccessException {

		return t.findByValueBean(queryString, this);
	}

	public List findByValueBean(String queryString, int max) throws DataAccessException {
		t.setMaxResults(max);
		return t.findByValueBean(queryString, this);
	}

	public List findByValueBean(final String queryString, final int start, final int max) throws DataAccessException {
		return (List) t.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(queryString);
				query.setFirstResult(start);
				if (max > 0) {
					query.setMaxResults(max);
				}
				List list = query.list();
				return list;
			}
		});
	}

	public List findByValueBean(final String queryString, final List<Object> params, final int start, final int max) {
		return (List) t.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(queryString);
				if (!CollectionUtils.isEmpty(params)) {
					for (int i = 0, j = params.size(); i < j; i++) {
						if (null == params.get(i) || StringUtils.isEmpty(params.get(i).toString())) {
							continue;
						}
						query.setParameter(i, params.get(i));
					}
				}
				query.setFirstResult(start);
				if (max > 0) {
					query.setMaxResults(max);
				}
				List list = query.list();
				return list;
			}
		});
	}

	// -------------------------------------------------------------------------
	// Convenience finder methods for named queries
	// -------------------------------------------------------------------------

	public List findByNamedQuery(String queryName, Object... values) throws DataAccessException {
		return t.findByNamedQuery(queryName, values);
	}

	public List findByNamedQueryAndNamedParam(String queryName, String paramName, Object value)
			throws DataAccessException {
		return t.findByNamedQueryAndNamedParam(queryName, paramName, value);
	}

	public List findByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object[] values)
			throws DataAccessException {
		return t.findByNamedQueryAndNamedParam(queryName, paramNames, values);
	}

	public List findByNamedQueryAndValueBean(String queryName, Object valueBean) throws DataAccessException {
		return t.findByNamedQueryAndValueBean(queryName, valueBean);
	}

	public List findByNamedQueryAndValueBean(String queryName) throws DataAccessException {
		return t.findByNamedQueryAndValueBean(queryName, this);
	}

	// -------------------------------------------------------------------------
	// Convenience finder methods for detached criteria
	// -------------------------------------------------------------------------
	public List findByCriteria(DetachedCriteria criteria) throws DataAccessException {
		t.setMaxResults(0);
		return t.findByCriteria(criteria);
	}

	public List findByCriteria(DetachedCriteria criteria, int firstResult, int maxResults) throws DataAccessException {
		return t.findByCriteria(criteria, firstResult, maxResults);
	}

	public List findByExample() throws DataAccessException {
		return t.findByExample(this);
	}

	public List findByExample(int firstResult, int maxResults) throws DataAccessException {
		return t.findByExample(this, firstResult, maxResults);
	}

	// -------------------------------------------------------------------------
	// Convenience query methods for iteration and bulk updates/deletes
	// -------------------------------------------------------------------------

	public Iterator iterate(String queryString, Object... values) throws DataAccessException {
		return t.iterate(queryString, values);
	}

	public void closeIterator(Iterator it) throws DataAccessException {
		t.closeIterator(it);
	}

	public int bulkUpdate(String queryString, Object[] values) throws DataAccessException {
		return t.bulkUpdate(queryString, values);
	}

	public int bulkUpdate(String hql) {
		return t.bulkUpdate(hql);
	}

	public int executeCall(String queryString, List<Object> params) {
		Session session = t.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(queryString);
		if (CollectionUtils.isNotEmpty(params)) {
			for (int i = 0, j = params.size(); i < j; i++) {
				query.setParameter(i, params.get(i));
			}
		}
		int result = query.executeUpdate();
		return result;
	}
}
