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
	//�ж��ַ�����Ч��
	public static boolean isValid(String str){
		if(str==null||"".equals(str.trim())){
			return false;
		}
		return true;
	}
	
	/**
	 * �жϼ�����Ч��
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isValid(Collection col){
		if(col==null||col.isEmpty()){
			return false;
		}
		return true;
	}

	/**
	 * �ж�������Ч��
	 */
	public static boolean isValid(Object[] arr) {
		if(arr==null||arr.length==0){
			return false;
		}
		return true;
	}
	
	/**
	 * �ж��Ƿ���Ȩ��
	 */
	@SuppressWarnings("unchecked")
	public static boolean hasRight(String ns,String actionName,HttpServletRequest request,BaseAction action){
		if(!ValidateUtil.isValid(ns)||"/".equals(ns)){
			ns="";
		}
		
		//�����?������action
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
		//�Ƿ�Ϊ������Դ
		if(r==null||r.isCommon()){
			return true;
		}else{
			User user=(User) s.getAttribute("user");
			//�Ƿ��¼
			if(user==null){
				return false;
			}else{
				//����userAware�û�ע������
				if(action!=null&&action instanceof UserAware){
					((UserAware)action).setUser(user);
				}
				//�Ƿ��ǳ�������Ա
				if(user.isSuperAdmin()){
					return true;
				}else{
					//�Ƿ���Ȩ��
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
