package com.zj.surveypark.util;

import java.security.MessageDigest;

public class App {

	public static void main(String[] args) throws Exception {
		StringBuffer buffer=new StringBuffer();
		char[] chars={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		String src="123";
		MessageDigest md= MessageDigest.getInstance("MD5");
		byte[] data=md.digest(src.getBytes());
		for(byte b:data){
			//¸ß4Î»
			buffer.append(chars[(b>>4)&0x0F]);
			//µÍ4Î»
			buffer.append(chars[b&0x0F]);
		}
		System.out.println(buffer.toString());
	}

}
