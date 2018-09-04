package com.zj.surveypark.service.impl;



import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zj.surveypark.dao.BaseDao;
import com.zj.surveypark.domain.security.Right;
import com.zj.surveypark.service.RightService;
import com.zj.surveypark.util.StringUtil;
import com.zj.surveypark.util.ValidateUtil;


/**
 * rightService
 */
@Service("rightService")
public class RightServiceImpl extends BaseServiceImpl<Right> implements
		RightService{

	/**
	 * 重写该方法,覆盖注解
	 */
	@Resource(name="rightDao")
	public void setDao(BaseDao<Right> dao) {
		super.setDao(dao);
	}

	/**
	 * 保存/更新权限
	 */
	public void saveOrUpdateRight(Right model) {
		//insert
		if(model.getId()==null){
			int rightPos=0;
			long rightCode=1;
			//查询最大权限位上的最大权限码
			//String hql="from Right r order by r.rightPos desc,r.rightCode desc";
			String hql="select max(r.rightPos),max(r.rightCode) from Right r "
					+ "where r.rightPos=(select max(rr.rightPos) from Right rr)";
			//List<Right> list=this.findEntityByHql(hql);
			Object[] arr=(Object[]) this.uniqueResult(hql);
			Integer topRightPos=(Integer) arr[0];
			Long topRightCode=(Long) arr[1];
			/*空表
			if(!ValidateUtil.isValid(list)){
				rightPos=0;
				rightCode=1;
			}else{
				Right topRight=list.get(0);
				int topRightPos=topRight.getRightPos();
				long topRightCode=topRight.getRightCode();
				//权限码是否到达最大值
				if(topRightCode>=(1L<<60)){
					rightPos=topRightPos+1;
					rightCode=1;
				}else{
					rightPos=topRightPos;
					rightCode=topRightCode<<1;
				}
			}*/
			if(topRightPos==null){
				rightPos=0;
				rightCode=1;
			}else{
				if(topRightCode>=(1L<<60)){
					rightPos=topRightPos+1;
					rightCode=1;
				}else{
					rightPos=topRightPos;
					rightCode=topRightCode<<1;
				}
			}
			model.setRightPos(rightPos);
			model.setRightCode(rightCode);
		}
		this.saveOrUpdateEntity(model);
	}

	public void appendRightByUrl(String url) {
		String hql="select count(*) from Right r where r.rightUrl=?";
		Long count=(Long) this.uniqueResult(hql, url);
		if(count==0){
			Right r=new Right();
			r.setRightUrl(url);
			this.saveOrUpdateRight(r);
		}
	}

	public void batchUpdateRights(List<Right> allRights) {
		if(ValidateUtil.isValid(allRights)){
			String hql="update Right r set r.rightName=? where r.id=?";
			for(Right r:allRights){
				this.batchEntityByHql(hql, r.getRightName(), r.getId());
			}
		}
	}

	public List<Right> findRightsInRange(Integer[] ownRightIds) {
		if(ValidateUtil.isValid(ownRightIds)){
			String hql="from Right r where r.id in ("+StringUtil.arr2Str(ownRightIds)+")";
			return this.findEntityByHql(hql);
		}
		return null;
	}

	public List<Right> findRightsNotInRange(Set<Right> rights) {
		if(!ValidateUtil.isValid(rights)){
			return this.findAllEntities();
		}else{
			String hql="from Right r where r.id not in ("+extractRightIds(rights)+")";
			return this.findEntityByHql(hql);
		}
		
	}

	/**
	 * 抽取权限id，形成字符串，“,”号分隔
	 */
	private String extractRightIds(Set<Right> rights) {
		if(!ValidateUtil.isValid(rights)){
			return null;
		}else{
			String temp="";
			for(Right r:rights){
				temp=temp+r.getId()+",";
			}
			return temp.substring(0,temp.length()-1);
		}
	}
	
}
