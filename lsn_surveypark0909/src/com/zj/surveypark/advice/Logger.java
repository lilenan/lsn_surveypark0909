package com.zj.surveypark.advice;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.ProceedingJoinPoint;

import com.opensymphony.xwork2.ActionContext;
import com.zj.surveypark.domain.Log;
import com.zj.surveypark.domain.User;
import com.zj.surveypark.service.LogService;
import com.zj.surveypark.util.StringUtil;

/**
 * ��־��¼��
 */
public class Logger {
	private LogService logService;
	//ע��logService
	public void setLogService(LogService logService) {
		this.logService = logService;
	}
	/**
	 * ��¼��־
	 */
	public Object record(ProceedingJoinPoint pjp){
		Log log=new Log();
		try{
			ActionContext ac=ActionContext.getContext();
			//operator
			if(ac!=null){
				HttpServletRequest request=(HttpServletRequest) ac.get(ServletActionContext.HTTP_REQUEST);
				if(request!=null){
					User user=(User) request.getSession().getAttribute("user");
					if(user!=null){
						log.setOperator(""+user.getId()+":"+user.getEmail());
					}
				}
			}
			//User user=(User) ServletActionContext.getRequest().getSession().getAttribute("user");
			//operName
			String methodName=pjp.getSignature().getName();
			log.setOperName(methodName);
			//operParams
			Object[] args=pjp.getArgs();
			log.setOperParams(StringUtil.arr2Str(args));
			
			//����Ŀ�����ķ���
			Object ret=pjp.proceed();
			//operResult,�ɹ�
			log.setOperResult("success");
			//resultMsg,�����Ϣ
			if(ret!=null){
				log.setResultMsg(ret.toString());
			}
			return ret;
		}catch (Throwable e){
			log.setOperResult("failure");
			log.setResultMsg(e.getMessage());
		}finally{
			logService.saveEntity(log);
		}
		return null;
	}
}
