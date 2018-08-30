package com.zj.surveypark.struts2.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zj.surveypark.domain.Page;
import com.zj.surveypark.domain.Question;
import com.zj.surveypark.service.SurveyService;

@Controller
@Scope("prototype")
public class QuestionAction extends BaseAction<Question> {

	private static final long serialVersionUID = 8351894712123015102L;
	private Integer sid;
	private Integer pid;
	private Integer qid;
	
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
	
	public Integer getQid() {
		return qid;
	}
	public void setQid(Integer qid) {
		this.qid = qid;
	}
	/**
	 * 调转到选题型页面
	 */
	public String toSelectQuestionType(){
		return "selectQuestionTypePage";
	}
	
	/**
	 * 调转到相应题型的页面
	 */
	public String toDesignQuestionPage(){
		return ""+model.getQuestionType();
	}
	
	/**
	 * 保存/更新问题
	 */
	public String saveOrUpdateQuestion(){
		//维持关联关系
		Page p=new Page();
		p.setId(pid);
		model.setPage(p);
		surveyService.saveOrUpdateQuestion(model);
		return "designSurveyAction";
	}
	
	/**
	 * 编辑问题
	 */
	public String editQuestion(){
		this.model=surveyService.getQuestion(qid);
		return ""+model.getQuestionType();
	}
	
	/**
	 * 删除问题
	 */
	public String deleteQuestion(){
		surveyService.deleteQuestion(qid);
		return "designSurveyAction";
	}
}
