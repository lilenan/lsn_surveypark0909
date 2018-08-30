package com.zj.surveypark.service;

import java.util.List;

import com.zj.surveypark.domain.Answer;
import com.zj.surveypark.domain.Page;
import com.zj.surveypark.domain.Question;
import com.zj.surveypark.domain.Survey;
import com.zj.surveypark.domain.User;

public interface SurveyService {

	//新建调查
	public Survey newSurvey(User u);
	
	//查询指定用户的调查集合
	public List<Survey> findMySurveys(User u);
	
	//按照id查询调查,携带所有孩子
	public Survey getSurvey(Integer sid);
		
	//按照id查询调查,携带所有孩子
	public Survey getSurveyWithChildren(Integer sid);

	public void updateSurvey(Survey model);

	//保存/更新页面
	public void saveOrUpdatePage(Page model);

	//编辑页标题
	public Page getPage(Integer pid);

	//保存/更新问题
	public void saveOrUpdateQuestion(Question model);

	//获取问题
	public Question getQuestion(Integer qid);

	//删除问题
	public void deleteQuestion(Integer qid);

	//删除页面
	public void deletePage(Integer pid);

	//删除调查
	public void deleteSurvey(Integer sid);

	//清楚调查
	public void clearAnswer(Integer sid);

	//切换状态
	public void toggleStatus(Integer sid);

	//更新数据库路径信息
	public void updateLogoPhotoPath(Integer sid, String string);

	//查询调查，携带page集合
	public List<Survey> findSurveysWithPage(User user);

	//进行移动和复制操作
	public void moveOrCopyPage(Integer srcPid, Integer targPid, int pos);

	//查询所有可以参与的调查
	public List<Survey> findAllAvailableSurveys();

	//查询调查首页
	public Page getFirstPage(Integer sid);

	//查询指定页面的上一页
	public Page getPrePage(Integer currPid);

	//查询指定页面的下一页
	public Page getNextPage(Integer currPid);

	//保存答案
	public void saveAnswers(List<Answer> processAnswers);

	//根据sid查询问题集合
	public List<Question> getQuestions(Integer sid);

	//查询答案
	public List<Answer> findAnswers(Integer sid);

}
