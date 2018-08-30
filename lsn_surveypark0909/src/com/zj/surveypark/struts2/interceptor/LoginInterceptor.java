package com.zj.surveypark.struts2.interceptor;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.zj.surveypark.domain.User;
import com.zj.surveypark.struts2.action.BaseAction;
import com.zj.surveypark.struts2.action.LoginAction;
import com.zj.surveypark.struts2.action.RegAction;
import com.zj.surveypark.struts2.action.UserAware;

public class LoginInterceptor implements Interceptor {

	private static final long serialVersionUID = -91442499779365811L;

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

	@SuppressWarnings("rawtypes")
	public String intercept(ActionInvocation invocation) throws Exception {
		BaseAction action=(BaseAction) invocation.getAction();
		if(action instanceof LoginAction||action instanceof RegAction){
			return invocation.invoke();
		}else{
			HttpSession s=ServletActionContext.getRequest().getSession();
			User user=(User) s.getAttribute("user");
			if(user==null){
				return "login";//对应全局结果中的result
			}else{
				//处理action的user注入问题
				if(action instanceof UserAware){
					((UserAware)action).setUser(user);
				}
				return invocation.invoke();
			}
		}
	}

}
