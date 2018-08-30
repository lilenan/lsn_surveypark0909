package com.zj.surveypark.struts2.action;

import java.text.DecimalFormat;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zj.surveypark.domain.Question;
import com.zj.surveypark.domain.statistics.OptionStatisticsModel;
import com.zj.surveypark.domain.statistics.QuestionStatisticsModel;
import com.zj.surveypark.service.StatisticsService;

/**
 * MatrixStatisticsAction
 */
@Controller
@Scope("prototype")
public class MatrixStatisticsAction extends BaseAction<Question> {
	private static final long serialVersionUID = -310244262024845825L;
	private String[] colors={"#ff0000","#00ff00","#0000ff","#ffff00","#ff00ff","#000fff"};
	private Integer qid;
	private QuestionStatisticsModel qsm;
	
	@Resource
	private StatisticsService ss;
	public Integer getQid() {
		return qid;
	}
	public void setQid(Integer qid) {
		this.qid = qid;
	}
	public QuestionStatisticsModel getQsm() {
		return qsm;
	}
	public void setQsm(QuestionStatisticsModel qsm) {
		this.qsm = qsm;
	}
	
	public String[] getColors() {
		return colors;
	}
	/*public void setColors(String[] colors) {
		this.colors = colors;
	}*/
	public String execute() throws Exception {
		this.qsm=ss.statistics(qid);
		return ""+qsm.getQuestion().getQuestionType();
	}
	
	/**
	 * 计算比例
	 */
	public String getScale(int rindex,int cindex){
		int qcount=qsm.getCount();
		int ocount=0;
		for(OptionStatisticsModel osm:qsm.getOsms()){
			if(osm.getMatrixRowIndex()==rindex&&osm.getMatrixColIndex()==cindex){
				ocount=osm.getCount();
				break;
			}
		}
		float scale=0;
		if(qcount!=0){
			scale=(float)ocount/(float)qcount;
		}
		scale=scale*100;
		DecimalFormat format=new DecimalFormat();
		format.applyPattern("#,###.00");
		return ""+ocount+"("+format.format(scale)+")";
	}
	
	/**
	 * 计算比例
	 */
	public String getScale(int rindex,int cindex,int oindex){
		int qcount=qsm.getCount();
		int ocount=0;
		for(OptionStatisticsModel osm:qsm.getOsms()){
			if(osm.getMatrixRowIndex()==rindex&&osm.getMatrixColIndex()==cindex&&osm.getMatrixSelectIndex()==oindex){
				ocount=osm.getCount();
				break;
			}
		}
		float scale=0;
		if(qcount!=0){
			scale=(float)ocount/(float)qcount;
		}
		scale=scale*100;
		DecimalFormat format=new DecimalFormat();
		format.applyPattern("#,###.00");
		return ""+ocount+"("+format.format(scale)+")";
	}
	
	public int getPercent(int rindex,int cindex,int oindex){
		int qcount=qsm.getCount();
		int ocount=0;
		for(OptionStatisticsModel osm:qsm.getOsms()){
			if(osm.getMatrixRowIndex()==rindex
					&&osm.getMatrixColIndex()==cindex
					&&osm.getMatrixSelectIndex()==oindex){
				ocount=osm.getCount();
				break;
			}
		}
		float scale=0;
		if(qcount!=0){
			scale=(float)ocount/(float)qcount;
		}
		scale=scale*100;
		return (int)scale;
	}
}
