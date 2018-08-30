package com.zj.surveypark.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;

/**
 * ���ݹ�����
 */
public class DataUtil {
/**
 * ����md5����
 */
	public static String md5(String src){
		try {
			StringBuffer buffer=new StringBuffer();
			char[] chars={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
			MessageDigest md= MessageDigest.getInstance("MD5");
			byte[] data=md.digest(src.getBytes());
			for(byte b:data){
				//��4λ
				buffer.append(chars[(b>>4)&0x0F]);
				//��4λ
				buffer.append(chars[b&0x0F]);
			}
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ��ȸ��ƣ�������������ͼ
	 */
	public static Serializable deeplyCopy(Serializable src){
		try{
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(baos);
			oos.writeObject(src);
			oos.close();
			baos.close();
			byte[] data=baos.toByteArray();
			ByteArrayInputStream bais=new ByteArrayInputStream(data);
			ObjectInputStream ois=new ObjectInputStream(bais);
			Serializable copy=(Serializable) ois.readObject();
			ois.close();
			bais.close();
			return copy;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
