package com.zj.surveypark.service;

import com.zj.surveypark.domain.User;

public interface UserService extends BaseService<User> {

	//�ж������Ƿ�ռ��
	public boolean isRegisted(String email);

	//У���¼��Ϣ
	public User validataLoginInfo(String email, String md5);
	
}
