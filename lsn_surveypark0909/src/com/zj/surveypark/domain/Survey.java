package com.zj.surveypark.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Survey
 */
public class Survey extends BaseEntity{

	private static final long serialVersionUID = 3351359893694085688L;
	private Integer id;
	private String title="未命名";
	private String preText="上一步";
	private String nextText="下一步";
	private String exitText="退出";
	private String doneText="完成";
	private Date createTime=new Date();
	private float maxOrderno;//最大页序
	private float minOrderno;//最小页序
	private boolean closed;//打开/关闭调查
	private String logoPhotoPath;//logo图片路径
	private User user;//建立从Survey到User之间多对一关联关系
	private Set<Page> pages=new HashSet<>();
	public Set<Page> getPages() {
		return pages;
	}
	public void setPages(Set<Page> pages) {
		this.pages = pages;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPreText() {
		return preText;
	}
	public void setPreText(String preText) {
		this.preText = preText;
	}
	public String getNextText() {
		return nextText;
	}
	public void setNextText(String nextText) {
		this.nextText = nextText;
	}
	public String getExitText() {
		return exitText;
	}
	public void setExitText(String exitText) {
		this.exitText = exitText;
	}
	public String getDoneText() {
		return doneText;
	}
	public void setDoneText(String doneText) {
		this.doneText = doneText;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public boolean isClosed() {
		return closed;
	}
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	public String getLogoPhotoPath() {
		return logoPhotoPath;
	}
	public void setLogoPhotoPath(String logoPhotoPath) {
		this.logoPhotoPath = logoPhotoPath;
	}
	public float getMaxOrderno() {
		return maxOrderno;
	}
	public void setMaxOrderno(float maxOrderno) {
		this.maxOrderno = maxOrderno;
	}
	public float getMinOrderno() {
		return minOrderno;
	}
	public void setMinOrderno(float minOrderno) {
		this.minOrderno = minOrderno;
	}
}
