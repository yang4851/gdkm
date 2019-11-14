package com.insilicogen.gdkm.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class NgsDataFeaturesXref implements Serializable{
	
	private int xrefId;
	private String type;
	private String value;
	private int featuresId;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public int getXrefId() {
		return xrefId;
	}
	public void setXrefId(int xrefId) {
		this.xrefId = xrefId;
	}
	public int getFeaturesId() {
		return featuresId;
	}
	public void setFeaturesId(int featuresId) {
		this.featuresId = featuresId;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	
	
}
