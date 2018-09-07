package com.zj.surveypark.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;

import com.zj.surveypark.dao.BaseDao;

/**
 * dao基类实现
 * 
 * @param <T>
 */
@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<T> implements BaseDao<T> {

	@Resource
	private SessionFactory sf;
	private Class<T> clazz;

	// 得到每个子类所指定的泛型

	public BaseDaoImpl() {
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		clazz = (Class<T>) type.getActualTypeArguments()[0];// 得到实参
	}

	public void saveEntity(T t) {
		sf.getCurrentSession().save(t);
	}

	public void updateEntity(T t) {
		sf.getCurrentSession().update(t);
	}

	public void saveOrUpdateEntity(T t) {
		sf.getCurrentSession().saveOrUpdate(t);
	}

	public void deleteEntity(T t) {
		sf.getCurrentSession().delete(t);
	}

	public void batchEntityByHql(String hql, Object... objects) {
		Query q = sf.getCurrentSession().createQuery(hql);
		for (int i = 0; i < objects.length; i++) {
			q.setParameter(i, objects[i]);
		}
		q.executeUpdate();
	}

	// 执行原生的sql语句
	public void executeSQL(String sql, Object... objects) {
		SQLQuery q = sf.getCurrentSession().createSQLQuery(sql);
		for (int i = 0; i < objects.length; i++) {
			q.setParameter(i, objects[i]);
		}
		q.executeUpdate();
	}

	public T getEntity(Integer id) {
		return (T) sf.getCurrentSession().get(clazz, id);
	}

	public T loadEntity(Integer id) {
		return (T) sf.getCurrentSession().load(clazz, id);
	}

	public List<T> findEntityByHql(String hql, Object... objects) {
		Query q = sf.getCurrentSession().createQuery(hql);
		for (int i = 0; i < objects.length; i++) {
			q.setParameter(i, objects[i]);
		}
		return q.list();
	}

	// 单值检索（查询结果有且仅有一条记录）
	public Object uniqueResult(String hql, Object... objects) {
		Query q = sf.getCurrentSession().createQuery(hql);
		for (int i = 0; i < objects.length; i++) {
			q.setParameter(i, objects[i]);
		}
		return q.uniqueResult();
	}
	
	//按照sql查询
	public List<T> findObjectBySql(String sql,Object...objects){
		SQLQuery q = sf.getCurrentSession().createSQLQuery(sql);
		for (int i = 0; i < objects.length; i++) {
			q.setParameter(i, objects[i]);
		}
		//添加实体类，在sql查询的集合中，将对象数组组装成实体对象
		q.addEntity(clazz);
		return q.list();
	}
}
