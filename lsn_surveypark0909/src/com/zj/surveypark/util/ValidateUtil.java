package com.zj.surveypark.util;

import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.zj.surveypark.domain.User;
import com.zj.surveypark.domain.security.Right;
import com.zj.surveypark.struts2.action.BaseAction;
import com.zj.surveypark.struts2.action.UserAware;

public class ValidateUtil {
	//判断字符串有效性
	public static boolean isValid(String str){
		if(str==null||"".equals(str.trim())){
			return false;
		}
		return true;
	}
	
	/**
	 * 判断集合有效性
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isValid(Collection col){
		if(col==null||col.isEmpty()){
			return false;
		}
		return true;
	}

	/**
	 * 判断数组有效性
	 */
	public static boolean isValid(Object[] arr) {
		if(arr==null||arr.length==0){
			return false;
		}
		return true;
	}
	
	/**
	 * 判断是否有权限
	 */
	@SuppressWarnings("unchecked")
	public static boolean hasRight(String ns,String actionName,HttpServletRequest request,BaseAction action){
		if(!ValidateUtil.isValid(ns)||"/".equals(ns)){
			ns="";
		}
		
		//处理带?参数的action
		if(actionName!=null&&actionName.contains("?")){
			actionName=actionName.substring(0,actionName.indexOf("?"));
		}
		
		String url=ns+"/"+actionName;
		HttpSession s=request.getSession();
		ServletContext sc=s.getServletContext();
		Map<String, Right> map=(Map<String, Right>) sc.getAttribute("all_rights_map");
		Right r=map.get(url);
		/*if(r==null){
			return false;
		}*/
		//是否为公共资源
		if(r==null||r.isCommon()){
			return true;
		}else{
			User user=(User) s.getAttribute("user");
			//是否登录
			if(user==null){
				return false;
			}else{
				//处理userAware用户注入问题
				if(action!=null&&action instanceof UserAware){
					((UserAware)action).setUser(user);
				}
				//是否是超级管理员
				if(user.isSuperAdmin()){
					return true;
				}else{
					//是否有权限
					if(user.hasRight(r)){
						return true;
					}else{
						return false;
					}
				}
			}
		}
	}
}
