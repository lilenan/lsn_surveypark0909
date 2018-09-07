package com.zj.surveypark.struts2.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zj.surveypark.domain.Log;
import com.zj.surveypark.service.LogService;

/**
 * LogAction
 */
@Controller
@Scope("prototype")
public class LogAction extends BaseAction<Log> {
	private static final long serialVersionUID = -8943606741017627747L;
	private List<Log> allLogs;
	
	@Resource
	private LogService logService;
	
	public List<Log> getAllLogs() {
		return allLogs;
	}
	public void setAllLogs(List<Log> allLogs) {
		this.allLogs = allLogs;
	}
	
	/**
	 * 查看所有日志
	 */
	public String findAllLogs(){
		this.allLogs=logService.findNearestLogs();
		return "logListPage";
	}
}
