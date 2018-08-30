package com.zj.surveypark.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zj.surveypark.domain.User;
import com.zj.surveypark.service.SurveyService;

public class TestSurveyService {
	private static ApplicationContext ac=null;
	@BeforeClass
	public static void iniAC(){
		ac=new ClassPathXmlApplicationContext("beans.xml");
	}
	
	@Test
	public void insertUser(){
		SurveyService ss= (SurveyService) ac.getBean("surveyService");
		User u=new User();
		u.setId(6);
		ss.newSurvey(u);
	}
}
