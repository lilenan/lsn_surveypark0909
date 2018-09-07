package com.zj.surveypark.struts2.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zj.surveypark.datasource.SurveyToken;
import com.zj.surveypark.domain.Answer;
import com.zj.surveypark.domain.Page;
import com.zj.surveypark.domain.Survey;
import com.zj.surveypark.service.SurveyService;
import com.zj.surveypark.util.StringUtil;
import com.zj.surveypark.util.ValidateUtil;

@Controller
@Scope("prototype")
public class EngageSurveyAction extends BaseAction<Survey> implements ServletContextAware,SessionAware,ParameterAware {
	private static final long serialVersionUID = -3083620443564498000L;
	private static final String CURRENT_SURVEY="current_survey";
	private static final String ALL_PARAMS_MAP="all_params_map";
	
	@Resource
	private SurveyService surveyService;
	private List<Survey> surveys;
	private Integer sid;
	private Page currPage;
	private Integer currPid;
	//接受sc
	private ServletContext sc;
	//接受session的map
	private Map<String, Object> sessionMap;
	//接受提交的参数
	private Map<String, String[]> paramsMap;
	public List<Survey> getSurveys() {
		return surveys;
	}
	public void setSurveys(List<Survey> surveys) {
		this.surveys = surveys;
	}
	
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public Page getCurrPage() {
		return currPage;
	}
	public void setCurrPage(Page currPage) {
		this.currPage = currPage;
	}
	public Integer getCurrPid() {
		return currPid;
	}
	public void setCurrPid(Integer currPid) {
		this.currPid = currPid;
	}
	/**
	 * 查询所有可以参与的调查
	 */
	public String findAllAvailableSurveys(){
		this.surveys=surveyService.findAllAvailableSurveys();
		return "engageSurveyListPage";
	}
	
	/**
	 * 取得图片的url地址
	 */
	public String getImageUrl(String logoPhotoPath){
		if(ValidateUtil.isValid(logoPhotoPath)){
			String path=sc.getRealPath(logoPhotoPath);
			if(new File(path).exists()){
				return sc.getContextPath()+logoPhotoPath;
			}
		}
		return sc.getContextPath()+"/question.bmp";
	}
	
	/**
	 * 参与调查入口方法
	 */
	public String entry(){
		//查询调查的首页
		this.currPage=surveyService.getFirstPage(sid);
		//将survey保存到session中
		//同时将survey的minOrderno和maxOrderno求出，作为属性存放到survey中
		sessionMap.put(CURRENT_SURVEY, currPage.getSurvey());
		//初始化所有参数的map
		sessionMap.put(ALL_PARAMS_MAP, new HashMap<Integer,Map<String, String[]>>());
		return "engageSurveyPage";
	}
	
	/**
	 * 处理参与调查
	 */
	public String doEngageSurvey(){
		String submitName=getSubmitName();
		if(submitName.endsWith("pre")){
			//合并参数到session中
			mergeParamsIntoSession();
			this.currPage=surveyService.getPrePage(currPid);
			return "engageSurveyPage";
		}else if(submitName.endsWith("next")){
			//合并参数到session中
			mergeParamsIntoSession();
			this.currPage=surveyService.getNextPage(currPid);
			return "engageSurveyPage";
		}else if(submitName.endsWith("exit")){
			//清除session的数据
			clearSessionData();
			return "engageSurveyAction";
		}else if(submitName.endsWith("done")){
			//合并参数到session中
			mergeParamsIntoSession();
			//绑定令牌到当前线程
			SurveyToken token=new SurveyToken();
			token.setCurrentSurvey(getCurrentSurvey());
			SurveyToken.bindingToken(token);
			
			//答案入库
			surveyService.saveAnswers(processAnswers());
			//清除数据
			clearSessionData();
			return "engageSurveyAction";
		}
		return null;
	}

	/**
	 * 处理答案
	 */
	private List<Answer> processAnswers() {
		Map<Integer, String> matrixRadioMap=new HashMap<>();
		List<Answer> answer=new ArrayList<>();
		Answer a=null;
		String key=null;
		String[] value=null;
		for(Map<String, String[]> map:getAllParamsMapInSession().values()){
			for(Entry<String, String[]> entry:map.entrySet()){
				key=entry.getKey();
				value=entry.getValue();
				//挑选所有q开头的参数
				if(key.startsWith("q")){
					//不含other和"_"
					if(!key.contains("other") && !key.contains("_")){
						a=new Answer();
						a.setAnswerIds(StringUtil.arr2Str(value));//answerIds
						a.setQuestionId(getQid(key));//questionId
						a.setSurveyId(getCurrentSurvey().getId());//surveyId
						//处理其他项
						String[] otherValue=map.get(key+"other");
						a.setOtherAnswer(StringUtil.arr2Str(otherValue));//otherAnswer
						answer.add(a);
					}else if(key.contains("_")){//处理矩阵式单选按钮
						//问题
						Integer qid=getMatrixRadioQid(key);
						String oldValue=matrixRadioMap.get(qid);
						if(oldValue==null){
							matrixRadioMap.put(qid, StringUtil.arr2Str(value));
						}else{
							matrixRadioMap.put(qid, oldValue+","+StringUtil.arr2Str(value));
						}
					}
				}
			}
		}
		//单独处理矩阵式单选按钮
		processMatrixRadionAnswers(answer,matrixRadioMap);
		return answer;
	}

	/**
	 * 单独处理矩阵式单选按钮
	 */
	private void processMatrixRadionAnswers(List<Answer> answers, Map<Integer, String> matrixRadioMap) {
		Integer key=null;
		String value=null;
		Answer a=null;
		for(Entry<Integer, String> entry:matrixRadioMap.entrySet()){
			key=entry.getKey();
			value=entry.getValue();
			a=new Answer();
			a.setAnswerIds(value);
			a.setQuestionId(key);
			a.setSurveyId(getCurrentSurvey().getId());
			answers.add(a);
		}
	}
	
	/**
	 * 得到矩阵式单选按钮问题id：q12_0-->12
	 */
	private Integer getMatrixRadioQid(String key) {
		return Integer.parseInt(key.substring(1, key.indexOf("_")));
	}
	/**
	 * 取得调查
	 */
	private Survey getCurrentSurvey() {
		return (Survey) sessionMap.get(CURRENT_SURVEY);
	}
	/**
	 * 提取问题id：q12-->12
	 */
	private Integer getQid(String key) {
		return Integer.parseInt(key.substring(1));
	}
	/**
	 * 清除session数据，释放资源
	 */
	private void clearSessionData() {
		sessionMap.remove(CURRENT_SURVEY);
		sessionMap.remove(ALL_PARAMS_MAP);
	}
	/**
	 * 将参数合并到session中
	 */
	private void mergeParamsIntoSession() {
		Map<Integer,Map<String, String[]>> allParamsMap=getAllParamsMapInSession();
		allParamsMap.put(currPid, paramsMap);
	}
	
	/**
	 * 获得session中所有参数的map
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, Map<String, String[]>> getAllParamsMapInSession() {
		return  (Map<Integer, Map<String, String[]>>) sessionMap.get(ALL_PARAMS_MAP);
	}
	/**
	 * 得到提交按钮的名称
	 */
	private String getSubmitName() {
		for(String name:paramsMap.keySet()){
			if(name.startsWith("submit_")){
				return name;
			}
		}
		return null;
	}
	//注入sc
	public void setServletContext(ServletContext arg0) {
		this.sc=arg0;
	}

	//注入session map
	public void setSession(Map<String, Object> arg0) {
		this.sessionMap=arg0;
	}

	//注入所有参数
	public void setParameters(Map<String, String[]> arg0) {
		this.paramsMap=arg0;
	}
	
	/**
	 * 设置选中标记
	 */
	public String setTag(String name,String value,String tag){
		Integer pid=this.currPage.getId();
		Map<String, String[]> map=this.getAllParamsMapInSession().get(pid);
		String[] oldValues=map.get(name);
		if(StringUtil.contains(oldValues,value)){
			return tag;
		}
		return "";
	}
	
	public String setText(String name){
		Integer pid=this.currPage.getId();
		Map<String, String[]> map=this.getAllParamsMapInSession().get(pid);
		String[] oldValues=map.get(name);
		if(ValidateUtil.isValid(oldValues)){
			return "value='"+oldValues[0]+"'";
		}
		return "";
	}
}
