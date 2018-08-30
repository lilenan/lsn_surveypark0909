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
	/*//接受session的map
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

	private User user;//接受session的user对象

	public List<Survey> getMySurveys() {
		return mySurveys;
	}

	public void setMySurveys(List<Survey> mySurveys) {
		this.mySurveys = mySurveys;
	}

	/**
	 * 新建调查
	 */
	public String newSurvey(){
		//User u=(User) sessionMap.get("user");
		this.model=surveyService.newSurvey(user);
		return "designSurveyPage";
	}
	
	/**
	 * 查询调查
	 */
	public String mySurveys(){
		//User u=(User) sessionMap.get("user");
		this.mySurveys=surveyService.findMySurveys(user);
		return "mySurveyListPage";
	}
	
	/**
	 * 从我的调查列表进入设计调查
	 */
	public String designSurvey(){
		this.model = surveyService.getSurveyWithChildren(sid);
		return "designSurveyPage" ;
	}
	
	/**
	 * 编辑调查
	 */
	public String editSurvey(){
		this.model=surveyService.getSurvey(sid);
		return "editSurveyPage";
	}
	
	/**
	 * 编辑调查--提交，更新调查
	 */
	public String updateSurvey(){
		this.sid=model.getId();
		model.setUser(user);
		surveyService.updateSurvey(model);
		return "designSurveyAction";
	}
	
	/**
	 * 删除调查
	 */
	public String deleteSurvey(){
		surveyService.deleteSurvey(sid);
		return "findMySurveysAction";
	}
	
	/**
	 * 清楚调查
	 */
	public String clearAnswer(){
		surveyService.clearAnswer(sid);
		return "findMySurveysAction";
	}
	
	/**
	 * 切换状态
	 */
	public String toggleStatus(){
		surveyService.toggleStatus(sid);
		return "findMySurveysAction";
	}
	
	/**
	 * 跳转增加logo页面
	 */
	public String toAddLogoPage(){
		return "addLogoPage";
	}
	//文件上传部分
	private File logoPhoto;
	private String logoPhotoFileName;
	//接受ServletContext对象
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
	 * 完成上传
	 */ 
	public String doAddLogo(){
		if(ValidateUtil.isValid(logoPhotoFileName)){
			//上传文件
			String dir=sc.getRealPath("/upload");
			long l=System.nanoTime();
			String ext=logoPhotoFileName.substring(logoPhotoFileName.lastIndexOf("."));
			File newFile=new File(dir,l+ext);
			logoPhoto.renameTo(newFile);
			//更新数据库路径信息
			surveyService.updateLogoPhotoPath(sid,"/upload/"+l+ext);
		}
		return "designSurveyAction";
	}
	
	//该方法在文件上传拦截器之前执行，执行顺序：prepareDoAddLogo()--拦截器--doAddLogo()
	public void prepareDoAddLogo(){
		inputPage="/addLogo.jsp";
	}
	
	/**
	 * 该方法在designSurvey之前，及getModel之前执行
	 */
	/*public void prepareDesignSurvey(){
		this.model=surveyService.getSurveyWithChildren(sid);
	}*/
	/*//注入session map
	public void setSession(Map<String, Object> session) {
		this.sessionMap=session;
	}*/

	//注入user对象
	public void setUser(User user) {
		// TODO Auto-generated method stub
		this.user=user;
	}

	//注入setServletContext对象
	public void setServletContext(ServletContext context) {
		this.sc=context;
	}
	
	/**
	 * 判断图片是否存在
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
	 * 分析调查
	 */
	public String analyzeSurvey(){
		this.model=surveyService.getSurveyWithChildren(sid);
		return "analyzeSurveyListPage";
	}
}
