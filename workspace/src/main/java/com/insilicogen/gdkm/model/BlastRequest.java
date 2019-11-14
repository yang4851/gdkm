package com.insilicogen.gdkm.model;

import java.util.Arrays;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BlastRequest {
	
	private String uploadString;
	private String toolName;
	private String[] dbNames;
	private String expect;
	private String filter;
	private String uploadFileValue;
	public String getUploadString() {
		return uploadString;
	}
	public void setUploadString(String uploadString) {
		this.uploadString = uploadString;
	}
	public String getToolName() {
		return toolName;
	}
	public void setToolName(String toolName) {
		this.toolName = toolName;
	}
	public String getExpect() {
		return expect;
	}
	public void setExpect(String expect) {
		this.expect = expect;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getUploadFileValue() {
		return uploadFileValue;
	}
	public void setUploadFileValue(String uploadFileValue) {
		this.uploadFileValue = uploadFileValue;
	}
	public String[] getDbNames() {
		return dbNames;
	}
	public void setDbNames(String[] dbNames) {
		this.dbNames = dbNames;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
