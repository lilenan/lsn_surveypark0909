package com.zj.surveypark.service;



import java.util.List;

import com.zj.surveypark.domain.Log;

public interface LogService extends BaseService<Log> {

	/**
	 * ��ѯ�������־��Ϣ��Ĭ����2����
	 */
	public List<Log> findNearestLogs();


}
