package com.zj.surveypark.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zj.surveypark.dao.BaseDao;
import com.zj.surveypark.domain.Answer;
import com.zj.surveypark.domain.Page;
import com.zj.surveypark.domain.Question;
import com.zj.surveypark.domain.Survey;
import com.zj.surveypark.domain.User;
import com.zj.surveypark.service.SurveyService;
import com.zj.surveypark.util.DataUtil;
import com.zj.surveypark.util.ValidateUtil;

@Service("surveyService")
public class SurveyServiceImpl implements SurveyService {

	// 注入surveyDao
	@Resource(name = "surveyDao")
	private BaseDao<Survey> surveyDao;

	// 注入pageDao
	@Resource(name = "pageDao")
	private BaseDao<Page> pageDao;

	// 注入questionDao
	@Resource(name = "questionDao")
	private BaseDao<Question> questionDao;

	// 注入answerDao
	@Resource(name = "answerDao")
	private BaseDao<Answer> answerDao;

	public Survey newSurvey(User u) {
		Survey s = new Survey();
		Page p = new Page();
		// 设置调查和用户的关系
		s.setUser(u);
		p.setSurvey(s);
		s.getPages().add(p);
		// 保存
		surveyDao.saveEntity(s);
		pageDao.saveEntity(p);
		return s;
	}

	@Override
	public List<Survey> findMySurveys(User u) {
		String hql = "from Survey s where s.user.id=?";
		return surveyDao.findEntityByHql(hql, u.getId());
	}

	public Survey getSurvey(Integer sid) {
		return surveyDao.getEntity(sid);
	}

	@Override
	public Survey getSurveyWithChildren(Integer sid) {
		// Survey s=surveyDao.getEntity(sid);
		Survey s = this.getSurvey(sid);
		// 初始化问题集合和页面集合
		for (Page p : s.getPages()) {
			p.getQuestions().size();
		}
		return s;
	}

	@Override
	public void updateSurvey(Survey model) {
		surveyDao.updateEntity(model);
	}

	@Override
	public void saveOrUpdatePage(Page model) {
		pageDao.saveOrUpdateEntity(model);
	}

	@Override
	public Page getPage(Integer pid) {
		return pageDao.getEntity(pid);
	}

	@Override
	public void saveOrUpdateQuestion(Question model) {
		questionDao.saveOrUpdateEntity(model);
	}

	@Override
	public Question getQuestion(Integer qid) {
		return questionDao.getEntity(qid);
	}

	@Override
	public void deleteQuestion(Integer qid) {
		//answers
		String hql="delete from Answer a where a.questionId=?";
		answerDao.batchEntityByHql(hql, qid);
		//questions
		hql="delete from Question q where q.id=?";
		questionDao.batchEntityByHql(hql, qid);
	}

	@Override
	public void deletePage(Integer pid) {
		//answers
		String hql="delete Answer a where a.questionId in (select q.id from Question q where q.page.id = ?)";
		answerDao.batchEntityByHql(hql, pid);
		//questions
		hql="delete from Question q where q.page.id=?";
		questionDao.batchEntityByHql(hql, pid);
		//page
		hql="delete from Page p where p.id=?";
		pageDao.batchEntityByHql(hql, pid);
	}

	@Override
	public void deleteSurvey(Integer sid) {
		//answer
		String hql="delete from Answer a where a.surveyId=?";
		answerDao.batchEntityByHql(hql, sid);
		//questions
		//Hibernate不允许两级以上的删除
		hql="delete from Question q where q.page.id in (select p.id from Page p where p.survey.id=?)";
		questionDao.batchEntityByHql(hql, sid);
		//pages
		hql="delete from Page p where p.survey.id=?";
		pageDao.batchEntityByHql(hql, sid);
		//suyvey
		hql="delete from Survey s where s.id=?";
		surveyDao.batchEntityByHql(hql, sid);
	}

	@Override
	public void clearAnswer(Integer sid) {
		String hql="delete from Answer a where a.surveyId=?";
		answerDao.batchEntityByHql(hql, sid);
	}

	@Override
	public void toggleStatus(Integer sid) {
		Survey s=this.getSurvey(sid);
		String hql="update Survey s set s.closed=? where s.id=?";
		surveyDao.batchEntityByHql(hql,!s.isClosed(), sid);
	}

	@Override
	public void updateLogoPhotoPath(Integer sid, String path) {
		String hql="update Survey s set s.logoPhotoPath=? where s.id=?";
		surveyDao.batchEntityByHql(hql, path,sid);
	}

	@Override
	public List<Survey> findSurveysWithPage(User user) {
		String hql="from Survey s where s.user.id=?";
		List<Survey> list=surveyDao.findEntityByHql(hql, user.getId());
		//强行初始化页面集合
		for(Survey s:list){
			s.getPages().size();
		}
		return list;
	}

	@Override
	public void moveOrCopyPage(Integer srcPid, Integer targPid, int pos) {
		Page srcPage=this.getPage(srcPid);
		Survey srcSurvey=srcPage.getSurvey();
		Page targPage=this.getPage(targPid);
		Survey targSurvey=targPage.getSurvey();
		//判断移动/复制
		if(srcSurvey.getId().equals(targSurvey.getId())){
			setOrderno(srcPage,targPage,pos);
		}else{
			//对原页面进行深度复制
			srcPage.getQuestions().size();
			Page copy=(Page) DataUtil.deeplyCopy(srcPage);//对srcPage进行深度复制
			//设置新的关联关系
			copy.setSurvey(targSurvey);
			//分别保存新的页面和问题
			pageDao.saveEntity(copy);
			for(Question q:copy.getQuestions()){
				questionDao.saveEntity(q);
			}
			setOrderno(copy,targPage,pos);
		}
	}

	//设置页序
	private void setOrderno(Page srcPage, Page targPage, int pos) {
		//判断位置
		if(pos==0){
			if(isFirstPage(targPage)){
				srcPage.setOrderno(targPage.getOrderno()-0.01f);
			}else{
				Page prePage=getPrePage(targPage);
				srcPage.setOrderno((targPage.getOrderno()+prePage.getOrderno())/2);
			}
		}else{
			if(isLastPage(targPage)){
				srcPage.setOrderno(targPage.getOrderno()-0.01f);
			}else{
				Page prePage=getNextPage(targPage);
				srcPage.setOrderno((targPage.getOrderno()+prePage.getOrderno())/2);
			}
		}
	}

	//获得页面所在调查的下一页
	private Page getNextPage(Page targPage) {
		String hql="from Page p where p.orderno>? and p.survey.id=? order by p.orderno asc";
		return pageDao.findEntityByHql(hql, targPage.getOrderno(),targPage.getSurvey().getId()).get(0);
	}

	//获得页面所在调查的上一页
	private Page getPrePage(Page targPage) {
		String hql="from Page p where p.orderno<? and p.survey.id=? order by p.orderno desc";
		return pageDao.findEntityByHql(hql, targPage.getOrderno(),targPage.getSurvey().getId()).get(0);
	}

	//判断页面是否是所在调查的尾页
	private boolean isLastPage(Page targPage) {
		String hql="from Page p where p.orderno>? and p.survey.id=?";
		List<Page> list=pageDao.findEntityByHql(hql, targPage.getOrderno(),targPage.getSurvey().getId());
		return !ValidateUtil.isValid(list);
	}

	//判断页面是否是所在调查的首页
	private boolean isFirstPage(Page targPage) {
		String hql="from Page p where p.orderno<? and p.survey.id=?";
		List<Page> list=pageDao.findEntityByHql(hql, targPage.getOrderno(),targPage.getSurvey().getId());
		return !ValidateUtil.isValid(list);
	}

	@Override
	public List<Survey> findAllAvailableSurveys() {
		String hql="from Survey s where s.closed=?";
		return surveyDao.findEntityByHql(hql, false);
	}

	@Override
	public Page getFirstPage(Integer sid) {
		String hql="from Page p where p.survey.id=? order by p.orderno asc";
		Page p=pageDao.findEntityByHql(hql, sid).get(0);
		p.getQuestions().size();
		p.getSurvey().getTitle();
		return p;
	}

	@Override
	public Page getPrePage(Integer currPid) {
		Page p=this.getPage(currPid);
		p=this.getPrePage(p);
		p.getQuestions().size();
		return p;
	}

	@Override
	public Page getNextPage(Integer currPid) {
		Page p=this.getPage(currPid);
		p=this.getNextPage(p);
		p.getQuestions().size();
		return p;
	}

	@Override
	public void saveAnswers(List<Answer> processAnswers) {
		String uuid=UUID.randomUUID().toString();
		Date date=new Date();
		for(Answer a:processAnswers){
			a.setUuid(uuid);
			a.setAnswerTime(date);
			answerDao.saveEntity(a);
		}
	}

	@Override
	public List<Question> getQuestions(Integer sid) {
		String hql="from Question q where q.page.survey.id=?";
		return questionDao.findEntityByHql(hql, sid);
	}

	@Override
	public List<Answer> findAnswers(Integer sid) {
		String hql="from Answer a where a.surveyId=? order by a.uuid";
		return answerDao.findEntityByHql(hql, sid);
	}

}
