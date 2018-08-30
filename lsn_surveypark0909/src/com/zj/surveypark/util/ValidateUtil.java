package com.zj.surveypark.util;

import java.util.Collection;

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
}
