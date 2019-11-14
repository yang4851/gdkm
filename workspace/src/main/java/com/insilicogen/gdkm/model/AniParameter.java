package com.insilicogen.gdkm.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AniParameter {
	
	private String inputFileDir;
	private String method;
	
	public String getInputFileDir() {
		return inputFileDir;
	}
	public void setInputFileDir(String inputFileDir) {
		this.inputFileDir = inputFileDir;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
