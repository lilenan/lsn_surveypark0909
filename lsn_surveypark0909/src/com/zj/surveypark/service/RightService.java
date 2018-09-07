package com.zj.surveypark.service;


import java.util.List;
import java.util.Set;

import com.zj.surveypark.domain.security.Right;

public interface RightService extends BaseService<Right> {

	public void saveOrUpdateRight(Right model);

	/**
	 * ����url׷��Ȩ��
	 */
	public void appendRightByUrl(String url);

	/**
	 * ��������Ȩ��
	 */
	public void batchUpdateRights(List<Right> allRights);

	/**
	 * ��ѯ��ָ����Χ�е�Ȩ��
	 */
	public List<Right> findRightsInRange(Integer[] ownRightIds);
	
	/**
	 * ��ѯ ����ָ����Χ�е�Ȩ��
	 */
	public List<Right> findRightsNotInRange(Set<Right> rights);

	/**
	 * ��ѯ ����Ȩ��λ
	 */
	public int getMaxRightPos();


}
