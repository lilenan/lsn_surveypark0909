package com.zj.surveypark.service;


import java.util.List;
import java.util.Set;

import com.zj.surveypark.domain.security.Role;

public interface RoleService extends BaseService<Role> {

	/**
	 * 保存/更新角色
	 */
	public void saveOrUpdateRole(Role model, Integer[] ownRightIds);

	/**
	 * 查询不在指定范围中的角色集合
	 */
	public List<Role> findRolesNotInRange(Set<Role> roles);

}
