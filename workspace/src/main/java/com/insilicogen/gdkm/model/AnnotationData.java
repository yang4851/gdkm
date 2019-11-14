package com.insilicogen.gdkm.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AnnotationData extends NgsDataRegist{

	private static final long serialVersionUID = -2219168075548797135L;
	
	private String submitter;
	
	private String submitOrgan;
	
	private String project;
	
	private String projectType;
	
	private String submitComment;
	
	private String assemblyMethod;
	
	private Code assemblyLevel;
	
	private Code genomeType;
	
	private String genomeCoverage;
	
	private String seqComment;
	
	private String refTitle;
	
	private String pubStatus;
	
	private String refComment;
	
	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}

	public String getSubmitOrgan() {
		return submitOrgan;
	}

	public void setSubmitOrgan(String submitOrgan) {
		this.submitOrgan = submitOrgan;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getSubmitComment() {
		return submitComment;
	}

	public void setSubmitComment(String submitComment) {
		this.submitComment = submitComment;
	}

	public String getAssemblyMethod() {
		return assemblyMethod;
	}

	public void setAssemblyMethod(String assemblyMethod) {
		this.assemblyMethod = assemblyMethod;
	}

	public Code getAssemblyLevel() {
		return assemblyLevel;
	}

	public void setAssemblyLevel(Code assemblyLevel) {
		this.assemblyLevel = assemblyLevel;
	}

	public Code getGenomeType() {
		return genomeType;
	}

	public void setGenomeType(Code genomeType) {
		this.genomeType = genomeType;
	}

	public String getGenomeCoverage() {
		return genomeCoverage;
	}

	public void setGenomeCoverage(String genomeCoverage) {
		this.genomeCoverage = genomeCoverage;
	}

	public String getSeqComment() {
		return seqComment;
	}

	public void setSeqComment(String seqComment) {
		this.seqComment = seqComment;
	}

	public String getRefTitle() {
		return refTitle;
	}

	public void setRefTitle(String refTitle) {
		this.refTitle = refTitle;
	}

	public String getPubStatus() {
		return pubStatus;
	}

	public void setPubStatus(String pubStatus) {
		this.pubStatus = pubStatus;
	}

	public String getRefComment() {
		return refComment;
	}

	public void setRefComment(String refComment) {
		this.refComment = refComment;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
