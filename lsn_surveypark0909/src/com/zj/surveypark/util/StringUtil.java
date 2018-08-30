package com.zj.surveypark.util;

/**
 * String������
 */
public class StringUtil {
	/**
	 * ���ַ�����ֳ�����
	 */
	public static String[] str2Arr(String str,String tag){
		if(ValidateUtil.isValid(str)){
			return str.split(tag);
		}
		return null;
	}

	/**
	 * �ж��������Ƿ���ָ���Ĵ�
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
	 * ������ת�����ַ�����ʹ��","�ָ�
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
