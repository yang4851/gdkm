package com.insilicogen.gdkm.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BlastAchiveLink{
	
	private NgsDataAchive achive;
	private List<BlastResult> results;
	private String pairwise;
	
	public String getPairwise() {
		return pairwise;
	}
	public void setPairwise(String pairwise) {
		this.pairwise = pairwise;
	}
	public NgsDataAchive getAchive() {
		return achive;
	}
	public void setAchive(NgsDataAchive achive) {
		this.achive = achive;
	}
	public List<BlastResult> getResults() {
		return results;
	}
	public void setResults(List<BlastResult> results) {
		if(this.results != null)
			this.results.addAll(results);
		else 
			this.results = results;
	}
	public void setResults(BlastResult result) {
		if(this.results == null)
			results = new ArrayList<BlastResult>();
		
		results.add(result);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
