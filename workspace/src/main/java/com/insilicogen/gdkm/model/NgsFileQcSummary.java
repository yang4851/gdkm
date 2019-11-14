package com.insilicogen.gdkm.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class NgsFileQcSummary extends AbstractIdObject {

	private static final long serialVersionUID = 5929357312064917369L; 

	private NgsDataAchive achive;
	
	private String summary;
	
	private String status;
	
	private User registUser;
	
	private Date registDate;

	public NgsFileQcSummary() {
		super();
	}
	
	public NgsFileQcSummary(Integer summaryId) {
		super(summaryId);
	}
	
	public NgsDataAchive getAchive() {
		return achive;
	}

	public void setAchive(NgsDataAchive achive) {
		this.achive = achive;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getRegistUser() {
		return registUser;
	}

	public void setRegistUser(User registUser) {
		this.registUser = registUser;
	}

	public Date getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
