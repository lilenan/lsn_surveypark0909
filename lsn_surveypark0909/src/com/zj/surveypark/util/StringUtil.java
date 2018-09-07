package com.zj.surveypark.util;

/**
 * String������
 */
public class StringUtil {
	/**
	 * ���ַ�����ֳ�����
	 */
	public static String[] str2Arr(String str, String tag) {
		if (ValidateUtil.isValid(str)) {
			return str.split(tag);
		}
		return null;
	}

	/**
	 * �ж��������Ƿ���ָ���Ĵ�
	 */
	public static boolean contains(String[] arr, String value) {
		if (ValidateUtil.isValid(arr)) {
			for (String s : arr) {
				if (s.equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * ������ת�����ַ�����ʹ��","�ָ�
	 */
	public static String arr2Str(Object[] value) {
		String tmp = "";
		if (ValidateUtil.isValid(value)) {
			for (Object o : value) {
				tmp = tmp + o + ",";
			}
			return tmp.substring(0, tmp.length() - 1);
		}
		return null;
	}
	
	public static String getDescString(String str){
		if(ValidateUtil.isValid(str)&&str.length()>30){
			return str.substring(0,29);
		}
		return str;
	}
}
