package com.zj.surveypark.struts2.action;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.interceptor.SessionAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zj.surveypark.domain.User;
import com.zj.surveypark.service.RightService;
import com.zj.surveypark.service.UserService;
import com.zj.surveypark.util.DataUtil;

/**
 * LoginAction
 */
@Controller
@Scope("prototype")
public class LoginAction extends BaseAction<User> implements SessionAware  {
	
	private static final long serialVersionUID = -6969778525713213592L;//���ڷ����л�
	
	@Resource
	private UserService userService;
	@Resource
	private RightService rightService;
	//����session��map
	private Map<String, Object> sessionMap;
	/**
	 * �����¼ҳ��
	 */
	public String toLoginPage(){
		return "loginPage";
	}
	
	public String doLogin(){
		return "success";
	}
	
	/**
	 * �÷���ֻ��doLogin֮ǰ����
	 */
	public void validateDoLogin(){
		User user=userService.validataLoginInfo(model.getEmail(),DataUtil.md5(model.getPassword()));
		if(user==null){
			addActionError("email/password wrong");
		}else{
			//��ʼ��Ȩ���ܺ�����
			int maxRightPos=rightService.getMaxRightPos();
			user.setRightSum(new long[maxRightPos+1]);
			//�����û���Ȩ�޵��ܺ�
			user.calculateRightSum();
			/*HttpSession s= ServletActionContext.getRequest().getSession();
			s.setAttribute(arg0, arg1);//ԭ��API�����������*/
			//user-->session
			sessionMap.put("user", user);
		}
	}

	//ע��Session��map
	public void setSession(Map<String, Object> arg0) {
		this.sessionMap=arg0;
	}
}
