package com.insilicogen.gdkm.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AniRequest {

	private String method;
	private List<AniFile> selectedFiles;
	private String outputDir;
	private NgsFileSeqQuantity seqQuantity;
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public List<AniFile> getSelectedFiles() {
		return selectedFiles;
	}
	public void setSelectedFiles(List<AniFile> selectedFiles) {
		this.selectedFiles = selectedFiles;
	}
	public String getOutputDir() {
		return outputDir;
	}
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	public NgsFileSeqQuantity getSeqQuantity() {
		return seqQuantity;
	}
	public void setSeqQuantity(NgsFileSeqQuantity seqQuantity) {
		this.seqQuantity = seqQuantity;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}	
}
