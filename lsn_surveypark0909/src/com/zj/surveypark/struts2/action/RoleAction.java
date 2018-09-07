package com.zj.surveypark.struts2.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zj.surveypark.domain.security.Right;
import com.zj.surveypark.domain.security.Role;
import com.zj.surveypark.service.RightService;
import com.zj.surveypark.service.RoleService;

@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role> {
	private static final long serialVersionUID = 6094786057651268554L;
	private List<Role> allRoles;
	//角色没有的权限集合
	private List<Right> noOwnRights;
	//接受授予的权限id数组
	private Integer[] ownRightIds;
	private Integer roleId;
	
	@Resource
	private RoleService roleService;
	@Resource
	private RightService rightService;
	
	public List<Role> getAllRoles() {
		return allRoles;
	}
	public void setAllRoles(List<Role> allRoles) {
		this.allRoles = allRoles;
	}
	
	public List<Right> getNoOwnRights() {
		return noOwnRights;
	}
	public void setNoOwnRights(List<Right> noOwnRights) {
		this.noOwnRights = noOwnRights;
	}
	public Integer[] getOwnRightIds() {
		return ownRightIds;
	}
	public void setOwnRightIds(Integer[] ownRightIds) {
		this.ownRightIds = ownRightIds;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	/**
	 * 查询所有角色
	 */
	public String findAllRoles(){
		this.allRoles=roleService.findAllEntities();
		return "roleListPage";
	}
	
	/**
	 * 进入添加新角色的页面
	 */
	public String toAddRolePage(){
		this.noOwnRights=rightService.findAllEntities();
		return "addRolePage";
	}
	
	/**
	 * 保存/更新角色
	 */
	public String saveOrUpdateRole(){
		roleService.saveOrUpdateRole(model,ownRightIds);
		return "findAllRolesAction";
	}
	
	/**
	 * 编辑角色
	 */
	public String editRole(){
		this.model=roleService.getEntity(roleId);
		this.noOwnRights=rightService.findRightsNotInRange(model.getRights());
		return "editRolePage";
	}
	
	/**
	 * 删除角色
	 */
	public String deleteRole(){
		Role r=new Role();
		r.setId(roleId);
		roleService.deleteEntity(r);
		return "findAllRolesAction";
	}
}
