package com.zj.surveypark.util;

import java.util.Collection;

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
}
