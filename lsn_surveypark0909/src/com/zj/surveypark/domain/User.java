package com.zj.surveypark.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.zj.surveypark.domain.security.Right;
import com.zj.surveypark.domain.security.Role;

/**
 * User 
 */
public class User extends BaseEntity {
	private static final long serialVersionUID = 3161470184247632823L;
	private Integer id;
	private  String email;
	private String password;
	private String nickName;
	private Date regDate=new Date();
	private boolean superAdmin;//是否是超级管理员
	
	//角色集合
	private Set<Role> roles=new HashSet<>();
	//用户权限总和
	private long[] rightSum;
	
	public boolean isSuperAdmin() {
		return superAdmin;
	}
	public void setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
	}
	public long[] getRightSum() {
		return rightSum;
	}
	public void setRightSum(long[] rightSum) {
		this.rightSum = rightSum;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	
	/**
	 * 计算用户的权限的总和
	 */
	public void calculateRightSum() {
		int pos=0;
		long code=0;
		for(Role role:roles){
			//判断超级管理员
			if("-1".equals(role.getRoleValue())){
				this.superAdmin=true;
				roles=null;
				return ;
			}
			for(Right r:role.getRights()){
				pos=r.getRightPos();
				code=r.getRightCode();
				rightSum[pos]=rightSum[pos] | code;
			}
		}
		roles=null;
	}
	
	/**
	 * 判断用户是否有指定的权限
	 */
	public boolean hasRight(Right r) {
		int pos=r.getRightPos();
		long code=r.getRightCode();
		long ret=rightSum[pos] & code;
		return !(ret==0);
	}
}
