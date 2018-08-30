package com.zj.surveypark.util;

/**
 * String工具类
 */
public class StringUtil {
	/**
	 * 将字符串拆分成数组
	 */
	public static String[] str2Arr(String str,String tag){
		if(ValidateUtil.isValid(str)){
			return str.split(tag);
		}
		return null;
	}

	/**
	 * 判断数组中是否含有指定的串
	 */
	public static boolean contains(String[] arr, String value) {
		if(ValidateUtil.isValid(arr)){
			for(String s:arr){
				if(s.equals(value)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 将数组转换成字符串，使用","分隔
	 */
	public static String arr2Str(String[] value){
		String tmp="";
		if(ValidateUtil.isValid(value)){
			for(String s:value){
				tmp=tmp+s+",";
			}
			return tmp.substring(0,tmp.length()-1);
		}
		return null;
	}
}
