package com.zj.surveypark.struts2.interceptor;



import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.zj.surveypark.struts2.action.BaseAction;
import com.zj.surveypark.util.ValidateUtil;

/**
 * È¨ÏÞ¹ýÂËÀ¹½ØÆ÷
 */
public class RightFilterInterceptor implements Interceptor {

	private static final long serialVersionUID = -91442499779365811L;

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		BaseAction action=(BaseAction) invocation.getAction();
		ActionProxy proxy=invocation.getProxy();
		String ns=proxy.getNamespace();
		String actionName=proxy.getActionName();
		if(ValidateUtil.hasRight(ns, actionName, ServletActionContext.getRequest(), action)){
			return invocation.invoke();
		}else{
			return "no_right_error";
		}
	}

}
