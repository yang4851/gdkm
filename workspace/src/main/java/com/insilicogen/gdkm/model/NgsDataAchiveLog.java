package com.insilicogen.gdkm.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class NgsDataAchiveLog extends AbstractIdObject {

	private static final long serialVersionUID = 4384315464479983005L;

	private int achiveId;
	
	private String processType;
	
	private String processStatus;
	
	private String processLog;
	
	private User registUser;
	
	private Date registDate;

	public NgsDataAchiveLog() {
		super();
	}
	
	public NgsDataAchiveLog(Integer logId) {
		super(logId);
	}
	
	public int getAchiveId() {
		return achiveId;
	}

	public void setAchiveId(int achiveId) {
		this.achiveId = achiveId;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public String getProcessLog() {
		return processLog;
	}

	public void setProcessLog(String processLog) {
		this.processLog = processLog;
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
