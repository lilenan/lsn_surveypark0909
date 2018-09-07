package com.zj.surveypark.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zj.surveypark.dao.BaseDao;
import com.zj.surveypark.domain.security.Right;
import com.zj.surveypark.domain.security.Role;
import com.zj.surveypark.service.RightService;
import com.zj.surveypark.service.RoleService;
import com.zj.surveypark.util.DataUtil;
import com.zj.surveypark.util.ValidateUtil;

/**
 * roleService
 */
@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role> implements
		RoleService{
	
	@Resource
	private RightService rightService;

	/**
	 * 重写该方法,覆盖注解
	 */
	@Resource(name="roleDao")
	public void setDao(BaseDao<Role> dao) {
		super.setDao(dao);
	}

	public void saveOrUpdateRole(Role model, Integer[] ownRightIds) {
		//ownRightIds无效，说明没有给角色分配权限
		if(!ValidateUtil.isValid(ownRightIds)){
			model.getRights().clear();
		}else{
			List<Right> rights=rightService.findRightsInRange(ownRightIds);
			model.setRights(new HashSet<Right>(rights));
		}
		this.saveOrUpdateEntity(model);
	}

	public List<Role> findRolesNotInRange(Set<Role> roles) {
		if(!ValidateUtil.isValid(roles)){
			return this.findAllEntities();
		}else{
			String hql="from Role r where r.id not in ("+DataUtil.extractEntityIds(roles)+")";
			return this.findEntityByHql(hql);
		}
	}
	
}
