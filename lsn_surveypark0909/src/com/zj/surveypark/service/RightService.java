package com.zj.surveypark.service;


import java.util.List;
import java.util.Set;

import com.zj.surveypark.domain.security.Right;

public interface RightService extends BaseService<Right> {

	public void saveOrUpdateRight(Right model);

	/**
	 * 按照url追加权限
	 */
	public void appendRightByUrl(String url);

	/**
	 * 批量更新权限
	 */
	public void batchUpdateRights(List<Right> allRights);

	/**
	 * 查询在指定范围中的权限
	 */
	public List<Right> findRightsInRange(Integer[] ownRightIds);
	
	/**
	 * 查询 不在指定范围中的权限
	 */
	public List<Right> findRightsNotInRange(Set<Right> rights);

	/**
	 * 查询 最大的权限位
	 */
	public int getMaxRightPos();


}
