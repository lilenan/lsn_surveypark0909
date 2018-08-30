package com.zj.surveypark.struts2.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zj.surveypark.domain.Page;
import com.zj.surveypark.domain.Survey;
import com.zj.surveypark.domain.User;
import com.zj.surveypark.service.SurveyService;

@Controller
@Scope("prototype")
public class MoveOrCopyPageAction extends BaseAction<Page> implements UserAware {

	private static final long serialVersionUID = -6623431221005379464L;
	private Integer srcPid;
	private List<Survey> surveys;
	private Integer targPid;
	
	private int pos;//位置：0-之前；1-之后
	private Integer sid;
	@Resource
	private SurveyService surveyService;
	
	//接受user
	private User user;
	public Integer getSrcPid() {
		return srcPid;
	}

	public void setSrcPid(Integer srcPid) {
		this.srcPid = srcPid;
	}

	public Integer getTargPid() {
		return targPid;
	}

	public void setTargPid(Integer targPid) {
		this.targPid = targPid;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public List<Survey> getSurveys() {
		return surveys;
	}

	public void setSurveys(List<Survey> surveys) {
		this.surveys = surveys;
	}

	/**
	 * 进入选择目标页
	 */
	public String toSelectTargetPage(){
		this.surveys=surveyService.findSurveysWithPage(user);
		return "moveOrCopyPageList";
	}
	
	/**
	 * 进行移动和复制操作
	 */
	public String doMoveOrCopyPage(){
		surveyService.moveOrCopyPage(srcPid,targPid,pos);
		return "designSurveyAction";
	}

	/**
	 * 注入user
	 */
	public void setUser(User user) {
		this.user=user;
	}
}
