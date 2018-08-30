package com.zj.surveypark.struts2.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zj.surveypark.domain.Answer;
import com.zj.surveypark.domain.Question;
import com.zj.surveypark.service.SurveyService;

/**
 * 收集调查
 */
@Controller
@Scope("prototype")
public class CollectSurveyAction extends BaseAction<Question> {
	private static final long serialVersionUID = -1081115775237548300L;
	private Integer sid;
	@Resource
	private SurveyService surveyService;
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public String execute() throws Exception {
		return SUCCESS;
	}
	public InputStream getIs(){
		try{
			HSSFWorkbook wb=new HSSFWorkbook();
			HSSFSheet sheet=wb.createSheet("surveypark sheet");
			HSSFRow row=sheet.createRow(0);
			HSSFCell cell=null;
			List<Question> questions=surveyService.getQuestions(sid);
			
			HSSFCellStyle style=wb.createCellStyle();
			style.setWrapText(true);
			
			//创建表头
			Question q=null;
			Map<Integer, Integer> qidIndexMap=new HashMap<>();
			for(int i=0;i<questions.size();i++){
				q=questions.get(i);
				cell=row.createCell(i);
				cell.setCellValue(q.getTitle());
				cell.setCellStyle(style);//样式
				sheet.setColumnWidth(i, 8000);//列宽
				qidIndexMap.put(q.getId(), i);
			}
			
			//
			String oldUuid="";
			String newUuid="";
			int rowIndex=0;
			List<Answer> answers=surveyService.findAnswers(sid);
			for(Answer a:answers){
				newUuid=a.getUuid();
				if(!oldUuid.equals(newUuid)){
					oldUuid=newUuid;
					rowIndex++;
					row=sheet.createRow(rowIndex);
				}
				cell=row.createCell(qidIndexMap.get(a.getQuestionId()));
				cell.setCellValue(a.getAnswerIds());
				cell.setCellStyle(style);
			}
			ByteArrayOutputStream boas=new ByteArrayOutputStream();
			wb.write(boas);
			return new ByteArrayInputStream(boas.toByteArray());
		}catch(Exception e){
			e.printStackTrace();
		}
		return  null;
	}
}
