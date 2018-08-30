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
	//确认密码
	private String confirmPassword;
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	//注入userService
	@Resource
	private UserService userService;
	//到达注册页面
	@SkipValidation
	public String toRegPage(){
		return "regPage";
	}
	//进行注册
	public String doReg(){
		model.setPassword(DataUtil.md5(model.getPassword()));
		userService.saveEntity(model);
		return "success";
	}
	public void validate(){
		//非空
		if(!ValidateUtil.isValid(model.getEmail())){
			addFieldError("email", "email是必填项！");
		}
		if(!ValidateUtil.isValid(model.getPassword())){
			addFieldError("password", "密码是必填项！");
		}
		if(!ValidateUtil.isValid(model.getNickName())){
			addFieldError("nickName", "nickName是必填项！");
		}
		if(this.hasErrors()){
			return;
		}
		//密码一致性
		if(!model.getPassword().equals(confirmPassword)){
			addFieldError("password", "密码输入不一致");
			return;
		}
		//email是否占用
		boolean b=userService.isRegisted(model.getEmail());
		if(b){
			addFieldError("email", "邮箱已占用！");
		}
	}
}
