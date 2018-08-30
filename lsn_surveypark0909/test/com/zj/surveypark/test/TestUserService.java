package com.zj.surveypark.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zj.surveypark.domain.User;
import com.zj.surveypark.service.UserService;

public class TestUserService {
	private static ApplicationContext ac=null;
	@BeforeClass
	public static void iniAC(){
		ac=new ClassPathXmlApplicationContext("beans.xml");
	}
	
	@Test
	public void insertUser(){
		UserService us=(UserService) ac.getBean("userService");
		User u=new User();
		
		u.setEmail("xpc@itcast.cn");
		u.setPassword("123456");
		us.saveEntity(u);
	}
}
