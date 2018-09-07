package com.zj.surveypark.service;



import java.util.List;

import com.zj.surveypark.domain.Log;

public interface LogService extends BaseService<Log> {

	/**
	 * 查询最近的日志信息，默认是2个月
	 */
	public List<Log> findNearestLogs();


}
