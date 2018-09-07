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
	//��ɫû�е�Ȩ�޼���
	private List<Right> noOwnRights;
	//���������Ȩ��id����
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
	 * ��ѯ���н�ɫ
	 */
	public String findAllRoles(){
		this.allRoles=roleService.findAllEntities();
		return "roleListPage";
	}
	
	/**
	 * ��������½�ɫ��ҳ��
	 */
	public String toAddRolePage(){
		this.noOwnRights=rightService.findAllEntities();
		return "addRolePage";
	}
	
	/**
	 * ����/���½�ɫ
	 */
	public String saveOrUpdateRole(){
		roleService.saveOrUpdateRole(model,ownRightIds);
		return "findAllRolesAction";
	}
	
	/**
	 * �༭��ɫ
	 */
	public String editRole(){
		this.model=roleService.getEntity(roleId);
		this.noOwnRights=rightService.findRightsNotInRange(model.getRights());
		return "editRolePage";
	}
	
	/**
	 * ɾ����ɫ
	 */
	public String deleteRole(){
		Role r=new Role();
		r.setId(roleId);
		roleService.deleteEntity(r);
		return "findAllRolesAction";
	}
}
