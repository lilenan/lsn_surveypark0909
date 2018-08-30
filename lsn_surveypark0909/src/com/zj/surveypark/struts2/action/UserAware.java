package com.zj.surveypark.struts2.action;

import com.zj.surveypark.domain.User;

/**
 * 用户关注接口
 */
public interface UserAware {
	public void setUser(User user);
}
