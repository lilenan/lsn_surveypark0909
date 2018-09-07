package com.zj.surveypark.struts2.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zj.surveypark.domain.User;
import com.zj.surveypark.domain.security.Role;
import com.zj.surveypark.service.RoleService;
import com.zj.surveypark.service.UserService;

/**
 * 用户授权action
 */
@Controller
@Scope("prototype")
public class UserAuthorizeAction extends BaseAction<User> {
	private static final long serialVersionUID = -4977974018698998076L;

	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;

	private List<User> allUsers;
	private Integer userId;
	private List<Role> noOwnRoles;
	private Integer[] ownRoleIds;
	public List<User> getAllUsers() {
		return allUsers;
	}
	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public List<Role> getNoOwnRoles() {
		return noOwnRoles;
	}
	public void setNoOwnRoles(List<Role> noOwnRoles) {
		this.noOwnRoles = noOwnRoles;
	}
	
	public Integer[] getOwnRoleIds() {
		return ownRoleIds;
	}
	public void setOwnRoleIds(Integer[] ownRoleIds) {
		this.ownRoleIds = ownRoleIds;
	}
	/**
	 * 查询所有用户
	 */
	public String findAllUsers(){
		this.allUsers=userService.findAllEntities();
		return "userAuthorizeListPage";
	}
	
	/**
	 * 修改授权
	 */
	public String editAuthorize(){
		this.model=userService.getEntity(userId);
		this.noOwnRoles=roleService.findRolesNotInRange(model.getRoles());
		return "userAuthorizePage";
	}
	
	/**
	 * 更新授权
	 */
	public String updateAuthorize(){
		//不能修改用户自身信息，只能修改角色内容
		userService.updateAuthorize(model,ownRoleIds);
		return "findAllUsersAction";
	}
	
	/**
	 * 清除授权
	 */
	public String clearAuthorize(){
		userService.clearAuthorize(userId);
		return "findAllUsersAction";
	}
}
