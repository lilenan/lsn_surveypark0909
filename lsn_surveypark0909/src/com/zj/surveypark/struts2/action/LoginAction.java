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
	
	private static final long serialVersionUID = -6969778525713213592L;//用于反序列化
	
	@Resource
	private UserService userService;
	@Resource
	private RightService rightService;
	//接受session的map
	private Map<String, Object> sessionMap;
	/**
	 * 进入登录页面
	 */
	public String toLoginPage(){
		return "loginPage";
	}
	
	public String doLogin(){
		return "success";
	}
	
	/**
	 * 该方法只在doLogin之前运行
	 */
	public void validateDoLogin(){
		User user=userService.validataLoginInfo(model.getEmail(),DataUtil.md5(model.getPassword()));
		if(user==null){
			addActionError("email/password wrong");
		}else{
			//初始化权限总和数组
			int maxRightPos=rightService.getMaxRightPos();
			user.setRightSum(new long[maxRightPos+1]);
			//计算用户的权限的总和
			user.calculateRightSum();
			/*HttpSession s= ServletActionContext.getRequest().getSession();
			s.setAttribute(arg0, arg1);//原生API，会增加耦合*/
			//user-->session
			sessionMap.put("user", user);
		}
	}

	//注入Session的map
	public void setSession(Map<String, Object> arg0) {
		this.sessionMap=arg0;
	}
}
