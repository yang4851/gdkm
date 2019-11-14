package com.insilicogen.gdkm.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BlastResult {
	
	private Integer id;
	private String queryId;
	private String subjectId;
	private Float identity;
	private Integer length;
	private Integer mismatches;
	private Integer gapOpenings;
	private Integer queryStart;
	private Integer queryEnd;
	private Integer subjectStart;
	private Integer subjectEnd;
	private String eValue;
	private Float bitScore;
	
//	private String pairwise;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getQueryId() {
		return queryId;
	}
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public Float getIdentity() {
		return identity;
	}
	public void setIdentity(Float identity) {
		this.identity = identity;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Integer getMismatches() {
		return mismatches;
	}
	public void setMismatches(Integer mismatches) {
		this.mismatches = mismatches;
	}
	public Integer getGapOpenings() {
		return gapOpenings;
	}
	public void setGapOpenings(Integer gapOpenings) {
		this.gapOpenings = gapOpenings;
	}
	public Integer getQueryStart() {
		return queryStart;
	}
	public void setQueryStart(Integer queryStart) {
		this.queryStart = queryStart;
	}
	public Integer getQueryEnd() {
		return queryEnd;
	}
	public void setQueryEnd(Integer queryEnd) {
		this.queryEnd = queryEnd;
	}
	public Integer getSubjectStart() {
		return subjectStart;
	}
	public void setSubjectStart(Integer subjectStart) {
		this.subjectStart = subjectStart;
	}
	public Integer getSubjectEnd() {
		return subjectEnd;
	}
	public void setSubjectEnd(Integer subjectEnd) {
		this.subjectEnd = subjectEnd;
	}
	public String geteValue() {
		return eValue;
	}
	public void seteValue(String eValue) {
		this.eValue = eValue;
	}
	public Float getBitScore() {
		return bitScore;
	}
	public void setBitScore(Float bitScore) {
		this.bitScore = bitScore;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
