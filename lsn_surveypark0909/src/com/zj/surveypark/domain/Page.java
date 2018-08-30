package com.zj.surveypark.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Page
 */
public class Page implements Serializable {
	
	private static final long serialVersionUID = -7539333158034676404L;
	private Integer id;
	private String title = "δ����";
	private String description;
	private float orderno;//Ҷ��
	
	//����
	private transient Survey survey ;//����transient�ؼ��֣�survey���󽫲��ٴ��л�
	
	//���⼯��
	private Set<Question> questions = new HashSet<Question>();

	public Set<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
		//Ĭ��orderno��id����һ��
		if(id!=null){
			this.orderno=id;
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getOrderno() {
		return orderno;
	}

	public void setOrderno(float orderno) {
		this.orderno = orderno;
	}

}
