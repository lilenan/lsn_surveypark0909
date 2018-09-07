package com.zj.surveypark.service;


import java.util.List;
import java.util.Set;

import com.zj.surveypark.domain.security.Role;

public interface RoleService extends BaseService<Role> {

	/**
	 * ����/���½�ɫ
	 */
	public void saveOrUpdateRole(Role model, Integer[] ownRightIds);

	/**
	 * ��ѯ����ָ����Χ�еĽ�ɫ����
	 */
	public List<Role> findRolesNotInRange(Set<Role> roles);

}
