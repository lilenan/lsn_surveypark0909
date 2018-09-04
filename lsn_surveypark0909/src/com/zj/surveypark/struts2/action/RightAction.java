package com.zj.surveypark.struts2.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zj.surveypark.domain.security.Right;
import com.zj.surveypark.service.RightService;

/**
 * 
 */
@Controller
@Scope("prototype")
public class RightAction extends BaseAction<Right> {
	private static final long serialVersionUID = -7021445905997323598L;
	private List<Right> allRights;
	private Integer rightId;
	
	@Resource
	private RightService rightService;
	public Integer getRightId() {
		return rightId;
	}
	public void setRightId(Integer rightId) {
		this.rightId = rightId;
	}
	public List<Right> getAllRights() {
		return allRights;
	}
	public void setAllRights(List<Right> allRights) {
		this.allRights = allRights;
	}
	
	/**
	 * ��ѯȫ��Ȩ��
	 */
	public String findAllRights(){
		this.allRights=rightService.findAllEntities();
		return "rightListPage";
	}
	
	/**
	 * ���Ȩ�޵�ҳ��
	 */
	public String toAddRightPage(){
		return "addRightPage";
	}
	
	/**
	 * ����/����Ȩ��
	 */
	public String saveOrUpdateRight(){
		rightService.saveOrUpdateRight(model);
		return "findAllRightsAction";
	}
	
	/**
	 * �༭Ȩ��
	 */
	public String editRight(){
		this.model=rightService.getEntity(rightId);
		return "editRightPage";
	}
	
	/**
	 * ɾ��Ȩ��
	 */
	public String deleteRight(){
		Right r=new Right();
		r.setId(rightId);
		rightService.deleteEntity(r);
		return "findAllRightsAction";
	}
	
	/**
	 * ��������Ȩ��
	 */
	public String batchUpdateRights(){
		rightService.batchUpdateRights(allRights); 
		return "findAllRightsAction";
	}
}
