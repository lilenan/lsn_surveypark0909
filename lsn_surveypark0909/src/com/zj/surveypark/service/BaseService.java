package com.zj.surveypark.service;

import java.util.List;


/**
 * 这种service一般用于单体操作
 */
public interface BaseService<T> {
	//操作
	public void saveEntity(T t);
	public void updateEntity(T t);
	public void saveOrUpdateEntity(T t);
	public void deleteEntity(T t);
	//按照hql批处理
	public void batchEntityByHql(String hql,Object...objects);
		
	public T getEntity(Integer id);
	public T loadEntity(Integer id);
	public List<T> findEntityByHql(String hql,Object...objects);
	//单值检索（查询结果有且仅有一条记录）
	public Object uniqueResult(String hql,Object...objects);
	//查询所有实体
	public List<T> findAllEntities();
}
