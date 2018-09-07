package com.zj.surveypark.service.impl;



import java.util.List;

import javax.annotation.Resource;

import org.hibernate.id.UUIDHexGenerator;
import org.springframework.stereotype.Service;

import com.zj.surveypark.dao.BaseDao;
import com.zj.surveypark.domain.Log;
import com.zj.surveypark.service.LogService;
import com.zj.surveypark.util.LogUtil;

/**
 * logService
 */
@Service("logService")
public class LogServiceImpl extends BaseServiceImpl<Log> implements
		LogService{
	UUIDHexGenerator id=new UUIDHexGenerator();
	/**
	 * 重写该方法,覆盖注解
	 */
	@Resource(name="logDao")
	public void setDao(BaseDao<Log> dao) {
		super.setDao(dao); 
	}
	
	public void saveEntity(Log t) {
		StringBuffer sql=new StringBuffer("insert into ");
		sql.append(LogUtil.generateLogTableName(0));
		sql.append(" (id,operator,opertime,opername,operparams,operresult,resultmsg) ");
		sql.append("values (?,?,?,?,?,?,?)");
		
		String uuid=(String) id.generate(null, null);
		this.executeSQL(sql.toString(), uuid, t.getOperator(),t.getOperTime(),t.getOperName(),t.getOperParams(),t.getOperResult(),t.getResultMsg());
	}

	public List<Log> findNearestLogs() {
		StringBuffer sql=new StringBuffer("select * from ");
		sql.append(LogUtil.generateLogTableName(-1)+" union ");
		sql.append("select * from "+LogUtil.generateLogTableName(0));
		
		return this.findObjectBySql(sql.toString());
	}

	
}
