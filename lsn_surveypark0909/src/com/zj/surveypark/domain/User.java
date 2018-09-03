package com.zj.surveypark.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.zj.surveypark.domain.security.Role;

/**
 * User 
 */
public class User {
	private Integer id;
	private  String email;
	private String password;
	private String nickName;
	private Date regDate=new Date();
	
	private Set<Role> roles=new HashSet<>();
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
}
