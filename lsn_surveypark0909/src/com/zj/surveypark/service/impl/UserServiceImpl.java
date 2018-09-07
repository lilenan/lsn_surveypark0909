package com.zj.surveypark.service.impl;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zj.surveypark.dao.BaseDao;
import com.zj.surveypark.domain.User;
import com.zj.surveypark.domain.security.Role;
import com.zj.surveypark.service.RoleService;
import com.zj.surveypark.service.UserService;
import com.zj.surveypark.util.StringUtil;
import com.zj.surveypark.util.ValidateUtil;

/**
 * userService
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	@Resource
	private RoleService roleService;
	/**
	 * 重写该方法,覆盖注解
	 */
	@Resource(name = "userDao")
	public void setDao(BaseDao<User> dao) {
		super.setDao(dao);
	}

	public boolean isRegisted(String email) {
		String hql = "from User u where u.email=?";
		List<User> list = this.findEntityByHql(hql, email);
		return ValidateUtil.isValid(list);
	}

	// 校验登录信息
	public User validataLoginInfo(String email, String password) {
		String hql = "from User u where u.email=? and u.password=?";
		List<User> list = this.findEntityByHql(hql, email, password);
		return ValidateUtil.isValid(list) ? list.get(0) : null;
	}

	public void updateAuthorize(User r, Integer[] ownRoleIds) {
		User uu=this.getEntity(r.getId());
		if(!ValidateUtil.isValid(ownRoleIds)){
			uu.getRoles().clear();
		}else{
			String hql="from Role r where r.id in("+StringUtil.arr2Str(ownRoleIds)+")";
			List<Role> roles=roleService.findEntityByHql(hql);
			uu.setRoles(new HashSet<Role>(roles));
		}
	}

	public void clearAuthorize(Integer userId) {
		this.getEntity(userId).getRoles().clear();
	}

}
