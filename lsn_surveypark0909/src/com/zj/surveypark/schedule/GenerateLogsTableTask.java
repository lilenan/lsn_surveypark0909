package com.zj.surveypark.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.zj.surveypark.service.LogService;
import com.zj.surveypark.util.LogUtil;

/**
 * 使用spring集成的石英调度，动态生成日志表
 */
public class GenerateLogsTableTask extends QuartzJobBean {

	//
	private LogService logService; 
	public void setLogService(LogService logService) {
		this.logService = logService;
	}
	
	/**
	 * 执行任务
	 */
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		//ddl语句
		String table=LogUtil.generateLogTableName(1); 
		String sql="create table if not exists "+LogUtil.generateLogTableName(1)+" like logs";
		logService.executeSQL(sql);
		System.out.println(table+"生成了");
		table=LogUtil.generateLogTableName(2);
		sql="create table if not exists "+LogUtil.generateLogTableName(2)+" like logs";
		logService.executeSQL(sql);
		System.out.println(table+"生成了");
	}

}
