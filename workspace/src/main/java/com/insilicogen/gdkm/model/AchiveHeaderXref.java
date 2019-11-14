package com.insilicogen.gdkm.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AchiveHeaderXref implements Serializable{
	
	private int ahxrefId;
	private int headerId;
	private int achiveId;
	
	public int getAhxrefId() {
		return ahxrefId;
	}
	public void setAhxrefId(int ahxrefId) {
		this.ahxrefId = ahxrefId;
	}
	public int getHeaderId() {
		return headerId;
	}
	public void setHeaderId(int headerId) {
		this.headerId = headerId;
	}
	public int getAchiveId() {
		return achiveId;
	}
	public void setAchiveId(int achiveId) {
		this.achiveId = achiveId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
