package com.zj.surveypark.struts2.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zj.surveypark.domain.Page;
import com.zj.surveypark.domain.Survey;
import com.zj.surveypark.service.SurveyService;

@Controller
@Scope("prototype")
public class PageAction extends BaseAction<Page> {

	private static final long serialVersionUID = -6623431221005379464L;
	private Integer sid;
	private Integer pid;
	@Resource
	private SurveyService surveyService;
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	/**
	 * 跳转到增加页页面
	 */
	public String toAddPage(){
		return "addPage";
	}
	
	/**
	 * 保存/更新页面
	 */
	public String saveOrUpdatePage(){
		//设置model和调查关联关系
		Survey s=new Survey();
		s.setId(sid);
		model.setSurvey(s);
		surveyService.saveOrUpdatePage(model);
		return "designSurveyAction";
	}
	
	/**
	 * 编辑页标题
	 */
	public String editPage(){
		this.model=surveyService.getPage(pid);
		return "editPage";
	}
	
	/**
	 * 删除页面
	 */
	public String deletePage(){
		surveyService.deletePage(pid);
		return "designSurveyAction";
	}
}
