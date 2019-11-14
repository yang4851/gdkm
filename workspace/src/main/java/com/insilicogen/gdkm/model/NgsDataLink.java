package com.insilicogen.gdkm.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class NgsDataLink implements Serializable {

	private static final long serialVersionUID = 1L;

	private String relation;
	
	private NgsDataRegist rawData;
	
	private NgsDataRegist processedData;

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}
	
	public NgsDataRegist getRawData() {
		return rawData;
	}

	public void setRawData(NgsDataRegist rawData) {
		this.rawData = rawData;
	}

	public NgsDataRegist getProcessedData() {
		return processedData;
	}

	public void setProcessedData(NgsDataRegist processedData) {
		this.processedData = processedData;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || getClass() != obj.getClass())
			return false;
		
		if(this == obj)
			return true;
		
		NgsDataRegist model = (NgsDataRegist) obj;
		
		if(rawData.equals(processedData))
			return false;
		
		return (rawData.equals(model.getRawData()) && processedData.equals(model.getProcessedData()));
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
