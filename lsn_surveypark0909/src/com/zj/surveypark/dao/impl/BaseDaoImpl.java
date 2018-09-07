package com.zj.surveypark.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;

import com.zj.surveypark.dao.BaseDao;

/**
 * dao����ʵ��
 * 
 * @param <T>
 */
@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<T> implements BaseDao<T> {

	@Resource
	private SessionFactory sf;
	private Class<T> clazz;

	// �õ�ÿ��������ָ���ķ���

	public BaseDaoImpl() {
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		clazz = (Class<T>) type.getActualTypeArguments()[0];// �õ�ʵ��
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

	// ִ��ԭ����sql���
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

	// ��ֵ��������ѯ������ҽ���һ����¼��
	public Object uniqueResult(String hql, Object... objects) {
		Query q = sf.getCurrentSession().createQuery(hql);
		for (int i = 0; i < objects.length; i++) {
			q.setParameter(i, objects[i]);
		}
		return q.uniqueResult();
	}
	
	//����sql��ѯ
	public List<T> findObjectBySql(String sql,Object...objects){
		SQLQuery q = sf.getCurrentSession().createSQLQuery(sql);
		for (int i = 0; i < objects.length; i++) {
			q.setParameter(i, objects[i]);
		}
		//���ʵ���࣬��sql��ѯ�ļ����У�������������װ��ʵ�����
		q.addEntity(clazz);
		return q.list();
	}
}
