package com.zj.surveypark.service;


import com.zj.surveypark.domain.security.Role;

public interface RoleService extends BaseService<Role> {

	/**
	 * 保存/更新角色
	 */
	public void saveOrUpdateRole(Role model, Integer[] ownRightIds);

}
