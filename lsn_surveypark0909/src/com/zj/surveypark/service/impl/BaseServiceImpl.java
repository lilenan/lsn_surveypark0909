package com.zj.surveypark.service.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import com.zj.surveypark.dao.BaseDao;
import com.zj.surveypark.service.BaseService;

/**
 * 抽象的service实现，专门用于继承
 * @param <T>
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {
	private BaseDao<T> dao;
	
	private Class<T> clazz;
	@SuppressWarnings("unchecked")
	public BaseServiceImpl() {
		ParameterizedType type=(ParameterizedType) this.getClass().getGenericSuperclass();
		clazz=(Class<T>) type.getActualTypeArguments()[0];
	}

	//注入dao
	@Resource
	public void setDao(BaseDao<T> dao) {
		this.dao = dao;
	}
	
	public void saveEntity(T t) {
		dao.saveEntity(t);
	}
	
	public void updateEntity(T t) {
		dao.updateEntity(t);
	}
	
	public void saveOrUpdateEntity(T t) {
		dao.saveOrUpdateEntity(t);
	}
	
	public void deleteEntity(T t) {
		dao.deleteEntity(t);
	}
	
	public void batchEntityByHql(String hql, Object... objects) {
		dao.batchEntityByHql(hql, objects);
	}
	
	public T getEntity(Integer id) {
		return dao.getEntity(id);
	}
	
	public T loadEntity(Integer id) {
		return dao.loadEntity(id);
	}
	
	public List<T> findEntityByHql(String hql, Object... objects) {
		return dao.findEntityByHql(hql, objects);
	}
	
	//单值检索（查询结果有且仅有一条记录）
	public Object uniqueResult(String hql,Object...objects){
		return  dao.uniqueResult(hql, objects);
	}
	
	public List<T> findAllEntities(){
		String hql="from "+clazz.getSimpleName();
		return (List<T>) this.findEntityByHql(hql);
	}
}
