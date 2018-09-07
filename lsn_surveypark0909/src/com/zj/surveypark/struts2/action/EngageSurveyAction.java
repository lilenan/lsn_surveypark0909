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
	//����sc
	private ServletContext sc;
	//����session��map
	private Map<String, Object> sessionMap;
	//�����ύ�Ĳ���
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
	 * ��ѯ���п��Բ���ĵ���
	 */
	public String findAllAvailableSurveys(){
		this.surveys=surveyService.findAllAvailableSurveys();
		return "engageSurveyListPage";
	}
	
	/**
	 * ȡ��ͼƬ��url��ַ
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
	 * ���������ڷ���
	 */
	public String entry(){
		//��ѯ�������ҳ
		this.currPage=surveyService.getFirstPage(sid);
		//��survey���浽session��
		//ͬʱ��survey��minOrderno��maxOrderno�������Ϊ���Դ�ŵ�survey��
		sessionMap.put(CURRENT_SURVEY, currPage.getSurvey());
		//��ʼ�����в�����map
		sessionMap.put(ALL_PARAMS_MAP, new HashMap<Integer,Map<String, String[]>>());
		return "engageSurveyPage";
	}
	
	/**
	 * ����������
	 */
	public String doEngageSurvey(){
		String submitName=getSubmitName();
		if(submitName.endsWith("pre")){
			//�ϲ�������session��
			mergeParamsIntoSession();
			this.currPage=surveyService.getPrePage(currPid);
			return "engageSurveyPage";
		}else if(submitName.endsWith("next")){
			//�ϲ�������session��
			mergeParamsIntoSession();
			this.currPage=surveyService.getNextPage(currPid);
			return "engageSurveyPage";
		}else if(submitName.endsWith("exit")){
			//���session������
			clearSessionData();
			return "engageSurveyAction";
		}else if(submitName.endsWith("done")){
			//�ϲ�������session��
			mergeParamsIntoSession();
			//�����Ƶ���ǰ�߳�
			SurveyToken token=new SurveyToken();
			token.setCurrentSurvey(getCurrentSurvey());
			SurveyToken.bindingToken(token);
			
			//�����
			surveyService.saveAnswers(processAnswers());
			//�������
			clearSessionData();
			return "engageSurveyAction";
		}
		return null;
	}

	/**
	 * �����
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
				//��ѡ����q��ͷ�Ĳ���
				if(key.startsWith("q")){
					//����other��"_"
					if(!key.contains("other") && !key.contains("_")){
						a=new Answer();
						a.setAnswerIds(StringUtil.arr2Str(value));//answerIds
						a.setQuestionId(getQid(key));//questionId
						a.setSurveyId(getCurrentSurvey().getId());//surveyId
						//����������
						String[] otherValue=map.get(key+"other");
						a.setOtherAnswer(StringUtil.arr2Str(otherValue));//otherAnswer
						answer.add(a);
					}else if(key.contains("_")){//�������ʽ��ѡ��ť
						//����
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
		//�����������ʽ��ѡ��ť
		processMatrixRadionAnswers(answer,matrixRadioMap);
		return answer;
	}

	/**
	 * �����������ʽ��ѡ��ť
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
	 * �õ�����ʽ��ѡ��ť����id��q12_0-->12
	 */
	private Integer getMatrixRadioQid(String key) {
		return Integer.parseInt(key.substring(1, key.indexOf("_")));
	}
	/**
	 * ȡ�õ���
	 */
	private Survey getCurrentSurvey() {
		return (Survey) sessionMap.get(CURRENT_SURVEY);
	}
	/**
	 * ��ȡ����id��q12-->12
	 */
	private Integer getQid(String key) {
		return Integer.parseInt(key.substring(1));
	}
	/**
	 * ���session���ݣ��ͷ���Դ
	 */
	private void clearSessionData() {
		sessionMap.remove(CURRENT_SURVEY);
		sessionMap.remove(ALL_PARAMS_MAP);
	}
	/**
	 * �������ϲ���session��
	 */
	private void mergeParamsIntoSession() {
		Map<Integer,Map<String, String[]>> allParamsMap=getAllParamsMapInSession();
		allParamsMap.put(currPid, paramsMap);
	}
	
	/**
	 * ���session�����в�����map
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, Map<String, String[]>> getAllParamsMapInSession() {
		return  (Map<Integer, Map<String, String[]>>) sessionMap.get(ALL_PARAMS_MAP);
	}
	/**
	 * �õ��ύ��ť������
	 */
	private String getSubmitName() {
		for(String name:paramsMap.keySet()){
			if(name.startsWith("submit_")){
				return name;
			}
		}
		return null;
	}
	//ע��sc
	public void setServletContext(ServletContext arg0) {
		this.sc=arg0;
	}

	//ע��session map
	public void setSession(Map<String, Object> arg0) {
		this.sessionMap=arg0;
	}

	//ע�����в���
	public void setParameters(Map<String, String[]> arg0) {
		this.paramsMap=arg0;
	}
	
	/**
	 * ����ѡ�б��
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
