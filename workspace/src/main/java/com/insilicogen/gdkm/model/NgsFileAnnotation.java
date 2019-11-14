package com.insilicogen.gdkm.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class NgsFileAnnotation extends AbstractIdObject{
	
	private static final long serialVersionUID = -7824839802761549465L;

	private NgsDataAchive achive;
	
	private int geneCount;
	
	private int cdsCount;
	
	private User registUser;
	
	private Date registDate;

	public NgsFileAnnotation() {
		super();
	}
	
	public NgsFileAnnotation(Integer annotationId) {
		super(annotationId);
	}
	
	public NgsDataAchive getAchive() {
		return achive;
	}

	public void setAchive(NgsDataAchive achive) {
		this.achive = achive;
	}

	public int getGeneCount() {
		return geneCount;
	}

	public void setGeneCount(int geneCount) {
		this.geneCount = geneCount;
	}

	public int getCdsCount() {
		return cdsCount;
	}

	public void setCdsCount(int cdsCount) {
		this.cdsCount = cdsCount;
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
