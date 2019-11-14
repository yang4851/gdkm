package com.insilicogen.gdkm.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * NGS 데이터 등록 기본정보
 * 
 * @see NgsRawData
 * @see NgsAnnotationData
 */
@JsonInclude(Include.NON_NULL)
public class NgsDataRegistView extends NgsDataRegist {
	
	private List<NgsDataAchive> fileTypes;
	
	public List<NgsDataAchive> getFileTypes() {
		return fileTypes;
	}
	public void setFileTypes(List<NgsDataAchive> fileTypes) {
		this.fileTypes = fileTypes;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
