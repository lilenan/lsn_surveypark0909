package com.zj.surveypark.struts2.action;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.struts2.util.ServletContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zj.surveypark.domain.Survey;
import com.zj.surveypark.domain.User;
import com.zj.surveypark.service.SurveyService;
import com.zj.surveypark.util.ValidateUtil;

@Controller
@Scope("prototype")
public class SurveyAction extends BaseAction<Survey> implements UserAware,ServletContextAware {

	private static final long serialVersionUID = -6443956850577800169L;
	/*//����session��map
	private Map<String, Object> sessionMap;*/
	@Resource
	private SurveyService surveyService;
	private List<Survey> mySurveys;
	
	private Integer sid;
	
	private String inputPage;
	
	public String getInputPage() {
		return inputPage;
	}

	public void setInputPage(String inputPage) {
		this.inputPage = inputPage;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	private User user;//����session��user����

	public List<Survey> getMySurveys() {
		return mySurveys;
	}

	public void setMySurveys(List<Survey> mySurveys) {
		this.mySurveys = mySurveys;
	}

	/**
	 * �½�����
	 */
	public String newSurvey(){
		//User u=(User) sessionMap.get("user");
		this.model=surveyService.newSurvey(user);
		return "designSurveyPage";
	}
	
	/**
	 * ��ѯ����
	 */
	public String mySurveys(){
		//User u=(User) sessionMap.get("user");
		this.mySurveys=surveyService.findMySurveys(user);
		return "mySurveyListPage";
	}
	
	/**
	 * ���ҵĵ����б������Ƶ���
	 */
	public String designSurvey(){
		this.model = surveyService.getSurveyWithChildren(sid);
		return "designSurveyPage" ;
	}
	
	/**
	 * �༭����
	 */
	public String editSurvey(){
		this.model=surveyService.getSurvey(sid);
		return "editSurveyPage";
	}
	
	/**
	 * �༭����--�ύ�����µ���
	 */
	public String updateSurvey(){
		this.sid=model.getId();
		model.setUser(user);
		surveyService.updateSurvey(model);
		return "designSurveyAction";
	}
	
	/**
	 * ɾ������
	 */
	public String deleteSurvey(){
		surveyService.deleteSurvey(sid);
		return "findMySurveysAction";
	}
	
	/**
	 * �������
	 */
	public String clearAnswer(){
		surveyService.clearAnswer(sid);
		return "findMySurveysAction";
	}
	
	/**
	 * �л�״̬
	 */
	public String toggleStatus(){
		surveyService.toggleStatus(sid);
		return "findMySurveysAction";
	}
	
	/**
	 * ��ת����logoҳ��
	 */
	public String toAddLogoPage(){
		return "addLogoPage";
	}
	//�ļ��ϴ�����
	private File logoPhoto;
	private String logoPhotoFileName;
	//����ServletContext����
	private ServletContext sc;
	public File getLogoPhoto() {
		return logoPhoto;
	}

	public void setLogoPhoto(File logoPhoto) {
		this.logoPhoto = logoPhoto;
	}

	public String getLogoPhotoFileName() {
		return logoPhotoFileName;
	}

	public void setLogoPhotoFileName(String logoPhotoFileName) {
		this.logoPhotoFileName = logoPhotoFileName;
	}

	/**
	 * ����ϴ�
	 */ 
	public String doAddLogo(){
		if(ValidateUtil.isValid(logoPhotoFileName)){
			//�ϴ��ļ�
			String dir=sc.getRealPath("/upload");
			long l=System.nanoTime();
			String ext=logoPhotoFileName.substring(logoPhotoFileName.lastIndexOf("."));
			File newFile=new File(dir,l+ext);
			logoPhoto.renameTo(newFile);
			//�������ݿ�·����Ϣ
			surveyService.updateLogoPhotoPath(sid,"/upload/"+l+ext);
		}
		return "designSurveyAction";
	}
	
	//�÷������ļ��ϴ�������֮ǰִ�У�ִ��˳��prepareDoAddLogo()--������--doAddLogo()
	public void prepareDoAddLogo(){
		inputPage="/addLogo.jsp";
	}
	
	/**
	 * �÷�����designSurvey֮ǰ����getModel֮ǰִ��
	 */
	/*public void prepareDesignSurvey(){
		this.model=surveyService.getSurveyWithChildren(sid);
	}*/
	/*//ע��session map
	public void setSession(Map<String, Object> session) {
		this.sessionMap=session;
	}*/

	//ע��user����
	public void setUser(User user) {
		// TODO Auto-generated method stub
		this.user=user;
	}

	//ע��setServletContext����
	public void setServletContext(ServletContext context) {
		this.sc=context;
	}
	
	/**
	 * �ж�ͼƬ�Ƿ����
	 */
	public boolean logoPhotoExists(){
		String path=model.getLogoPhotoPath();
		if(ValidateUtil.isValid(path)){
			String realPath=sc.getRealPath(path);
			return new File(realPath).exists();
		}
		return false;
	}
	
	/**
	 * ��������
	 */
	public String analyzeSurvey(){
		this.model=surveyService.getSurveyWithChildren(sid);
		return "analyzeSurveyListPage";
	}
}
