package com.zj.surveypark.service;


import com.zj.surveypark.domain.security.Role;

public interface RoleService extends BaseService<Role> {

	/**
	 * ����/���½�ɫ
	 */
	public void saveOrUpdateRole(Role model, Integer[] ownRightIds);

}
