package com.zj.surveypark.service;

import com.zj.surveypark.domain.statistics.QuestionStatisticsModel;

public interface StatisticsService {
	public QuestionStatisticsModel statistics(Integer qid);
}
