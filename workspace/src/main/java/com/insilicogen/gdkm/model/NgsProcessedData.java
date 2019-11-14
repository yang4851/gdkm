package com.insilicogen.gdkm.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class NgsProcessedData implements Serializable {

	private static final long serialVersionUID = -3116695274291554994L;

	private int registId;
	
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
	
	private String refPubStatus;
	
	private String refComment;
	
	public int getRegistId() {
		return registId;
	}

	public void setRegistId(int registId) {
		this.registId = registId;
	}

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

	public String getRefPubStatus() {
		return refPubStatus;
	}

	public void setRefPubStatus(String refPubStatus) {
		this.refPubStatus = refPubStatus;
	}

	public String getRefComment() {
		return refComment;
	}

	public void setRefComment(String refComment) {
		this.refComment = refComment;
	}

	@Override
	public boolean equals(Object obj){
		if(obj == null || getClass() != obj.getClass())
			return false;
		
		if(this == obj)
			return true;
		
		NgsProcessedData model = (NgsProcessedData) obj;
		if(registId < 1 || model.getRegistId() < 1)
			return false;
		
		return (registId == model.getRegistId());
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
