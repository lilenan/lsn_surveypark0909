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
 * 日志记录仪
 */
public class Logger {
	private LogService logService;
	//注入logService
	public void setLogService(LogService logService) {
		this.logService = logService;
	}
	/**
	 * 记录日志
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
			
			//调用目标对象的方法
			Object ret=pjp.proceed();
			//operResult,成功
			log.setOperResult("success");
			//resultMsg,结果消息
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
