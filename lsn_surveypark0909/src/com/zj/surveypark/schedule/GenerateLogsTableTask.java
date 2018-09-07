package com.zj.surveypark.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.zj.surveypark.service.LogService;
import com.zj.surveypark.util.LogUtil;

/**
 * ʹ��spring���ɵ�ʯӢ���ȣ���̬������־��
 */
public class GenerateLogsTableTask extends QuartzJobBean {

	//
	private LogService logService; 
	public void setLogService(LogService logService) {
		this.logService = logService;
	}
	
	/**
	 * ִ������
	 */
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		//ddl���
		String table=LogUtil.generateLogTableName(1); 
		String sql="create table if not exists "+LogUtil.generateLogTableName(1)+" like logs";
		logService.executeSQL(sql);
		System.out.println(table+"������");
		table=LogUtil.generateLogTableName(2);
		sql="create table if not exists "+LogUtil.generateLogTableName(2)+" like logs";
		logService.executeSQL(sql);
		System.out.println(table+"������");
	}

}
