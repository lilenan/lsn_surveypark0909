package com.zj.surveypark.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.zj.surveypark.domain.security.Right;
import com.zj.surveypark.service.RightService;

/**
 * ��ʼ��Ȩ�޼�����
 */
@SuppressWarnings("rawtypes")
@Component
public class IniRightListener implements ApplicationListener,ServletContextAware {

	@Resource
	private RightService rs;
	//����ServletContext����
	private ServletContext sc;
	public void onApplicationEvent(ApplicationEvent arg0) {
		//�Ƿ���������ˢ���¼�
		if(arg0 instanceof ContextRefreshedEvent){
			List<Right> list=rs.findAllEntities();
			Map<String, Right> map=new HashMap<>();
			for(Right r:list){
				map.put(r.getRightUrl(), r);
			}
			sc.setAttribute("all_rights_map", map);
			System.out.println("Ȩ�޳�ʼ�����");
		}
	}

	//ע��sc
	public void setServletContext(ServletContext servletContext) {
		System.out.println("ע��sc");
		this.sc=servletContext;
	}

}
