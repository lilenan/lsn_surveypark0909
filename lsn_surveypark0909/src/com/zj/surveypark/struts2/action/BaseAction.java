package com.zj.surveypark.struts2.action;

import java.lang.reflect.ParameterizedType;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * 抽象的action超类，专门用于继承
 */
public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T>,Preparable {

	private static final long serialVersionUID = -5046952223587295777L;
	public T model;
	@SuppressWarnings("unchecked")
	public BaseAction(){
		try {
			ParameterizedType type=(ParameterizedType) this.getClass().getGenericSuperclass();
			Class<T> clazz=(Class<T>) type.getActualTypeArguments()[0];
			model=clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	public void prepare() throws Exception {
	}
	public T getModel(){
		return model;
	}
	
}
