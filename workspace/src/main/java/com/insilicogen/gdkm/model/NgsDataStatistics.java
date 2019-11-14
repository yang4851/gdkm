package com.insilicogen.gdkm.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class NgsDataStatistics implements Serializable {

	private static final long serialVersionUID = 4395185201970989883L;
	
	private Taxonomy taxonomy;
	
	private String period;
	
	private Integer month;
	
	private Integer rawCount;
	
	private Long rawSize;
	
	private Integer processedCount;
	
	private Long processedSize;

	private Integer fileCount;
	
	private Long fileSize;

	public Taxonomy getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(Taxonomy taxonomy) {
		this.taxonomy = taxonomy;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}
	
	public Integer getRawCount() {
		return rawCount;
	}

	public void setRawCount(Integer rawCount) {
		this.rawCount = rawCount;
	}

	public Long getRawSize() {
		return rawSize;
	}

	public void setRawSize(Long rawSize) {
		this.rawSize = rawSize;
	}

	public Integer getProcessedCount() {
		return processedCount;
	}

	public void setProcessedCount(Integer processedCount) {
		this.processedCount = processedCount;
	}

	public Long getProcessedSize() {
		return processedSize;
	}

	public void setProcessedSize(Long processedSize) {
		this.processedSize = processedSize;
	}

	public Integer getFileCount() {
		return fileCount;
	}

	public void setFileCount(Integer fileCount) {
		this.fileCount = fileCount;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
}
