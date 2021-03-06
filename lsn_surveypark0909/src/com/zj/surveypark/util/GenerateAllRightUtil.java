package com.zj.surveypark.util;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zj.surveypark.service.RightService;

/**
 * 生成所有权限的工具类
 */
public class GenerateAllRightUtil {

	public static void main(String[] args) throws Exception {
		ApplicationContext ac=new ClassPathXmlApplicationContext("beans.xml");
		RightService rs=(RightService) ac.getBean("rightService");
		URL url=GenerateAllRightUtil.class.getClassLoader().getSystemResource("com/zj/surveypark/struts2/action");
		File dir=new File(url.toURI());
		File[] files=dir.listFiles();
		String fname="";
		for(File f:files){
			fname=f.getName();
			if(fname.endsWith("class")&&!fname.equals("BaseAction.class")&&!fname.equals("UserAware.class")){
				processClass(fname,rs);
			}
		}
	}

	/**
	 * 处理每个类
	 * @throws ClassNotFoundException 
	 */
	private static void processClass(String fname,RightService rs) throws Exception {
		String pkgName="com.zj.surveypark.struts2.action";
		String simpleClassName=fname.substring(0,fname.indexOf("."));
		Class clazz=Class.forName(pkgName+"."+simpleClassName);
		Method[] methods=clazz.getDeclaredMethods();
		String mname="";//方法名
		Class retType=null;
		Class[] paramType=null;
		String url="";
		for(Method m:methods){
			mname=m.getName();
			retType=m.getReturnType();
			paramType=m.getParameterTypes();
			if(retType==String.class&&!ValidateUtil.isValid(paramType)&&Modifier.isPublic(m.getModifiers())){
				if(mname.equals("execute")){
					url="/"+simpleClassName;
				}else{
					url="/"+simpleClassName+"_"+mname;
				}
				rs.appendRightByUrl(url);
			}
		}
	}

}
