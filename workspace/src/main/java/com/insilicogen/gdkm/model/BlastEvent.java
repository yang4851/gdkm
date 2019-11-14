package com.insilicogen.gdkm.model;

import java.io.File;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BlastEvent {
	private Integer id;
	private String blastTool;
	private String blastDb;
	private File inputFile;
	private String expect;
	private String filter;
	private String event;
	private String fileName;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBlastTool() {
		return blastTool;
	}
	public void setBlastTool(String blastTool) {
		this.blastTool = blastTool;
	}
	public String getBlastDb() {
		return blastDb;
	}
	public void setBlastDb(String blastDb) {
		this.blastDb = blastDb;
	}
	public File getInputFile() {
		return inputFile;
	}
	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
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
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}

