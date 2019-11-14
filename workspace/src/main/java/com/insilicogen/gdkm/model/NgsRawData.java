package com.insilicogen.gdkm.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class NgsRawData implements Serializable {
	
	private static final long serialVersionUID = -2219168075548797135L;
	
	private int registId;
	
	private Code exprType;
	
	private Code domain;
	
	private String cultureCondition;
	
	private String treatment;
	
	private Code replicate;
	
	private String sampleName;
	
	private String organismType;
	
	private String sampleType;
	
	private Code sampleSource;
	
	private String geographLocation;
	
	private String sampleComment;
	
	private Code platform;
	
	private String construction;
	
	private String selection;
	
	private String sequencer;
	
	private String strategy;
	
	private Code readType;
	
	private int readLength;
	
	private int insertSize;
	
	private String adapterSeq;
	
	private String qualityScoreVersion;
	
	private String baseCallingProgram;
	
	private String libraryComment;

	public String getLibraryComment() {
		return libraryComment;
	}

	public void setLibraryComment(String libraryComment) {
		this.libraryComment = libraryComment;
	}

	public int getRegistId() {
		return registId;
	}

	public void setRegistId(int registId) {
		this.registId = registId;
	}

	public Code getExprType() {
		return exprType;
	}

	public void setExprType(Code exprType) {
		this.exprType = exprType;
	}

	public Code getDomain() {
		return domain;
	}

	public void setDomain(Code domain) {
		this.domain = domain;
	}

	public String getCultureCondition() {
		return cultureCondition;
	}

	public void setCultureCondition(String cultureCondition) {
		this.cultureCondition = cultureCondition;
	}

	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public Code getReplicate() {
		return replicate;
	}

	public void setReplicate(Code replicate) {
		this.replicate = replicate;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public String getOrganismType() {
		return organismType;
	}

	public void setOrganismType(String organismType) {
		this.organismType = organismType;
	}

	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}

	public Code getSampleSource() {
		return sampleSource;
	}

	public void setSampleSource(Code sampleSource) {
		this.sampleSource = sampleSource;
	}

	public String getGeographLocation() {
		return geographLocation;
	}

	public void setGeographLocation(String geographLocation) {
		this.geographLocation = geographLocation;
	}

	public String getSampleComment() {
		return sampleComment;
	}

	public void setSampleComment(String sampleComment) {
		this.sampleComment = sampleComment;
	}

	public Code getPlatform() {
		return platform;
	}

	public void setPlatform(Code platform) {
		this.platform = platform;
	}

	public String getConstruction() {
		return construction;
	}

	public void setConstruction(String construction) {
		this.construction = construction;
	}

	public String getSelection() {
		return selection;
	}

	public void setSelection(String selection) {
		this.selection = selection;
	}

	public String getSequencer() {
		return sequencer;
	}

	public void setSequencer(String sequencer) {
		this.sequencer = sequencer;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public Code getReadType() {
		return readType;
	}

	public void setReadType(Code readType) {
		this.readType = readType;
	}

	public int getReadLength() {
		return readLength;
	}

	public void setReadLength(int readLength) {
		this.readLength = readLength;
	}

	public int getInsertSize() {
		return insertSize;
	}

	public void setInsertSize(int insertSize) {
		this.insertSize = insertSize;
	}

	public String getAdapterSeq() {
		return adapterSeq;
	}

	public void setAdapterSeq(String adapterSeq) {
		this.adapterSeq = adapterSeq;
	}

	public String getQualityScoreVersion() {
		return qualityScoreVersion;
	}

	public void setQualityScoreVersion(String qualityScoreVersion) {
		this.qualityScoreVersion = qualityScoreVersion;
	}

	public String getBaseCallingProgram() {
		return baseCallingProgram;
	}

	public void setBaseCallingProgram(String baseCallingProgram) {
		this.baseCallingProgram = baseCallingProgram;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null || getClass() != obj.getClass())
			return false;
		
		if(this == obj)
			return true;
		
		NgsRawData model = (NgsRawData) obj;
		if(registId < 1 || model.getRegistId() < 1)
			return false;
		
		return (registId == model.getRegistId());
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
