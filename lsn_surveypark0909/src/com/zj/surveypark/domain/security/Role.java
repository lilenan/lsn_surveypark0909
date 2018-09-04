package com.zj.surveypark.domain.security;

import java.util.HashSet;
import java.util.Set;

/**
 * 角色
 */
public class Role {
	private Integer id;
	private String roleName;
	private String roleValue;
	private String roleDesc;

	// 建立从Role到Right之间多对多关联关系
	private Set<Right> rights = new HashSet<Right>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleValue() {
		return roleValue;
	}

	public void setRoleValue(String roleValue) {
		this.roleValue = roleValue;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Set<Right> getRights() {
		return rights;
	}

	public void setRights(Set<Right> rights) {
		this.rights = rights;
	}

}
