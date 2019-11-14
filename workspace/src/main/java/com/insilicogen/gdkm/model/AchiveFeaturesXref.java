package com.insilicogen.gdkm.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AchiveFeaturesXref implements Serializable{
	private int afxrefId;
	private int achiveId;
	private int featuresId;
	
	public int getAfxrefId() {
		return afxrefId;
	}
	public void setAfxrefId(int afxrefId) {
		this.afxrefId = afxrefId;
	}
	public int getAchiveId() {
		return achiveId;
	}
	public void setAchiveId(int achiveId) {
		this.achiveId = achiveId;
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
