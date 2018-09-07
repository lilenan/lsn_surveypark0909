package com.zj.surveypark.service;

import com.zj.surveypark.domain.User;

public interface UserService extends BaseService<User> {

	//判断邮箱是否占用
	public boolean isRegisted(String email);

	//校验登录信息
	public User validataLoginInfo(String email, String md5);

	//更新用户授权信息
	public void updateAuthorize(User r, Integer[] ownRoleIds);

	//清楚用户授权
	public void clearAuthorize(Integer userId);
	
}
