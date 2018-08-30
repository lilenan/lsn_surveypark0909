package com.zj.surveypark.struts2.action;

import javax.annotation.Resource;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zj.surveypark.domain.User;
import com.zj.surveypark.service.UserService;
import com.zj.surveypark.util.DataUtil;
import com.zj.surveypark.util.ValidateUtil;

@Controller
@Scope("prototype")
public class RegAction extends BaseAction<User> {

	private static final long serialVersionUID = -331507319122807085L;
	//ȷ������
	private String confirmPassword;
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	//ע��userService
	@Resource
	private UserService userService;
	//����ע��ҳ��
	@SkipValidation
	public String toRegPage(){
		return "regPage";
	}
	//����ע��
	public String doReg(){
		model.setPassword(DataUtil.md5(model.getPassword()));
		userService.saveEntity(model);
		return "success";
	}
	public void validate(){
		//�ǿ�
		if(!ValidateUtil.isValid(model.getEmail())){
			addFieldError("email", "email�Ǳ����");
		}
		if(!ValidateUtil.isValid(model.getPassword())){
			addFieldError("password", "�����Ǳ����");
		}
		if(!ValidateUtil.isValid(model.getNickName())){
			addFieldError("nickName", "nickName�Ǳ����");
		}
		if(this.hasErrors()){
			return;
		}
		//����һ����
		if(!model.getPassword().equals(confirmPassword)){
			addFieldError("password", "�������벻һ��");
			return;
		}
		//email�Ƿ�ռ��
		boolean b=userService.isRegisted(model.getEmail());
		if(b){
			addFieldError("email", "������ռ�ã�");
		}
	}
}
