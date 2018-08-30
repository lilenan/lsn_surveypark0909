package com.zj.surveypark.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zj.surveypark.dao.BaseDao;
import com.zj.surveypark.domain.User;
import com.zj.surveypark.service.UserService;
import com.zj.surveypark.util.ValidateUtil;

/**
 * userService
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	/**
	 * 重写该方法,覆盖注解
	 */
	@Resource(name="userDao")
	public void setDao(BaseDao<User> dao) {
		super.setDao(dao);
	}

	public boolean isRegisted(String email) {
		String hql="from User u where u.email=?";
		List<User> list=this.findEntityByHql(hql, email);
		return ValidateUtil.isValid(list);
	}
	
	//校验登录信息
		public User validataLoginInfo(String email, String password){
			String hql="from User u where u.email=? and u.password=?";
			List<User> list=this.findEntityByHql(hql, email,password);
			return ValidateUtil.isValid(list)?list.get(0):null;
		}
	
}
